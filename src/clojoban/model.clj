(ns clojoban.model
  "Level management. Generates and holds the game model."
  (:use [flyweight.utils :only [load-dir map2d]]))

(defn- parse-layout [layout]
  (map2d
    #(condp = %
       \# :wall
       \. :ice
       :floor)
    layout))

(defn- load-level [levels file]
  (let [number (Integer. (.getName file))
        level (read-string (slurp file))
        layout (level :layout)]
    (conj levels
          {number (into level {:number number
                               :layout (parse-layout layout)
                               :width (apply max (map count layout))
                               :height (count layout)
                               :steps 0})})))

;;; PUBLICS

(defonce levels
  #^{:doc "Map of levels, indexed by filename (should be an integer.)"}
  (ref {}))

(defn add-levels
  "Given a dir, add levels into clojoban.levels/levels."
  [dir]
  (dosync
    (alter levels load-dir dir load-level)
    (alter levels
           #(conj %
                  {(inc (apply max (keys %)))
                   :end}))))