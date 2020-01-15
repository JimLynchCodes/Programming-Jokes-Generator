(ns serverless.functions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.nodejs :as nodejs]
            [cljs-http.client :as http]
            [cljs.core.async :refer [put! chan <!]]))

(nodejs/enable-util-print!)
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

;  TODO - Add type annotations to describe what properties are 
;  available on the "event" and "ctx" here (and ability to 
;  ctrl +  to the implementation would be awesome!!)
(defn jokes [event ctx cb]
  (go (let [response (<! (http/get "https://sv443.net/jokeapi/category/Programming"))

            ;  TODO - Add type annotations to describe this object:
            ; {
            ;   "category": "Programming",
            ;   "type": "single",
            ;   "joke": "___",
            ;   "id": 0
            ;  }
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

(set! (.-exports js/module) #js
                             {:healthCheck healthCheck
                              :now now
                              :jokes jokes})