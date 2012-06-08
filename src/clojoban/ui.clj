(ns clojoban.ui
  "Game UI generation."
  (:use [clojoban.images :only [images]])
  (:import [java.awt Color image.BufferedImage]
           [javax.imageio ImageIO]))

;;; CONSTANTS
(def tile-size
  16)

(def tiles
  {; Map
   :wall (images "wall.png")
   :floor (images "floor.png")
   ; Entities
   :player-up (images "player-up.png")
   :player-down (images "player-down.png")
   :player-left (images "player-left.png")
   :player-right (images "player-right.png")
   :goal (images "goal.png")
   :box (images "box.png")})

;;; PRIVATES
(defn- gen-canvas [{:keys [width height layout player boxes goals]} last-direction]
  (let [[player-x player-y] player
        img-width (* tile-size width)
        img-height (* tile-size height)
        image (BufferedImage. img-width img-height BufferedImage/TYPE_INT_ARGB)
        g (.createGraphics image)]
    (do
      (doseq [y (range height) ; Draw layout
              x (range (count (layout y)))]
          (.drawImage g
            (tiles (get-in layout [y x]))
            (* tile-size x)
            (* tile-size y)
            nil))
      (doseq [goal goals ; Draw goals
              :let [[goal-x goal-y] goal]]
        (.drawImage g
          (tiles :goal)
          (* tile-size goal-x)
          (* tile-size goal-y)
          nil))
      (doseq [box boxes ; Draw boxes
              :let [[box-x box-y] box]]
        (.drawImage g
          (tiles :box)
          (* tile-size box-x)
          (* tile-size box-y)
          nil))
      (.drawImage g ; Draw player
        (tiles last-direction)
        (* tile-size player-x)
        (* tile-size player-y)
        nil)
      image)))

(defn- gen-hud-image [num steps canvas]
  )

;;; PUBLICS
(defn generate-image [{:keys [num steps level last-direction]}]
  "Generates the final UI of the game (level+HUD)"
  (fn [ostream]
    (ImageIO/write (gen-canvas level last-direction) "png" ostream)))