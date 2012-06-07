(ns clojoban.tiles
  "Tile management."
  (:use [clojure.java.io :only [file]])
  (:import [javax.imageio ImageIO]))

(defn- load-image [path]
  (ImageIO/read (file path)))

(defn- load-tiles
  [tiles dir]
  (dosync
    (reduce load-image tiles
            (.listFiles (file dir)))))

;;; PUBLICS

(def tiles (ref {}))

(defn add-tiles
  [dir]
  (dosync
    (alter tiles load-tiles dir)))