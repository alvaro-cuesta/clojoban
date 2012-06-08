(ns clojoban.utils
  "Utiliy functions.")

(defn load-dir
  "Add a whole dir into coll using fn (a [file coll] function)"
  [dir fn coll]
  (dosync
    (reduce fn coll (.listFiles (file dir)))))