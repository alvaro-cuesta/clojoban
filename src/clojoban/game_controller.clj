(ns clojoban.game-controller
  "Game actions controller. Implements the game's logic."
  (:use [clojoban.levels :only [levels]]))

(defn- action-up [session]
  )

(defn- action-down [session]
  )

(defn- action-left [session]
  )

(defn- action-right [session]
  )

(defn- action-restart [session]
  )

(defn- action-new [session]
  (let [level (levels 0)]
  {:num 0
   :steps 0
   :map (level :map)
   :entities (level :entities)}))

(def game-controller
  {"up" action-up
   "down" action-down
   "left" action-left
   "right" action-right
   "restart" action-restart
   "new" action-new})