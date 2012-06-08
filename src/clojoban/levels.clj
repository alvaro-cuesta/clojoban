(ns clojoban.levels
  "Level management."
  (:use [clojoban.utils :only [load-dir]]))

(defn- parse-level [fn data]
  (vec (for [line data]
         (vec (for [element (seq line)]
                (fn element))))))

(defn- instance-map [{data :data}]
  (parse-level
    #(condp = %
       \# :wall
       :floor)
    data))

(defn- instance-entities [{data :data}]
  (parse-level
    #(condp = %
       \P :player-down
       \x :box
       \@ :goal
       :empty)
    data))

(defn- load-level [levels file]
  (let [num (Integer. (.getName file))
        level (read-string (slurp (.getAbsolutePath file)))
        data (level :data)]
    (conj levels
          {num
           {:name (level :name)
            :map (instance-map level)
            :entities (instance-entities level)}})))

;;; PUBLICS

(def levels
  #^{:doc "Map of levels, indexed by filename (should be an integer.)"}
  (ref {}))

(defn add-levels
  "Given a dir, add levels into clojoban.levels/levels."
  [dir]
  (dosync
    (alter levels load-dir dir load-level)))