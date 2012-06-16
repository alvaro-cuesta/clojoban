(ns clojoban.themes
  "Theme management."
  (:use [clojoban.utils :only [load-dir]]
        [clojure.java.io :only [file]])
  (:import [javax.imageio ImageIO]))

(defn- load-theme [themes folder]
  (let [name (.getName folder)
        theme (read-string (slurp (.getAbsolutePath (file folder "theme"))))
        tiles (theme :tiles)
        images (theme :images)]
    (conj themes
          {(keyword name)
           (into theme {:name name
                        :tiles (into {} (map (fn [[k v]] [k (ImageIO/read (file folder v))])
                                             tiles))
                        :images (into {} (map (fn [[k v]] [k (ImageIO/read (file folder v))])
                                              images))})})))

;;; PUBLICS

(def themes
  #^{:doc "Map of themes, indexed by parent folder."}
  (ref {}))

(defn add-themes
  "Given a dir, add themes into clojoban.themes/themes."
  [dir]
  (dosync
    (alter themes load-dir dir load-theme)))