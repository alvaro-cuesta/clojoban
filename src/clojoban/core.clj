(ns clojoban.core
  "A little Sokoban clone for \"Create a User-Profile Mini-Game\" at http://codegolf.stackexchange.com"
  (:use [clojoban model view controller]
        [flyweight.core :as fw]
        [flyweight.themes]
        [ring.middleware session stacktrace resource]
        [compojure.core])
  (:require [compojure.route :as route])
  (:gen-class))

(defroutes clojoban
  (GET "/game" [] (fw/step game-controller generate-image))
  (route/resources "/")
  (route/resources "/themes" {:root "themes"})
  (route/resources "/levels" {:root "levels"})
  (route/not-found "<h1>Page not found (404)</h1>")) ; TODO: use templating?

(defn- wrap-root-index [handler]
  (fn [req]
    (handler
      (update-in req [:uri]
                 #(if (= "/" %)
                    "/index.html"
                    %)))))

(defn- wrap-theme [handler]
  (fn [req]
    (handler
      (update-in req [:uri]
                 #(clojure.string/replace-first %
                    "/theme/"
                    (str "/themes/"
                         (if-let [theme ((req :session) :theme)]
                           (name theme)
                           "default")
                         "/"))))))

;;; PUBLICS

(def handler
  "Wrapped application handler."
  (-> clojoban
    (wrap-root-index)
    (wrap-theme)
    (wrap-session)
    (wrap-stacktrace)))

(defn init
  "Bootstraps the needed data to start the server."
  ([level-dir theme-dir]
    (add-levels level-dir)
    (add-themes theme-dir themes))
  ([] (init "resources/levels" "resources/themes")))

(defn -main
  "Launches the server at port, loading the levels from dir."
  ([port level-dir theme-dir threads]
    (init level-dir theme-dir)
    (println "Launching game server on port" (Integer. port))
    (fw/start handler (Integer. port) threads))
  ([port level-dir theme-dir] (-main port level-dir theme-dir 50))
  ([port threads] (-main port "resources/levels" "resources/themes" threads))
  ([port] (-main port "resources/levels" "resources/themes"))
  ([] (-main 1337)))