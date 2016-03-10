(ns nubank.core)

(def graph '([1 2] [1 3] [2 6] [6 7] [3 4] [4 5] [2 5] [5 7]))

(defn transitions
  [graph node]
  (map #(if (= node (first %))
         %
         (reverse %))
       (filter #(or (= node (first %)) (= node (second %))) graph)))

(defn breadth-first
  [graph start]
  (let [walk
        (fn walk [past-nodes trans]
          (let [next-trans (drop-while #(past-nodes (second %)) trans)]
            (when-let [next-node (second (first next-trans))]
              (lazy-seq
                (cons (first next-trans)
                      (walk (conj past-nodes next-node)
                            (concat next-trans (transitions graph next-node))))))))]
    (walk #{start} (transitions graph start))))

(println (breadth-first graph 4))