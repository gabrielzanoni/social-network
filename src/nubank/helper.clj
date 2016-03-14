(ns nubank.helper)

(defn sort-map-by-value [map]
  (into
    (sorted-map-by
      (fn [key1 key2]
        (compare
          [(get map key2) key2]
          [(get map key1) key1])))
    map))

(defn update-values [m f & args]
  (reduce (fn [r [k v]]
            (assoc r k (apply f v args))) {} m))

(defn set-all-map-values [map value]
  (zipmap (keys map) (repeat value)))

(defn exp [x n]
  (. Math pow x n))
