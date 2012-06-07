(ns clojoban.levels
  "Level management.")

(defn- instance-map [{data :data}]
  (vec
    (for [y data]
      (vec
        (for [x (seq y)]
          (condp = x
            \# \#
            (char 32)))))))

(defn- instance-entities [{data :data}]
  (vec
    (for [y data]
      (vec
        (for [x (seq y)]
          (condp = x
            \P \D
            \x \x
            \@ \@
            \-))))))

(defn- load-level [levels file]
  (let [num (Integer. (.getName file))
        level (read-string (slurp (.getAbsolutePath file)))
        data (level :data)]
    (conj levels
          {num
           {:name (level :name)
            :map (instance-map level)
            :entities (instance-entities level)}})))

(defn- load-levels [levels dir]
  (dosync
    (reduce load-level levels
            (.listFiles (java.io.File. dir)))))

;;; PUBLICS

(def levels
  #^{:doc "Map of levels, indexed by filename (should be an integer.)"}
  (ref {}))

(defn add-levels
  "Given a dir, loads levels into clojoban.levels/levels"
  [dir]
  (dosync
    (alter levels load-levels dir)))