(ns clojoban.levels
  "Level management."
  (:use [clojoban.utils :only [load-dir map2d]]))

(defn- parse-layout [layout]
  (map2d
    #(condp = %
       \# :wall
       \@ :goal
       :floor)
    layout))

(defn- load-level [levels file]
  (let [num (Integer. (.getName file))
        level (read-string (slurp (.getAbsolutePath file)))
        layout (level :layout)]
    (conj levels
          {num (into level {:layout (parse-layout layout)
                            :width (apply max (map count layout))
                            :height (count layout)})})))

;;; PUBLICS

(def levels
  #^{:doc "Map of levels, indexed by filename (should be an integer.)"}
  (ref {}))

(defn add-levels
  "Given a dir, add levels into clojoban.levels/levels."
  [dir]
  (dosync
    (alter levels load-dir dir load-level)))