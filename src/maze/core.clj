(ns maze.core
  (:require [clojure.edn :as edn]
            [maze.display :as display]
            [maze.generate :as generate]))

(defn -main
  ([] (println "USAGE: <width> <height>"))
  
  ([width-string height-string]
     (let [width (edn/read-string width-string)
           height (edn/read-string height-string)]
       (-> (generate/generate-maze [width height])
           display/render))))
