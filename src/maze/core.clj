(ns maze.core
  (:require [clojure.string :refer [join]]))

;;; Display

(defn create-grid
  [width height initial-value]
  (->> (repeat initial-value)
       (partition width)
       (take height)
       (map vec)
       vec))

(defn printable-char
  [value]
  (get {:wall \X}
       value
       \space))

(defn printable-row
  [row]
  (str (join (map printable-char row))
       \newline))

(defn printable-grid
  [grid]
  (join (map printable-row grid)))

(defn render
  [grid]
  (print (printable-grid grid)))

;;; Neighbours

(defn neighbours-of
  [[x y]]
  [[(- x 2) y]
   [(+ x 2) y]
   [x (- y 2)]
   [x (+ y 2)]])

(defn valid?
  [[x y] grid]
  (and (< -1 y (count grid))
       (< -1 x (count (nth grid y)))))

(defn wall?
  [[x y] grid]
  (= (get-in grid [y x])
     :wall))

(defn first-valid-neighbour
  [point grid]
  (->> (neighbours-of point)
       (filter #(valid? % grid))
       (filter #(wall? % grid))
       shuffle
       first))

;;; Evolve.

(defn set-floor
  [point grid]
  (update-in grid (reverse point) :floor))

(defn remove-midpoint
  [grid [x y] [x' y']]
  (set-floor [(/ (+ x x') 2)
              (/ (+ y y') 2)]
             grid))

(defn evolve
  [grid x y]
  (loop [grid' grid
         point [x y]
         backtracks []]
    (let [grid'' (set-floor point grid')
          neighbour (first-valid-neighbour point grid'')]
      (cond
       neighbour (recur (remove-midpoint grid'' point neighbour)
                        neighbour
                        (cons point backtracks))
       (first backtracks) (recur grid''
                                 (first backtracks)
                                 (rest backtracks))
       :else grid''))))

;;; Run.

(defn render-grid
  [width height]
  {:pre [(odd? width)
         (odd? height)]}
  (let [grid (create-grid width height :wall)]
    (render (evolve grid 1 1))))

(render-grid 51 31)
