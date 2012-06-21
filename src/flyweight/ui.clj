(ns flyweight.ui
  "User-interface tools (images, drawing, etc.)"
  (:import [java.awt.image BufferedImage]))

;;; image system
(declare ^:dynamic *g*)

(defrecord Image
  [image graphics])

(defn new-image [width height]
  (let [image (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)]
    (Image. image (.createGraphics image))))

(defmacro with-image [image & body]
  `(binding [*g* (.graphics ~image)]
     (do ~@body
       (.image ~image))))

;;; graphics manipulation
(defmulti draw!
  (fn [what x y] (class what)))
(defmethod draw! java.awt.Image draw!-image
  [image x y]
  (.drawImage *g* image x y nil))
(defmethod draw! String draw!-string
  [string x y]
  (.drawString *g* string x y))

(defn color! [color]
  (.setColor *g* color))

(defn font! [font]
  (.setFont *g* font))

(defn scale! [x y]
  (.scale *g* x y))

(defn translate! [x y]
  (.translate *g* x y))

(defn fill! [x y w h]
  (.fillRect *g* x y w h))