(ns nubank.graph
  (:require [clojure.set :as set]))

(def nodes (ref '([64 48] [70 95] [80 25] [80 88] [37 15] [36 50] [13 57] [26 50] [77 53] [69 2] [69 12] [82 10] [95 75] [5 4] [90 55] [65 62] [5 35] [51 80] [36 39] [47 39] [18 12] [39 49] [2 90] [99 13] [25 17] [34 74] [54 7] [41 88] [80 4] [79 81] [53 85] [26 24] [21 98] [38 44] [88 3] [10 88] [29 47] [78 98] [9 73] [68 1] [41 6] [29 74] [67 17] [97 31] [83 79] [53 6] [49 45] [79 68] [0 29] [35 98] [51 40] [38 73] [52 34] [53 10] [18 99] [29 12] [37 75] [67 97] [93 58] [17 53] [38 54] [80 41] [49 20] [65 49] [78 72] [79 13] [83 33] [8 24] [77 29] [33 17] [96 64] [51 8] [25 55] [73 76] [98 26] [58 34] [83 13] [84 59] [65 41] [57 36] [46 41] [3 92] [4 65] [48 79] [35 29] [2 77] [90 46] [63 22] [72 38] [24 29] [64 61] [24 31] [87 76] [48 16] [58 48] [2 85] [15 76] [15 63] [25 99] [14 72] [9 3] [37 44] [44 82] [44 7] [88 66] [97 85] [16 31] [30 22] [40 5] [90 3] [88 60] [39 10] [49 99] [56 94] [48 24] [40 64] [94 77] [98 18] [28 26] [87 41] [91 32] [42 36] [10 75] [51 66] [69 17] [1 10] [3 15] [13 64] [52 99] [52 73] [97 57] [10 19] [4 98] [35 69] [53 17] [64 76] [44 1] [32 10] [0 69] [34 44] [93 15] [6 79] [10 71] [73 23] [26 70] [56 51] [59 75] [8 26] [29 64] [39 28] [76 4] [55 3] [8 21] [11 1] [41 29] [77 10] [23 28] [97 63] [35 99] [44 43] [5 65] [76 8] [47 62] [9 66] [45 9] [54 35] [59 26] [85 23] [9 44] [89 68] [93 8] [12 2] [13 60] [55 92] [84 68] [43 89] [45 86] [77 90] [11 80] [74 42] [42 24] [31 99] [4 58] [79 9] [43 16] [8 62] [86 90] [49 86] [99 96] [57 5] [82 81] [59 99] [97 55] [75 51] [42 86] [90 75] [63 25] [38 98] [75 53] [98 70] [55 44] [47 73] [40 78] [1 57] [48 62] [4 34] [27 45] [1 41] [82 56] [66 83] [38 76] [4 9] [16 28] [1 35] [60 81] [76 6] [24 90] [7 50] [14 3] [48 28] [67 0] [70 66] [10 14] [34 79] [20 65] [1 62] [49 1] [31 20] [68 8] [41 44] [86 84] [10 23] [33 57] [35 33] [1 58] [17 76] [2 69] [18 0] [12 56] [63 91] [47 42] [50 38] [42 62] [83 90] [69 93] [75 58] [33 5] [33 82] [95 82] [45 68] [74 81] [6 99] [52 75] [78 58] [91 15] [6 35] [81 59] [28 13] [20 51] [11 47] [4 88] [79 82] [33 90] [58 70] [32 18] [15 88] [4 15] [28 51] [9 92] [50 88] [57 60] [95 33] [74 78] [90 2] [52 10] [48 30] [51 57] [26 14] [8 40] [68 5] [33 37] [92 4] [26 89] [48 74] [36 51] [62 70] [7 86] [45 82] [47 44] [96 0] [52 89] [59 95] [62 82] [81 27] [78 86] [78 22] [55 4] [58 61] [17 37] [31 58] [5 90] [90 94] [80 57] [82 39] [15 35] [17 2] [76 92] [41 67] [48 20] [67 42] [73 97] [42 38] [18 66] [34 58] [4 46] [50 9] [62 97] [9 2] [88 22] [90 9] [92 29] [23 78] [41 95] [44 31] [77 60] [35 50] [6 16] [27 22] [3 20] [9 77] [97 47] [5 45] [46 27] [0 38] [20 35] [44 46] [42 33] [41 80] [4 22] [48 97] [44 14] [99 1] [79 16] [85 64] [81 8] [67 48] [75 52] [38 52] [9 8] [66 28] [99 5] [59 74] [53 63] [82 7] [74 63] [72 76] [13 52] [20 79] [24 1] [89 53] [49 35] [74 6] [76 63] [52 95] [13 37] [33 87] [98 95] [64 36] [55 59] [8 23] [64 10] [15 3] [34 88] [4 69] [2 1] [35 36] [12 73] [45 52] [87 55] [85 91] [80 42] [84 77] [50 34] [20 83] [55 29] [99 34] [26 38] [78 29] [71 82] [70 68] [69 19] [26 36] [93 96] [88 57] [97 91] [52 21] [72 46] [94 39] [33 74] [68 7] [79 38] [65 55] [66 89] [65 89] [46 82] [38 95] [58 83] [45 62] [13 31] [51 0] [45 48] [89 44] [5 84] [99 98] [40 23] [8 28] [21 68] [57 84] [95 29] [60 75] [23 97] [52 92] [25 18] [53 36] [41 20] [89 55] [58 37] [37 38] [65 43] [65 69] [98 73] [5 16] [50 54] [37 88] [43 86] [33 46] [62 64] [86 33] [2 58] [27 14] [22 50] [88 24] [16 65] [26 12] [96 72] [15 20] [46 84] [22 99] [37 78] [37 12] [81 5] [21 96] [93 75] [9 72] [35 21] [90 67] [1 44] [33 47] [57 24] [48 42] [42 27] [86 69] [56 5] [40 33] [67 78] [63 92] [88 20] [16 51] [28 98] [88 46] [89 56] [76 48] [20 89] [92 82] [3 51] [22 20] [39 47] [44 37] [52 66] [33 49] [78 31] [50 48] [75 31] [55 27] [65 37] [44 33] [69 33] [2 11] [86 28] [1 48] [37 20] [82 51] [89 70] [69 67] [58 84] [31 28] [31 95] [31 87] [93 79] [22 18] [91 9] [24 36] [74 9] [5 15] [72 82] [76 22] [20 9] [4 64] [23 17] [63 80] [15 52] [18 95] [94 89] [87 81] [54 88] [22 4] [79 18] [96 22] [50 93] [82 36] [86 54] [30 8] [35 91] [78 19] [72 75] [54 63] [58 2] [83 55] [47 88] [69 73] [2 72] [74 51] [62 35] [84 4] [3 26] [11 41] [59 76] [60 5] [6 89] [50 11] [33 98] [76 1] [39 20] [88 1] [12 20] [51 49] [33 0] [81 40] [22 63] [97 95] [55 15] [96 10] [14 73] [14 93] [47 86] [14 45] [34 18] [65 5] [28 92] [10 45] [3 46] [51 33] [29 1] [51 53] [26 43] [16 40] [44 97] [20 17] [4 76] [38 26] [78 93] [1 39] [85 6] [98 88] [13 44] [83 39] [92 43] [16 91] [92 33] [5 67] [73 33] [54 76] [32 92] [57 89] [5 50] [47 96] [28 27] [92 90] [39 54] [74 47] [46 39] [51 26] [0 5] [74 89] [18 62] [7 82] [62 98] [44 35] [62 55] [60 38] [20 31] [67 34] [62 45] [39 78] [56 84] [28 37] [19 75] [29 31] [94 73] [67 55] [59 88] [37 40] [10 18] [1 99] [33 32] [46 81] [49 87] [54 77] [10 74] [11 72] [44 18] [83 58] [23 13] [54 15] [5 3] [62 54] [70 86] [60 85] [46 37] [18 64] [37 0] [46 94] [40 26] [74 52] [0 15] [7 71] [54 33] [13 27] [35 41] [51 23] [62 25] [98 11] [2 20] [98 78] [51 76] [71 24] [69 92] [93 7] [60 25] [59 51] [73 77] [88 18] [54 32] [56 52] [38 48] [66 59] [82 28] [60 35] [22 88] [57 72] [57 15] [73 29] [45 59] [43 67] [19 3] [92 53] [23 63] [86 37] [2 74] [31 23] [67 63] [44 2] [53 42] [67 22] [89 8] [37 45] [89 88] [4 31] [74 12] [81 49] [12 89] [91 99] [73 22] [31 56] [82 34] [98 91] [63 95] [84 55] [8 84] [84 54] [71 63] [73 11] [42 81] [62 91] [35 61] [91 85] [84 44] [29 93] [27 60] [41 9] [17 46] [90 97] [17 41] [89 62] [78 77] [35 37] [71 62] [40 88] [98 9] [27 21] [98 68] [56 96] [89 79] [27 98] [0 8] [32 69] [63 27] [29 71] [66 7] [44 30] [88 30] [45 87] [48 51] [35 92] [85 12] [9 45] [90 84] [88 69] [86 94] [88 40] [41 35] [12 9] [61 22] [72 78] [55 81] [20 97] [66 58] [73 88] [8 69] [68 65] [93 54] [90 89] [93 67] [36 64] [36 74] [12 62] [22 96] [39 90] [95 54] [68 79] [0 47] [65 70] [79 57] [75 74] [97 42] [28 14] [69 41] [50 28] [17 74] [44 16] [47 67] [58 98] [70 52] [36 24] [95 53] [3 66] [17 47] [97 17] [92 38] [53 92] [94 65] [69 34] [74 90] [99 62] [66 50] [9 23] [16 49] [49 51] [32 68] [31 93] [85 56] [91 54] [70 45] [43 23] [55 86] [95 3] [47 82] [75 44] [54 41] [3 12] [5 13] [88 61] [85 0] [82 87] [60 32] [12 52] [64 65] [31 75] [3 74] [73 55] [30 11] [8 78] [94 27] [61 69] [28 17] [50 98] [0 26] [81 83] [34 12] [91 83] [87 49] [18 24] [68 93] [81 66] [87 26] [61 9] [22 57] [28 33] [36 73] [74 49] [39 44] [73 60] [51 5] [76 66] [24 47] [45 93] [75 14] [18 56] [66 57] [95 9] [86 40] [76 67] [7 84] [6 65] [15 39] [99 7] [88 11] [23 65] [32 83] [86 57] [21 90] [66 13] [54 44] [77 50] [64 13] [11 2] [75 73] [43 65] [77 5] [91 47] [57 16] [20 74] [92 78] [27 57] [82 99] [76 3] [14 99] [89 13] [9 4] [80 76] [89 54] [99 50] [15 26] [5 8] [46 70] [62 0] [20 67] [27 5] [65 27] [47 12] [99 59] [77 74] [11 14] [78 41] [16 53] [75 36] [7 79] [24 44] [13 15] [36 35] [53 97] [99 81] [12 69] [82 48] [36 16] [98 85] [28 1] [74 80] [27 51] [63 39] [26 85] [69 28] [76 95] [92 74] [72 53] [3 83] [20 6] [12 43] [49 32] [31 37] [30 35] [20 59] [44 69] [24 59] [14 33] [70 12] [61 47] [52 76] [49 93] [41 1] [30 73] [23 87] [44 4] [29 40] [29 30] [69 4] [68 30] [95 20] [55 97] [78 64] [27 65] [1 86] [93 65] [96 95] [57 93] [64 66] [13 43] [57 28] [92 73] [99 71] [19 44] [79 94] [42 58] [82 80] [59 23] [10 42] [1 77] [28 67] [58 51] [35 42] [20 32] [34 92] [63 46] [26 45] [23 57])))


; ---- Main functions ---- ;

(defn vertex [nodes]
  (keys
    (conj
      (into {} nodes)
      (set/map-invert (into {} nodes)))))

(defn transitions [graph node]
  (into
    #{}
    (map
      #(if (= node (first %))
        (second %)
        (first %))
      (filter #(or (= node (first %)) (= node (second %))) graph))))

(defn get-graph-from-nodes [nodes]
  (reduce
    (fn [r x]
      (conj r [x (transitions nodes x)]))
    {}
    (vertex nodes)))

(defn add-node [ref-nodes data]
  (when-not (or
              (some #{data} @ref-nodes)
              (some #{(into [] (reverse data))} @ref-nodes))
    (dosync (alter ref-nodes conj data))))


; ---- Interface ---- ;

(defn get-graph []
  (get-graph-from-nodes @nodes))

(defn has-key [key]
  (some #{key} (vertex @nodes)))