(ns nubank.shortest-path
  (:require [nubank.helper :as helper]))

(def ^:private inf Double/POSITIVE_INFINITY)


; ---- Main functions ---- ;

(defn update-costs [g costs unvisited curr]
  (let [curr-cost (get costs curr)]
    (reduce
      (fn [c nbr]
        (if (unvisited nbr)
          (update-in c [nbr] min (+ curr-cost 1))
          c))
      costs
      (get g curr))))


(defn ^:private dijkstra [g src]
   (loop [costs (assoc (helper/set-all-map-values g inf) src 0)
          curr src
          unvisited (disj (apply hash-set (keys g)) src)]
     (cond
       (or (empty? unvisited) (= inf (get costs curr)))
       costs

       :else
       (let [next-costs (update-costs g costs unvisited curr)
             next-node (apply min-key next-costs unvisited)]
         (recur next-costs next-node (disj unvisited next-node))))))

(defn ^:private get-farness-map [graph]
  (reduce
    #(merge-with + (dijkstra graph %2) %1)
    (zipmap (keys graph) (repeat 0))
    (keys graph)))


; ---- Interface ---- ;
(defn centrality-map [graph]
  (helper/sort-map-by-value
    (reduce
      (fn [acc [key value]]
        (assoc acc key (/ 1 value)))
      {}
      (get-farness-map
        graph))))

(def shortest-path dijkstra)