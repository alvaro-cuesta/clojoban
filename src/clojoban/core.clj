(ns clojoban.core
  "A little Sokoban clone for \"Create a User-Profile Mini-Game\" at http://codegolf.stackexchange.com"
  (:use [clojoban.game.model :only [add-levels]]
        [clojoban.game view controller]
        [clojoban.images :only [images add-images]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware session stacktrace resource file-info]
        [ring.util response io])
  (:gen-class))

(defn- update-session [session referer]
  (if referer
    (let [splitted-ref (clojure.string/split referer #"\?")
          action (if (empty? session) "_clojoban_new_" ; HACK!
                   (when (= 2 (count splitted-ref))
                     (splitted-ref 1)))]
      ((game-controller action identity) session)))) ; HACK! game-controller


(def route
  #^:private
  {"/game" (fn [{:keys [headers session]}]
                 (let [new-session (update-session session (headers "referer"))]
                   (-> (response (piped-input-stream (generate-image new-session)))
                     (content-type "image/png")
                     (assoc :session new-session)
                     (assoc :cache-control "max-age=0, no-cache"))))})

(defn- handler [request]
  ((route (request :uri)
          (fn [_] (-> (not-found "Page not found. Are you messing with me, boy?")
                    (assoc :cache-control "max-age=86400, must-revalidate"))))
    request))

;;; PUBLICS

(def app
  "Wrapped application handler."
  (-> handler
    (wrap-session)
    (wrap-resource "public")
    (wrap-file-info)
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