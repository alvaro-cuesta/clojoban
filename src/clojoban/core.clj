(ns clojoban.core
  "A little Sokoban clone for \"Create a User-Profile Mini-Game\" @ http://codegolf.stackexchange.com"
  (:use [clojoban.levels :only [add-levels]]
        [clojoban.tiles :only [add-tiles]]
        [clojoban game-controller ui]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware session stacktrace]
        [ring.util response io])
  (:gen-class))

(defn- update-session [session referer]
  (if referer
    (let [splitted-ref (clojure.string/split referer #"\?")
          action (if (empty? session) "new"
                     (when (= 2 (count splitted-ref))
                       (splitted-ref 1)))]
      ((game-controller action identity) session))))

(def index-html
  #^:private
  (slurp "resources/static/index.html"))

(def route
  #^:private
  {"/" (fn [_]
         (-> (response index-html)
           (content-type "text/html; charset=utf-8")))
   "/game" (fn [{headers :headers, session :session,}]
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
  ([dir]
    (add-levels dir)
    (add-tiles "resources/images"))
  ([] (boot "resources/levels")))

(defn -main
  "Launches the server at port, loading the levels from dir."
  ([port dir]
     (boot dir)
     (println "Launching game server on port" (Integer. port))
     (run-jetty app {:port (Integer. port)}))
  ([port] (-main port "resources/levels"))
  ([] (-main 1337)))