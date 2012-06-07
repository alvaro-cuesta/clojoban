(ns clojoban.tiles
  "Tile management."
  (:use [clojure.java.io :only [file]])
  (:import [javax.imageio ImageIO]))

(defn- load-image [tiles file]
  (conj tiles
        {(.getName file) (ImageIO/read file)}))

(defn- load-tiles
  [tiles dir]
  (dosync
    (reduce load-image tiles
            (.listFiles (file dir)))))

;;; PUBLICS

(def tiles
  #^{:doc "Map of tiles, indexed by filename."}
  (ref {}))

(defn add-tiles
  [dir]
  (dosync
    (alter tiles load-tiles dir)))