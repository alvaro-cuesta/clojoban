(ns clojoban.levels
  "Level management.")

(defn- parse-level [fn level]
  (vec (for [line data]
         (vec (for [element (seq line)]
                (fn element))))))

(defn- instance-map [{data :data}]
  (parse-level
    #(condp = %
       \# :wall
       :floor)))

(defn- instance-entities [{data :data}]
  (parse-level
    #(condp = %
       \P :player-down
       \x :box
       \@ :goal
       :empty)))

(defn- load-level [file levels]
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
    (alter levels clojoban.utils/load-dir dir load-level levels)))