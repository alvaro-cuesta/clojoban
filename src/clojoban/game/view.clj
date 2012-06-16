(ns clojoban.game.view
  "Game UI generation (the game view.)"
  (:use [clojoban.themes :only [themes]])
  (:import [java.awt Color Font FontMetrics image.BufferedImage]
           [javax.imageio ImageIO]))

;;; PRIVATES
(defn- draw-game [g theme {:keys [width height layout player boxes goals]} last-direction]
  (let [tile-width ((theme :size) 0)
        tile-height ((theme :size) 1)
        [player-x player-y] player]
    (doseq [y (range height) ; Draw layout
            x (range (count (layout y)))]
      (.drawImage g
        ((theme :tiles) (get-in layout [y x]))
        (* tile-width x)
        (* tile-height y)
        nil))
    (doseq [goal goals ; Draw goals
            :let [[goal-x goal-y] goal]]
      (.drawImage g
        ((theme :images) :goal)
        (* tile-width goal-x)
        (* tile-height goal-y)
        nil))
    (doseq [box boxes ; Draw boxes
            :let [[box-x box-y] box]]
      (.drawImage g
        ((theme :images) :box)
        (* tile-width box-x)
        (* tile-height box-y)
        nil))
    (.drawImage g ; Draw player
      ((theme :images) last-direction)
      (* tile-width player-x)
      (* tile-height player-y)
      nil)))

(defn- draw-ui
  [theme steps total-steps {:keys [number name author width height] :as level} last-direction]
  (let [tile-width ((theme :size) 0)
        tile-height ((theme :size) 1)
        [scale-x scale-y] (theme :scale)
        dummy-graphics (.createGraphics (BufferedImage. 1 1 BufferedImage/TYPE_INT_ARGB))
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
        image (BufferedImage. img-width img-height BufferedImage/TYPE_INT_ARGB)
        g (.createGraphics image)
        font (.getFont g)]
    (do
      (.setColor g (Color/BLACK))
      (.setFont g font)
      (.drawString g top-text 0 font-height)
      (.drawString g bottom-text 0 (+ (* 2 font-height) game-height -3))
      (.translate g 0 (+ 3 font-height))
      (.scale g scale-x scale-y)
      (draw-game g theme level last-direction)
      image)))

(defn- draw-end []
  (let [width 400
        height 300
        image (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        g (.createGraphics image)
        font (.getFont g)
        font-metrics (.getFontMetrics g font)
        font-height (+ 3 (.getHeight font-metrics))]
    (do
      (.setColor g (Color/BLACK))
      (.fillRect g 0 0 width height)
      (.setColor g (Color/WHITE))
      (.setFont g font)
      (.drawString g "Congratulations, you reached the end level!" 4 font-height)
      (.drawString g "Actually, you might want to come back for new levels." 4 (* 2 font-height))
      (.drawString g "This game is constantly evolving," 4 (* 3 font-height))
      (.drawString g "see the GitHub project on how to contribute." 4 (* 4 font-height))
      image)))

;;; PUBLICS
(defn generate-image [{:keys [theme steps total-steps level last-direction]}]
  "Generates the final UI of the game (level+HUD)"
  (fn [ostream]
    (ImageIO/write
      (if (= level :end)
        (draw-end)
        (draw-ui (themes theme) steps total-steps level last-direction))
      "png" ostream)))