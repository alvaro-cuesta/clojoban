(ns flyweight.themes
  "Theme management."
  (:use [flyweight.utils :only [load-dir]]
        [clojure.java.io :only [file]])
  (:import [javax.imageio ImageIO]))

(defn- load-theme [themes folder]
  (let [name (.getName folder)
        theme (read-string (slurp (file folder "theme")))
        tiles (theme :tiles)
        images (theme :images)]
    (conj themes
          {(keyword name)
           (into theme
                 {:name name
                  :tiles (into {} (map (fn [[k v]]
                                         [k (ImageIO/read (file folder v))])
                                       tiles))
                  :images (into {} (map (fn [[k v]]
                                          [k (ImageIO/read (file folder v))])
                                        images))})})))

;;; PUBLICS

(defn add-themes
  "Given a dir, add themes into flyweight.theme/themes."
  [dir themes]
  (dosync
    (alter themes load-dir dir load-theme)))