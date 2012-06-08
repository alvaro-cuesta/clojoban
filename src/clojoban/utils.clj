(ns clojoban.utils
  "Utiliy functions."
  (:use [clojure.java.io :only [file]]))

(defn load-dir
  "Add a whole dir into coll using fn (a [coll file] function)"
  [coll dir fn]
  (dosync
    (reduce fn coll (.listFiles (file dir)))))

(defn map2d [fn data]
  "Like map, but for 2d data structures."
  (vec (for [line data]
         (vec (for [element (seq line)]
                (fn element))))))