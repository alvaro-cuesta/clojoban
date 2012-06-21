(ns clojoban.controller
  "Game actions controller. Implements the game's logic."
  (:use [clojoban.model :only [levels]]))

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
            (if (and (not= level pushed-level)
                     (= number (pushed-level :number)))
              (recur pushed-level direction)
              pushed-level))
          (if (= to-tile :ice)
            (recur (assoc level :player to) direction)
            (assoc level :player to)))
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
         :last-direction :player-down})))

(defn- action-restart [{:keys [level] :as session}]
  {:steps 0
   :level (levels (level :number))
   :last-direction :player-down})

(defn- action-new [_]
  {:steps 0
   :total-steps 0
   :level (levels 1)
   :last-direction :player-down})

(defn- wrap-new [fun]
  #(if (empty? %)
     (action-new %)
     (fun %)))

(defn- wrap-end [fun]
  #(if (not= (% :level) :end)
     (fun %)
     %))

(defn- wrap-theme [fun theme]
  #(cond
     (not (nil? theme)) (assoc (fun %) :theme (keyword theme))
     (nil? (% :theme)) (assoc (fun %) :theme :default)
     :else (fun %)))

(defn game-controller [query]
  "The controller of the game. Reads query-map and acts accordingly"
  (if (query :_clojoban_new_)
    action-new
    (->
      (cond
        (query :_clojoban_up_) #(action-move % :player-up)
        (query :_clojoban_down_) #(action-move % :player-down)
        (query :_clojoban_left_) #(action-move % :player-left)
        (query :_clojoban_right_) #(action-move % :player-right)
        (query :_clojoban_restart_) action-restart
          :else identity)
      (wrap-end)
      (wrap-new)
      (wrap-theme (query :_clojoban_theme_)))))