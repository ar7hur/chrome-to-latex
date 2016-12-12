(ns copy-as-latex.popup
  (:require [reagent.core :as reagent :refer [atom]]))

(def state (atom {:logs [] :article "~"}))

(defn main []
  (let [{:keys [logs article]} @state]
    [:div
       [:ul
         (for [l logs]
           [:li l])]
       [:div {:class "article"} article]]))

(defn init []
  (js/document.addEventListener 
    "DOMContentLoaded"
    #(reagent/render-component [main]
                               (js/document.getElementById "app")))
  (.query js/chrome.tabs
          #js {:active true :currentWindow true}
          (fn [result]
            (when-let [tab-id (.-id (first result))]
              (js/chrome.tabs.sendMessage tab-id (clj->js :scrap)
                (fn response [resp] 
                  (js/console.log resp)
                  (reset! state (js->clj resp :keywordize-keys true))))))))