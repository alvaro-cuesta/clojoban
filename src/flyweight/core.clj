(ns flyweight.core
  (:use [flyweight.utils :only [stream-image]] 
        [ring.util response io]))

(defn step [game-controller image-generator]
  (fn [{:keys [headers session]}]
    (let [new-session (into session ((game-controller (headers "referer")) session))]
      (-> (response (piped-input-stream (stream-image (image-generator new-session))))
        (content-type "image/png")
        (header "Cache-Control" "max-age=0, must-revalidate")
        (header "P3P" "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"")
        (assoc :session new-session)))))