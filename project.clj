(defproject copy-as-latex "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.89"]
                 [reagent "0.5.1"]]
  :source-paths ["src"]
  :profiles {:dev
             {:plugins [[com.cemerick/austin "0.1.6"]
                        [lein-cljsbuild "1.1.3"]
                        [lein-chromebuild "0.3.0"]]
              :cljsbuild
              {:builds
               {:main
                {:source-paths ["src"]
                 :compiler {:output-to "target/unpacked/copy_as_latex.js"
                            :output-dir "target/js"
                            :optimizations :whitespace
                            :pretty-print true}}}}}})
