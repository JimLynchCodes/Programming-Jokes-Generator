(ns serverless.functions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [schema.core :as s]
            [cljs.nodejs :as nodejs]
            [cljs-http.client :as http]
            [cljs.core.async :refer [put! chan <!]]))

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

; Purposely incorrect schema
(def Joke {:category s/String
           :type     s/String
           :joke     s/String
           :id       s/Int
           :foobarrr s/Int
           :hmmmmm   s/String})

; Doesn't fail when it should
(s/defn unpack-joke [response] :- Joke
  (->> response
       (:body)))

(s/defn jokes [event
               ctx
               cb]

  (go (let [response (<! (http/get "https://sv443.net/jokeapi/category/Programming"))
            joke (unpack-joke response)]

        (js/console.log event)
        (js/console.log ctx)
        (js/console.log (str "jokin' " joke))

        (cb nil (clj->js
                 {:statusCode 200
                  :headers    {"Content-Type" "application/json"}
                  :body       (->> joke
                                   (clj->js)
                                   (.stringify js/JSON))})))))

(set! (.-exports js/module) #js
                             {:healthCheck healthCheck
                              :now now
                              :jokes jokes})