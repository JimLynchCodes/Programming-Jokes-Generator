(ns serverless.functions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [schema.core :as s]
            [clojure.spec.alpha :as spec]
            [cljs.nodejs :as nodejs]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [spec-tools.data-spec :as data]))

(nodejs/enable-util-print!)
(s/set-fn-validation! true)
(defonce moment (nodejs/require "moment"))
(set! js/XMLHttpRequest (nodejs/require "xhr2"))

(def JOKES_ENDPOINT "https://sv443.net/jokeapi/category/Programming")

(defn healthCheck [_event ctx cb]
  (println ctx)
  (cb nil (clj->js
           {:statusCode 200
            :headers    {"Content-Type" "text/html"}
            :body       "{\n\t\"message\": \"We're good!\"\n}"})))

(defn now [_event ctx cb]
  (println ctx)
  (cb nil (clj->js
           {:statusCode 200
            :headers    {"Content-Type" "text/html"}
            :body       (str "<h1>" (.format (moment.) "LLLL") "</h1>")})))

(s/defschema Joke {:id       s/Int
                   :category s/String
                   :type     s/String
                   :joke     s/String})

(s/defschema TwoPartJoke {:id       s/Int
                          :category s/String
                          :type     "single"
                          :joke     s/String
                          :setup     string?
                          :delivery     string?})

(spec/def ::Joke (data/spec ::Joke
                            {:id       int?
                             :category string?
                             :type     "twopart"
                             :joke     string?}))

(spec/def ::TwoPartJoke (data/spec ::Joke
                                   {:id       int?
                                    :category string?
                                    :type     "twopart"
                                    :setup     string?
                                    :delivery     string?}))

(spec/fdef jokes
  :args (spec/cat :event map? :ctx map? :cb any?)
  :ret nil)

(defn jokes [_event _ctx cb]
  (go (let [response (<! (http/get JOKES_ENDPOINT))
            joke  (->> response
                       (:body)
                       (clj->js)
                       (.stringify js/JSON))]
        (cb nil (clj->js
                 {:statusCode 200
                  :headers    {"Content-Type" "application/json"}
                  :body       joke})))))

(set! (.-exports js/module) #js
                             {:healthCheck healthCheck
                              :now now
                              :jokes jokes})