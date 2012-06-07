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
	  \@ (tiles "goal.png")
    \- (tiles "empty.png")})

;;; PRIVATES
(defn- gen-canvas [map entities]
  (let [width (* tile-size (count (first map)))
        height (* tile-size (count map))
        bi (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        g (.createGraphics bi)]
    (do
      (doseq [y (range (count map))
              x (range (count (first map)))
              :let [x-img (* tile-size x)
                    y-img (* tile-size y)]]
          (do
            (println (get-in map [y x]))
            (.drawImage g (tile-map (get-in map [y x])) x-img y-img nil)
            (.drawImage g (tile-map (get-in entities [y x])) x-img y-img nil)))
      {:width width :heigh height :image bi})))
    
(defn- gen-hud-image [num steps canvas]
  )
    

;;; PUBLICS
(defn generate-image [{num :num, steps :steps, map :map, entities :entities}]
  (fn [ostream]
		(ImageIO/write ((gen-canvas map entities) :image) "png" ostream)))