(ns clojoban.game-controller
  "Game actions controller. Implements the game's logic."
  (:use [clojoban.levels :only [levels]]))

(defn- action-move [{:keys [map entities] :as session} direction]
  )

(defn- action-restart [session]
  )

(defn- action-new [_]
  {:num 0
   :steps 0
   :level (levels 0)
   :last-direction :player-down})

(def game-controller
  #^{:doc "Map of 'actions' to functions for the game."}
  {"up" #(action-move % [0 -1])
   "down" #(action-move % [0 1])
   "left" #(action-move % [-1 0])
   "right" #(action-move % [1 0])
   "restart" action-restart
   "new" action-new})