(ns clojoban.core
  "A little Sokoban clone for \"Create a User-Profile Mini-Game\" at http://codegolf.stackexchange.com"
  (:use [clojoban.game.model :only [add-levels]]
        [clojoban.game view controller]
        [clojoban.images :only [images add-images]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware session stacktrace content-type]
        [ring.util response io])
  (:gen-class))

(defn- update-session [session referer]
  (if referer
    (let [splitted-ref (clojure.string/split referer #"\?")
          action (if (empty? session) "_clojoban_new_" ; HACK!
                   (when (= 2 (count splitted-ref))
                     (splitted-ref 1)))]
      ((game-controller action identity) session)))) ; HACK! game-controller

(def cached-static
  #^:private
  {:index-html (slurp "resources/static/index.html")
   :jquery-min-js (slurp "resources/static/jquery.min.js")
   :clojoban-js (slurp "resources/static/clojoban.js")
   :clojoban-css (slurp "resources/static/clojoban.css")
   :favicon-ico (slurp "resources/static/favicon.ico")})

(defn- serve-cached-static
  ([resource type]
    (fn [req] (content-type ((serve-cached-static resource) req) type)))
  ([resource]
    (fn [req] (-> (response (cached-static resource))
                (assoc :cache-control "max-age=86400, must-revalidate")))))

(def route
  #^:private
  {"/" (serve-cached-static :index-html "text/html; charset=UTF-8")
   "/jquery.min.js" (serve-cached-static :jquery-min-js)
   "/clojoban.js" (serve-cached-static :clojoban-js)
   "/clojoban.css" (serve-cached-static :clojoban-css)
   "/favicon.ico" (serve-cached-static :favicon-ico)
   "/game.png" (fn [{:keys [headers session]}]
                 (let [new-session (update-session session (headers "referer"))]
                   (-> (response (piped-input-stream (generate-image new-session)))
                     (assoc :session new-session)
                     (assoc :cache-control "max-age=0, no-cache"))))})

(defn- handler [request]
  ((route (request :uri)
          (fn [_] (-> (not-found "Page not found. Are you messing with me, boy?")
                    (assoc :cache-control "max-age=86400, must-revalidate"))))
    request))

;;; PUBLICS

(def app
  "Application handler, wrapped around session and stacktrace middleware."
  (-> handler
    (wrap-session)
    (wrap-content-type)
    (wrap-stacktrace)))

(defn boot
  "Bootstraps the needed data to start the server."
  ([level-dir theme-dir]
    (add-levels level-dir)
    (add-images theme-dir)
    (add-tiles images))
  ([] (boot "resources/levels" "resources/images")))

(defn -main
  "Launches the server at port, loading the levels from dir."
  ([port level-dir theme-dir]
    (boot level-dir theme-dir)
    (println "Launching game server on port" (Integer. port))
    (run-jetty app {:port (Integer. port) :join? false}))
  ([port] (-main port "resources/levels" "resources/images"))
  ([] (-main 1337)))