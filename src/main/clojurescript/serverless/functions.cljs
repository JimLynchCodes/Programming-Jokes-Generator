(ns serverless.functions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [schema.core :as s]
            [cljs.nodejs :as nodejs]
            [cljs-http.client :as http]
            [cljs.core.async :refer [put! chan <!]]
            [spec-tools.data-spec :as data]
            [orchestra.spec.test :as st]
            [orchestra.core :refer [defn-spec]]))

(nodejs/enable-util-print!)
(defonce moment (nodejs/require "moment"))
(set! js/XMLHttpRequest (nodejs/require "xhr2"))

(s/set-fn-validation! true)

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
(s/defschema bad-map {:foobar s/String})
(s/defschema Joke {:category s/String
                   :type     s/String
                   :joke     s/String
                   :id       s/Int})

(s/defn ^:always-validate jokes [event :- ANY
                                 ctx   :- ANY
                                 cb    :- ANY] :- nil
  (go (let [response (<! (http/get "https://sv443.net/jokeapi/category/Programming"))

            ; Would like to specify this as joke :- Joke
            joke  (->> response
                       (:body)
                       (clj->js)
                       (.stringify js/JSON))]

        (js/console.log event)
        (js/console.log ctx)

        (cb nil (clj->js
                 {:statusCode 200
                  :headers    {"Content-Type" "application/json"}
                  :body       joke})))))

(s/def ::Nil
  (data/spec ::Nil
             nil))

(defn-spec handler ::Nil [event map? ctx map? cb any?]
  'nil)

(defn-spec jokes3 ::JokeResponse [response map?]
  {:statusCode 200
   :headers    {"Content-Type" "application/json"}
   :body       (->> response :body)})

(st/instrument)

(set! (.-exports js/module) #js
                             {:healthCheck healthCheck
                              :now now
                              :jokes jokes})