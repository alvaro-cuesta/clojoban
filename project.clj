(defproject clojoban "0.0.1-SNAPSHOT"
  :description "A little Sokoban clone for \"Create a User-Profile Mini-Game\" @ http://codegolf.stackexchange.com"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "Same as Clojure"}
  
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.1.0"]]
  :plugins [[lein-ring "0.7.1"]]
  
  :main clojoban.core
  :ring {:handler clojoban.core/app
         :init clojoban.core/boot
         :port 1337
         :join? false}
  
  :repl-port 4001)