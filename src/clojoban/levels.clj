(ns clojoban.levels
  "Level management.")

(defn- instance-map [level]
  )

(defn- instance-entities [level]
  )

(defn- load-level [levels file]
  (let [num (Integer. (.getName file))
        level (read-string (slurp (.getAbsolutePath file)))
        data (level :data)]
    (conj levels
          {num
           {:name (level :name)
            :map (instance-map data)
            :entities (instance-entities data)}})))

(defn- load-levels [levels dir]
  (dosync
    (reduce load-level levels
            (.listFiles (java.io.File. dir)))))

;;; PUBLICS

(def levels (ref {}))

(defn add-levels
  "Given a dir, loads levels into clojoban.levels/levels"
  [dir]
  (dosync
    (alter levels load-levels dir)))