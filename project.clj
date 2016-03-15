(defproject nubank "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler nubank.server/handler}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [liberator "0.13"]
                 [compojure "1.3.4"]
                 [ring/ring-core "1.4.0"]])
