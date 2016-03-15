(ns nubank.server
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [nubank.shortest-path :as shortest-path]
            [nubank.graph :as graph]
            [nubank.score :as score]))

; ---- Helpers ---- ;

;; convert the body to a reader. Useful for testing in the repl
;; where setting the body to a string is much simpler.
(defn body-as-string [ctx]
  (if-let [body (get-in ctx [:request :body])]
    (condp instance? body
      java.lang.String body
      (slurp (io/reader body)))))

;; Parse the body as json and store in the context
;; under the given key.
(defn is-get [request]
  (#{:get} (get-in request [:request :request-method])))

(defn parse-json [ctx key]
  (when (#{:post} (get-in ctx [:request :request-method]))
    (try
      (if-let [body (body-as-string ctx)]
        (let [data (json/read-str body)]
          [false {key data}])
        {:message "No body"})
      (catch Exception e
        (.printStackTrace e)
        {:message (format "IOException: %s" (.getMessage e))}))))

;; Check if the content type is json.
(defn check-content-type [ctx content-types]
  (if (#{:post} (get-in ctx [:request :request-method]))
    (or
      (some #{(get-in ctx [:request :headers "content-type"])}
            content-types)
      [false {:message "Unsupported Content-Type"}])
    true))

(defn is-vertex [body]
  (and
    (vector? body)
    (= 2
       (count body)
       (count (filter integer? body)))
    (graph/has-key (first body))
    (graph/has-key (second body))))

(defn is-node [body]
  (and
    (integer? body)
    (graph/has-key body)))

; ---- Resources ---- ;
(def resource-defaults
  {:handle-not-found (fn [_] "Ops, an error as ocurred.")
   :available-media-types ["application/json"]
   :malformed? (fn [request] (parse-json request ::data))
   :known-content-type? (fn [request] (check-content-type request ["application/json"]))})

(defresource vertex-resource [post-func ok-func]
             resource-defaults
             :allowed-methods [:post :get]
             :processable? (fn [request] (or (is-get request) (is-vertex (::data request))))
             :post! (fn [request] (post-func (::data request)))
             :handle-ok (fn [_] (json/write-str (ok-func))))

(defresource fraudulent-resource [post-func]
             resource-defaults
             :allowed-methods [:post]
             :processable? (fn [request] (is-node (::data request)))
             :post! (fn [request] (post-func (::data request))))

(defresource centrality-resource [ok-func]
             resource-defaults
             :allowed-methods [:get]
             :handle-ok (fn [_] (json/write-str (ok-func))))


; ---- Routes ---- ;
(defroutes app
           (ANY "/vertex" [] (vertex-resource
                               (partial graph/add-node graph/nodes)
                               score/get-scores))

           (ANY "/fraudulent" [] (fraudulent-resource
                                   score/set-fraudulent-node))

           (ANY "/vertex-centrality" [] (centrality-resource
                                          (partial shortest-path/centrality-map (graph/get-graph)))))

; ---- Main handler ---- ;
(def handler
  (-> app
      wrap-params))