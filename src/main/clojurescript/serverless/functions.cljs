(ns serverless.functions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [schema.core :as s]
            [clojure.spec.alpha :as spec]
            [cljs.nodejs :as nodejs]
            [cljs-http.client :as http]
            [cljs.core.async :refer [put! chan <!]]
            [spec-tools.data-spec :as data]))

(nodejs/enable-util-print!)
(s/set-fn-validation! true)
(defonce moment (nodejs/require "moment"))
(set! js/XMLHttpRequest (nodejs/require "xhr2"))

(defn healthCheck [event ctx cb]
  (println ctx)
  (cb nil (clj->js
           {:statusCode 200
            :headers    {"Content-Type" "text/html"}
            :body       "{\n\t\"message\": \"We're gud!\"\n}"})))


(defn now [event ctx cb]
  (println ctx)
  (cb nil (clj->js
           {:statusCode 200
            :headers    {"Content-Type" "text/html"}
            :body       (str "<h1>" (.format (moment.) "LLLL") "</h1>")})))
(s/defschema ANY s/Any)
(s/defschema Joke {:category s/String
                   :type     s/String
                   :joke     s/String
                   :id       s/Int})

(spec/def ::Joke (data/spec ::Joke
                         {:category string?
                            :type     string?
                            :joke     string?
                            :id       int?}))

; (def random-jokes (->> (spec/exercise ::Joke)
;                        (map first)))

(defn conforms? [spec x]
  (if (spec/valid? spec x)
    x
    (throw (ex-info (str "invalid value for spec " spec \newline (spec/explain-str spec x))
                    {:in x :spec spec}))))

(spec/fdef jokes
  :args (spec/cat :event map? :ctx map? :cb any?)
  :ret nil)

(defn jokes [event ctx cb]
  (go (let [response (<! (http/get "https://sv443.net/jokeapi/category/Programming"))

            ; Would like to specify this as joke :- Joke
            joke  (->> response
                       (:body)
                       (conforms? ::Joke)
                       (clj->js)
                       (.stringify js/JSON))]

        (js/console.log event)
        (js/console.log ctx)

        (cb nil (clj->js
                 {:statusCode 200
                  :headers    {"Content-Type" "application/json"}
                  :body       joke})))))

(set! (.-exports js/module) #js
                             {:healthCheck healthCheck
                              :now now
                              :jokes jokes})