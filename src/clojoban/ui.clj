(ns clojoban.ui
  "Game UI generation."
  (:use [clojoban.tiles])
  (:import [java.awt Color image.BufferedImage]
           [javax.imageio ImageIO]))

(defn- draw-hud [canvas-width canvas-height]
  )

(defn- draw-game [width height]
  )

;;; PUBLICS

(defn generate-image [{num :num, steps :steps, map :map, entities :entities}]
  (fn [ostream]
		(let [width 400
		      height 300
		      bi (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
		      g (.createGraphics bi)]
		  (do
		    (.setColor g (Color/RED))
		    (.fillRect g 0 0 width height)
		    (.setColor g (Color/BLACK))
		    (.drawString g
		      (str "LEVEL: " num " - STEPS: " steps)
		      0 10)
		    (ImageIO/write bi "png" ostream)))))