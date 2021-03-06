(ns maze.generate)

(defn- create-grid
  [width height initial-value]
  (->> (repeat initial-value)
       (partition width)
       (take height)
       (map vec)
       vec))

(defn- neighbours-of
  [[x y]]
  [[(- x 2) y]
   [(+ x 2) y]
   [x (- y 2)]
   [x (+ y 2)]])

(defn- position-inside-grid?
  [[x y] grid]
  (and (< -1 y (count grid))
       (< -1 x (count (nth grid y)))))

(defn wall?
  [[x y] grid]
  (= (get-in grid [y x])
     :wall))

(defn- first-valid-neighbour
  [point grid]
  (->> (neighbours-of point)
       (filter #(position-inside-grid? % grid))
       (filter #(wall? % grid))
       shuffle
       first))

(defn- set-floor
  [point grid]
  (update-in grid (reverse point) :floor))

(defn- remove-midpoint
  [grid [x y] [x' y']]
  (set-floor [(/ (+ x x') 2)
              (/ (+ y y') 2)]
             grid))

(defn- evolve
  [grid [x y]]
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

(defn generate-maze
  "Generate a maze, a 2D array, with the given wicth & height.
  Width and height must be odd numbers. Start is an optional [x y] pair, and will default to [1 1]"
  ([size] (generate-maze size [1 1]))
  ([[width height] center]
     {:pre [(odd? width)
            (odd? height)]}
     (-> (create-grid width height :wall)
         (evolve center))))
