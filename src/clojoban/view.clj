(ns clojoban.view
  "Game UI generation (the game view.)"
  (:use [flyweight.themes :only [themes]]
        [flyweight ui])
  (:import [java.awt Color Font FontMetrics]))

;;; PRIVATES
(defn- draw-game!
  [theme
   {:keys [width height layout player boxes goals]}
   last-direction]
  (let [[tile-width tile-height] (theme :size)
        tiles (theme :tiles)
        images (theme :images)
        [player-x player-y] player]
    ; Draw layout
    (doseq [y (range height)
            x (range (count (layout y)))]
      (draw!
        (tiles (get-in layout [y x]))
        (* tile-width x)
        (* tile-height y)))
    ; Draw goals
    (doseq [goal goals
            :let [[goal-x goal-y] goal]]
      (draw!
        (if (some #(= goal %) boxes)
          (images :reached-goal)
          (images :goal))
        (* tile-width goal-x)
        (* tile-height goal-y)))
    ; Draw boxes
    (doseq [box boxes
            :let [[box-x box-y] box]]
      (draw!
        (images :box)
        (* tile-width box-x)
        (* tile-height box-y)))
    ; Draw player
    (draw!
      (images last-direction)
      (* tile-width player-x)
      (* tile-height player-y))))

(defn- game-image
  [theme
   steps
   total-steps
   {:keys [number name author width height] :as level}
   last-direction]
  (let [[tile-width tile-height] (theme :size)
        [scale-x scale-y] (theme :scale)
        dummy-graphics (:graphics (new-image 1 1))
        dummy-font (.getFont dummy-graphics)
        font-metrics (.getFontMetrics dummy-graphics dummy-font)
        font-height (+ 3 (.getHeight font-metrics))
        top-text (if author 
                   (format "%03d: %s (by %s)" number name author)
                   (format "%03d: %s" number name))
        top-width (.stringWidth font-metrics top-text)
        bottom-text (format "Steps: %d - Total: %d" steps total-steps)
        bottom-width (.stringWidth font-metrics bottom-text)
        game-width (* tile-width width scale-x)
        game-height (* tile-height height scale-y)
        img-width (max game-width top-width bottom-width)
        img-height (+ game-height (* 2 font-height) 3)
        image (new-image img-width img-height)]
    (with-image image
      (color! (Color/BLACK))
      (font! (.getFont *g*))
      (draw! top-text 0 font-height)
      (draw! bottom-text 0 (+ (* 2 font-height) game-height -3))
      (translate! 0 (+ 3 font-height))
      (scale! scale-x scale-y)
      (draw-game! theme level last-direction))))

(defn- end-image []
  (let [width 400
        height 300
        image (new-image width height)
        font (.getFont (:graphics image))
        font-metrics (.getFontMetrics (:graphics image) font)
        font-height (+ 3 (.getHeight font-metrics))]
    (with-image image
      (color! (Color/BLACK))
      (fill! 0 0 width height)
      (color! (Color/WHITE))
      (font! font)
      (draw! "Congratulations, you reached the end level!"
             4 font-height)
      (draw! "Actually, you might want to come back for new levels."
             4 (* 2 font-height))
      (draw! "This game is constantly evolving,"
             4 (* 3 font-height))
      (draw! "see the GitHub project on how to contribute."
             4 (* 4 font-height)))))

;;; PUBLICS
(defn generate-image [{:keys [theme steps total-steps level last-direction]}]
  "The game view image generation."
  (if (= level :end)
    (end-image)
    (game-image (themes theme) steps total-steps level last-direction)))