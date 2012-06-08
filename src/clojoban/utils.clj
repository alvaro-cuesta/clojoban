(ns clojoban.utils
  "Utiliy functions."
  (:use [clojure.java.io :only [file]]))

(defn load-dir
  "Add a whole dir into coll using fn (a [coll file] function)"
  [coll dir fn]
  (dosync
    (reduce fn coll (.listFiles (file dir)))))