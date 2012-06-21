(ns flyweight.core
  "TODO" ; TODO: document this
  (:use [flyweight.utils :only [stream-image split-query]] 
        [ring.util response io]
        [ring.adapter.jetty]))

(defn start
  "Start the game server at port, handling requests on handler."
  ([handler port threads]
    (run-jetty handler
               {:port port
                :join? false
                :max-threads threads}))
  ([handler port]
    (start handler port 50)))

(defn step [game-controller image-generator]
  "Step in-game, mutating gamestate (session data) through game-controller
  and drawing the game using image-generator."
  (fn [{:keys [headers session]}]
    (let [query (split-query (headers "referer"))
          new-session (into session ((game-controller query)
                                      session))]
      (-> (response (piped-input-stream
                      (stream-image (image-generator new-session) "png")))
        (content-type (str "image/" format))
        (header "Cache-Control" "max-age=0, must-revalidate")
        (header "P3P" "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"")
        (assoc :session new-session)))))