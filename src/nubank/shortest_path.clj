(ns nubank.shortest-path
  (:require [nubank.graph :as social-network]))

(def ^:private inf Double/POSITIVE_INFINITY)

(defn ^:private update-costs
  [g costs unvisited curr]
  (let [curr-cost (get costs curr)]
    (reduce
      (fn [c nbr]
        (if (unvisited nbr)
          (update-in c [nbr] min (+ curr-cost 1))
          c))
      costs
      (get g curr))))


(defn ^:private dijkstra
  ([g src]
   (dijkstra g src nil))
  ([g src dst]
   (loop [costs (assoc (zipmap (keys g) (repeat inf)) src 0)
          curr src
          unvisited (disj (apply hash-set (keys g)) src)]
     (cond
       (= curr dst)
       (select-keys costs [dst])

       (or (empty? unvisited) (= inf (get costs curr)))
       costs

       :else
       (let [next-costs (update-costs g costs unvisited curr)
             next-node (apply min-key next-costs unvisited)]
         (recur next-costs next-node (disj unvisited next-node)))))))

(defn ^:private get-farness-map [graph]
  (reduce
    #(merge-with + (dijkstra graph %2) %1)
    (zipmap (keys graph) (repeat 0))
    (keys graph)))

(defn ^:private sort-map-by-value [map]
  (into (sorted-map-by (fn [key1 key2]
                         (compare
                           [(get map key1)  key1]
                           [(get map key2) key2])))
        map))

; interface
(defn closeness-map []
  (sort-map-by-value
    (reduce
      (fn [acc [key value]]
        (assoc acc key (/ 1 value)))
      {}
      (get-farness-map
        (social-network/get-graph)))))

(def shortes-path dijkstra)

(println (closeness-map))