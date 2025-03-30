(ns app.core
  (:require
   [reagent.dom.client :as rdom.client]
   [re-frame.core :as re-frame]
   [app.events :as events]
   [app.views :as views]
   [app.config :as config]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (-> (rdom.client/create-root root-el)
        (rdom.client/render [views/main-panel]))))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::events/load-products])
  (dev-setup)
  (mount-root))
