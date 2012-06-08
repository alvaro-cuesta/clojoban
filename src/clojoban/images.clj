(ns clojoban.images
  "Image management."
  (:use [clojure.java.io :only [file]])
  (:import [javax.imageio ImageIO]))

(defn- load-image [file images]
  (conj images
        {(.getName file) (ImageIO/read file)}))

;;; PUBLICS

(def images
  #^{:doc "Map of images, indexed by filename."}
  (ref {}))

(defn add-images
  "Given a dir, add images into clojoban.images/images."
  [dir]
  (dosync
    (alter images clojoban.utils/load-dir dir load-image images)))