(ns clojoban.game.controller
  "Game actions controller. Implements the game's logic."
  (:use [clojoban.game.model :only [levels]]))

(def directions
  {:player-up [0 -1]
   :player-down [0 1]
   :player-left [-1 0]
   :player-right [1 0]})

(defn- move [{:keys [layout player boxes goals]} direction]
  (let [moving-to (map + player direction)
        moving-two-tiles (map + moving-to direction)]
    (when (not= (get-in layout (reverse moving-to)) :wall)
        (if (some #(= moving-to %) boxes)
          (when (and (not= (get-in layout (reverse moving-two-tiles)) :Wall)
                     (not-any? #(= moving-two-tiles %) boxes))
            {:player moving-to
             :boxes (replace {moving-to moving-two-tiles} boxes)
             :goals goals})
          {:player moving-to
           :boxes boxes}))))

(defn- action-move [{:keys [steps level] :as session} direction]
  (into session
        {:steps (inc steps)
         :level (into level (move level (directions direction)))
         :last-direction direction}))

(defn- action-restart [{:keys [level] :as session}]
  (into session
        {:level (levels (level :number))
         :last-direction :player-down}))

(defn- action-new [_]
  {:steps 0
   :level (levels 0)
   :last-direction :player-down})

(defn- wrapper [fun]
  #(if % (fun %) action-new))

(def game-controller
  #^{:doc "Map of 'actions' to functions for the game."}
  {"up" (wrapper #(action-move % :player-up))
   "down" (wrapper #(action-move % :player-down))
   "left" (wrapper #(action-move % :player-left))
   "right" (wrapper #(action-move % :player-right))
   "restart" (wrapper action-restart)
   "new" action-new})