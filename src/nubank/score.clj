(ns nubank.score
  (:require [nubank.graph :as graph])
  (:require [nubank.shortest-path :as shortest-path])
  (:require [nubank.helper :as helper]))


; ---- Initial values ---- ;

; Vertex with "0" value is a fraudulent costumer
(def fraudulent-map (ref
                     (zipmap
                       (graph/vertex @graph/nodes)
                       (repeat 1))))

(def initial-coefficient-f-map (zipmap
                                 (graph/vertex @graph/nodes)
                                 (repeat 1)))


; ---- Main functions ---- ;
(defn coefficient-f [shortest-path]
  (- 1
     (helper/exp
       (/ 1 2)
       shortest-path)))

(defn coefficient-f-map [initial-coefficient-f-map fraudulent-map]
  (reduce
    (fn [acc [key value]]
      (if (zero? value)
        (merge-with
          *
          acc
          (helper/update-values (shortest-path/shortest-path (graph/get-graph) key) coefficient-f))
        acc))
    initial-coefficient-f-map
    fraudulent-map))


; ---- Interface ---- ;
(defn get-scores []
  (helper/sort-map-by-value
    (merge-with
      *
      (coefficient-f-map initial-coefficient-f-map @fraudulent-map)
      (shortest-path/centrality-map (graph/get-graph)))))

(defn set-fraudulent-node [node]
  (dosync
    (alter fraudulent-map assoc node 0)))