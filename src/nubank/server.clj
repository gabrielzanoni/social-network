(ns nubank.server
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [nubank.shortest-path :as shortest-path]
            [nubank.graph :as graph]
            [clojure.data.json :as json]
            [clojure.java.io :as io]))

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
  (when (#{:put :post} (get-in ctx [:request :request-method]))
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
  (if (#{:put :post} (get-in ctx [:request :request-method]))
    (or
      (some #{(get-in ctx [:request :headers "content-type"])}
            content-types)
      [false {:message "Unsupported Content-Type"}])
    true))

(defn is-vertex [body] (and
                         (vector? body)
                         (= 2 (count body))
                         (= 0 (count (filter (complement integer?) body)))))

; ---- Resources ---- ;
(def resource-defaults
  {:handle-not-found (fn [_] "Ops, an error as ocurred.")
   :available-media-types ["application/json"]})

(defresource vertex-resource [post-func ok-func]
             resource-defaults
             :allowed-methods [:post :get]
             :malformed? (fn [request] (parse-json request ::data))
             :known-content-type? (fn [request] (check-content-type request ["application/json"]))
             :processable? (fn [request] (or (is-get request) (is-vertex (::data request))))
             :post! (fn [request] (post-func (::data request)))
             :handle-ok (fn [_] (json/write-str (ok-func))))


; ---- Routes ----;
(defroutes app
           (ANY "/vertex" [] (vertex-resource
                               graph/add-vertex
                               shortest-path/closeness-map)))


; ---- Main handler ----;
(def handler
  (-> app
      wrap-params))