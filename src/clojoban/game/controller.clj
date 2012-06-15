(ns clojoban.game.controller
  "Game actions controller. Implements the game's logic."
  (:use [clojoban.game.model :only [levels]]))

(def directions
  {:player-up [0 -1]
   :player-down [0 1]
   :player-left [-1 0]
   :player-right [1 0]})

(defn- push-box [from {:keys [number layout boxes goals] :as level} direction]
  (let [to (map + from direction)
        to-tile (get-in layout (reverse to))
        pushed (assoc level :boxes (replace {from to} boxes))]
    (if (and (not= to-tile :wall)
             (not-any? #(= to %) boxes))
      (if (every? #(some (partial = %) goals)
                  (replace {from to} boxes))
        (levels (inc number))
        (if (= to-tile :ice)
          (push-box to pushed direction)
          pushed))
      level)))

(defn- move-player [{:keys [number layout player boxes] :as level} direction]
    (let [to (map + player direction)
          to-tile (get-in layout (reverse to))]
      (if (not= to-tile :wall)
        (if (some #(= to %) boxes)
          (let [pushed-level (push-box to level direction)]
            (if (and
                  (not= level pushed-level)
                  (= number (pushed-level :number)))
              (move-player pushed-level direction)
              pushed-level))
          (into level
                (if (= to-tile :ice)
                  (move-player (assoc level :player to) direction)
                  {:player to})))
        level)))

(defn- action-move [{:keys [steps total-steps level] :as session} direction]
  (when-let [new-level (move-player level (directions direction))]
    (cond
      (= level new-level)
        {:steps steps
         :total-steps total-steps
         :level level
         :last-direction direction}
      (= (level :number) (new-level :number))
        {:steps (inc steps)
         :total-steps (inc total-steps)
         :level new-level
         :last-direction direction}
      :else
        {:steps 0
         :total-steps (inc total-steps)
         :level new-level
         :last-direction direction})))

(defn- action-restart [{:keys [total-steps level] :as session}]
  {:steps 0
   :total-steps total-steps
   :level (levels (level :number))
   :last-direction :player-down})

(defn- action-new [_]
  {:steps 0
   :total-steps 0
   :level (levels 0)
   :last-direction :player-down})

(defn- wrap-new [fun]
  #(if % (fun %) action-new))

(defn- wrap-end [fun]
  #(if (not= (% :level) :end)
     (fun %)
     %))

(def game-controller
  #^{:doc "Map of 'actions' to functions for the game."}
  {"_clojoban_up_" (wrap-end (wrap-new #(action-move % :player-up)))
   "_clojoban_down_" (wrap-end (wrap-new #(action-move % :player-down)))
   "_clojoban_left_" (wrap-end (wrap-new #(action-move % :player-left)))
   "_clojoban_right_" (wrap-end (wrap-new #(action-move % :player-right)))
   "_clojoban_restart_" (wrap-end (wrap-new action-restart))
   "_clojoban_new_" action-new})