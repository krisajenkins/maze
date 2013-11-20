(ns maze.display
  (:require [clojure.string :refer [join]]))

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

