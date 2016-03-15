(ns nubank.tests
  (:use [nubank.mock-data])
  (:require [clojure.test :refer :all]
            [nubank.graph :as graph]
            [nubank.shortest-path :as shortest-path]
            [nubank.score :as score]))


; ---- Graph ---- ;
(deftest graph-from-node-test
  (testing "[INFO] Testing 'get-graph-from-nodes' function..."
    (is (= graph-1 (graph/get-graph-from-nodes test-nodes-1)))
    (is (= graph-2 (graph/get-graph-from-nodes test-nodes-2)))))

(deftest transition-test
  (testing "[INFO] Testing 'transitions' function..."
    (doseq [index (range (count transitions-nodes-1))]
      (is (= (nth transitions-nodes-1 index) (graph/transitions graph-1 (+ 1 index)))))
    (doseq [index (range (count transitions-nodes-2))]
      (is (= (nth transitions-nodes-2 index) (graph/transitions graph-2 index))))))

(deftest add-node-test
  (testing "[INFO] Testing 'add-node' function..."
    (graph/add-node test-nodes-1-ref [11 7])
    (graph/add-node test-nodes-1-ref [11 2])
    (graph/add-node test-nodes-1-ref [10 7])
    (graph/add-node test-nodes-1-ref [10 3])
    (graph/add-node test-nodes-2-ref [11 7])
    (graph/add-node test-nodes-2-ref [11 2])
    (graph/add-node test-nodes-2-ref [10 7])
    (graph/add-node test-nodes-2-ref [10 3])
    (is (some #{[11 7]} @test-nodes-1-ref))
    (is (some #{[11 2]} @test-nodes-1-ref))
    (is (some #{[10 7]} @test-nodes-1-ref))
    (is (some #{[10 3]} @test-nodes-1-ref))
    (is (some #{[11 7]} @test-nodes-2-ref))
    (is (some #{[11 2]} @test-nodes-2-ref))
    (is (some #{[10 7]} @test-nodes-2-ref))
    (is (some #{[10 3]} @test-nodes-2-ref))))


; ---- Shortest Path ---- ;
(deftest centrality-map-test
  (testing "[INFO] Testing 'centrality-map' function..."
    (is (= centrality-map-1 (shortest-path/centrality-map graph-1)))
    (is (= centrality-map-2 (shortest-path/centrality-map graph-2)))))


; ---- Score ---- ;
(deftest coefficient-f-test
  (testing "[INFO] Testing 'coefficient-f-map' function..."
    (is (= coefficient-f-map-1 (score/coefficient-f-map initial-coefficient-f-map-1 fraudulent-map-1)))
    (is (= coefficient-f-map-2 (score/coefficient-f-map initial-coefficient-f-map-2 fraudulent-map-2)))))