(ns clojoban.core
  "A little Sokoban clone for \"Create a User-Profile Mini-Game\" at http://codegolf.stackexchange.com"
  (:use [clojoban.game.model :only [add-levels]]
        [clojoban.game view controller]
        [clojoban.images :only [images add-images]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware session stacktrace]
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

(def route
  #^:private
  {"/" (fn [_]
         (-> (response (cached-static :index-html))
           (content-type "text/html; charset=utf-8")))
   "/jquery.min.js" (fn [_]
                      (-> (response (cached-static :jquery-min-js))
                        (content-type "text/javascript")))
   "/clojoban.js" (fn [_]
                    (-> (response (cached-static :clojoban-js))
                      (content-type "text/javascript")))
   "/clojoban.css" (fn [_]
                     (-> (response (cached-static :clojoban-css))
                       (content-type "text/css")))
   "/favicon.ico" (fn [_]
                    (-> (response (cached-static :favicon-ico))
                      (content-type "image/x-icon")))
   "/game" (fn [{:keys [headers session]}]
             (let [new-session (update-session session (headers "referer"))]
               (-> (response (piped-input-stream (generate-image new-session)))
                 (content-type "image/png")
                 (assoc :session new-session))))})

(defn- handler [request]
  ((route (request :uri)
          (fn [_] (-> (not-found "Page not found. Are you messing with me, boy?"))))
    request))

;;; PUBLICS

(def app
  "Application handler, wrapped around session and stacktrace middleware."
  (-> handler
    (wrap-session)
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