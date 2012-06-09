(ns clojoban.game-controller
  "Game actions controller. Implements the game's logic."
  (:use [clojoban.levels :only [levels]]))

(def directions
  {:player-up [0 -1]
   :player-down [0 1]
   :player-left [-1 0]
   :player-right [1 0]})
  

(defn- action-move [{:keys [level] :as session} direction]
  (let [player (level :player)]
    (into session
          {:level (into level
                        {:player (map + player (directions direction))})
           :last-direction direction})))

(defn- action-restart [{:keys [level] :as session}]
  (into session
        {:level (levels (level :number))
         :last-direction :player-down}))

(defn- action-new [_]
  {:steps 0
   :level (levels 0)
   :last-direction :player-down})

(defn- wrapper [session fun]
  #(if session fun action-new))

(def game-controller
  #^{:doc "Map of 'actions' to functions for the game."}
  {"up" (wrapper #(action-move % :player-up))
   "down" (wrapper #(action-move % :player-down))
   "left" (wrapper #(action-move % :player-left))
   "right" (wrapper #(action-move % :player-right))
   "restart" (wrapper action-restart)
   "new" action-new})