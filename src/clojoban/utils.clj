(ns clojoban.utils
  "Utiliy functions."
  (:use [clojure.java.io :only [file]]
        [clojure.string :only [split]]))

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

(defn split-query [url]
  "Split query args from URL."
  (let [split-url (split url #"\?")]
    (if (= 2 (count split-url))
      (->> (split (split-url 1) #"&") 
        (map #(split % #"=")) 
        (map (fn [[k v]] [(keyword k) (if (nil? v) true v)])) 
        (into {}))
      {})))