(ns clojoban.ui
  "Game UI generation."
  (:use [clojoban.tiles])
  (:import [java.awt Color image.BufferedImage]
           [javax.imageio ImageIO]))

;;; CONSTANTS
(def tile-size
  16)

(def tile-map
  {; Map
    \# (tiles "wall.png")
	  (char 32) (tiles "floor.png")
	  
	  ; Entities
	  \U (tiles "player-up.png")
	  \D (tiles "player-down.png")
	  \L (tiles "player-left.png")
	  \R (tiles "player-right.png")
	  \x (tiles "box.png")
	  \@ (tiles "goal.png")})

;;; PRIVATES
(defn- gen-canvas [map entities]
  (let [width (* tile-size (count map))
        height (* tile-size (count [map 0]))
        bi (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        g (.createGraphics bi)]
    (do
      (.setColor g (Color/BLACK))
      (.fillRect g 0 0 width height)
      (for [y (count map)
            x (count (map y))
            :let [y-img (* tile-size y)
                  x-img (* tile-size x)]]
        (do
          (.drawImage (tile-map (get-in map y x)) x-img y-img nil)
          (.drawImage (tile-map (get-in entities y x)) x-img y-img nil)))
      {:width width :heigh height :image bi})))
    
(defn- gen-hud-image [num steps canvas]
  )
    

;;; PUBLICS
(defn generate-image [{num :num, steps :steps, map :map, entities :entities}]
  (fn [ostream]
		(ImageIO/write ((gen-canvas map entities) :image) "png" ostream)))