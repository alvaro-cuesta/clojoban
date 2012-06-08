(ns clojoban.images
  "Image management."
  (:use [clojoban.utils :only [load-dir]] )
  (:import [javax.imageio ImageIO]))

(defn- load-image [images file]
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
    (alter images load-dir dir load-image)))