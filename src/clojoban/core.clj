(ns clojoban.core
  "A little Sokoban clone for \"Create a User-Profile Mini-Game\" at http://codegolf.stackexchange.com"
  (:use [clojoban.game.model :only [add-levels]]
        [clojoban.game view controller]
        [clojoban.themes :only [themes add-themes]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware session stacktrace resource file-info]
        [ring.util response io]
        [compojure.core])
  (:require [compojure.route :as route])
  (:gen-class))

(defroutes clojoban
  (GET "/game" []
       (fn [{:keys [headers session]}]
         (let [new-session (into session ((game-controller (headers "referer")) session))]
           (-> (response (piped-input-stream (generate-image new-session)))
             (content-type "image/png")
             (header "Cache-Control" "max-age=0, must-revalidate")
             (header "P3P" "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"")
             (assoc :session new-session))))))

(defroutes app
  clojoban
  (route/resources "/")
  (route/resources "/themes" {:root "themes"})
  (route/resources "/levels" {:root "levels"})
  (route/not-found "<h1>Page not found (404)</h1>"))

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
                 #(clojure.string/replace-first
                    %
                    "/theme/"
                    (str "/themes/"
                         (if-let [theme ((req :session) :theme)]
                           (name theme)
                           "default")
                         "/"))))))

;;; PUBLICS

(def app
  "Wrapped application handler."
  (-> app
    (wrap-root-index)
    (wrap-theme)
    (wrap-session)
    (wrap-stacktrace)))

(defn boot
  "Bootstraps the needed data to start the server."
  ([level-dir theme-dir]
    (add-levels level-dir)
    (add-themes theme-dir))
  ([] (boot "resources/levels" "resources/themes")))

(defn -main
  "Launches the server at port, loading the levels from dir."
  ([port level-dir theme-dir]
    (boot level-dir theme-dir)
    (println "Launching game server on port" (Integer. port))
    (run-jetty app {:port (Integer. port) :join? false}))
  ([port] (-main port "resources/levels" "resources/themes"))
  ([] (-main 1337)))