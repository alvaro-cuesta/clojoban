(ns flyweight.theme
  "Theme management."
  (:use [flyweight.utils :only [load-dir]]
        [clojure.java.io :only [file]])
  (:import [javax.imageio ImageIO]))

(defn- load-theme [themes folder]
  (let [name (.getName folder)
        ;theme (read-string (slurp (.getAbsolutePath (file folder "theme"))))
        theme (read-string (slurp (file folder "theme")))
        tiles (theme :tiles)
        images (theme :images)]
    (conj themes
          {(keyword name)
           (into theme {:name name
                        :tiles (into {} (map (fn [[k v]]
                                               [k (ImageIO/read (file folder v))])
                                             tiles))
                        :images (into {} (map (fn [[k v]]
                                                [k (ImageIO/read (file folder v))])
                                              images))})})))

;;; PUBLICS

(def themes
  #^{:doc "Map of themes, indexed by parent folder."}
  (ref {}))

(defn add-themes
  "Given a dir, add themes into flyweight.theme/themes."
  [dir]
  (dosync
    (alter themes load-dir dir load-theme)))