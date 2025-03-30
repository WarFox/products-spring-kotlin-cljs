(ns app.views
  (:require
   [re-frame.core :as re-frame]
   [app.subs :as subs]
   [goog.string :as gstring]
   [app.events :as events]))

(defn simple-text-input
  [value callback-fn]
  [:input
   {:type "text"
    :value value
    :on-change #(callback-fn (-> % .-target .-value))}])

(defn simple-number-input
  [value callback-fn]
  [:input
   {:type "number"
    :step "0.01"
    :placeholder "0.00"
    :value value
    :on-change #(callback-fn (-> % .-target .-valueAsNumber))}])

(defn new-product-form
  [product]
  [:div
   [:h3 "Add a new product"]
   [:div
    [:label "Name:"]
    [simple-text-input (:name product) #(re-frame/dispatch [::events/update-new-product :name %])]]

   [:div
    [:label "Price:"]
    [simple-number-input (:price product) #(re-frame/dispatch [::events/update-new-product :price %])]]

   [:div
    [:label "Description:"]
    [simple-text-input (:description product) #(re-frame/dispatch [::events/update-new-product :description %])]]

   [:button {:on-click #(re-frame/dispatch [::events/add-product product])}
    "Add Product"]])

(defn main-panel
  []
  (let [loading? (re-frame/subscribe [::subs/loading?])
        product  (re-frame/subscribe [::subs/new-product])
        products (re-frame/subscribe [::subs/products])]
    [:div {:class "container mx-auto px-4 py-8"}
     [new-product-form @product]
     [:div 
      [:h2  "Products"]
      [:div
       [:button {:on-click #(re-frame/dispatch [::events/load-products])}
        "Load Products"]

       (when @loading?
         [:span "Loading..."])]

      [:ul
       (for [product @products]
         ^{:key (:id product)}
         [:li
          [:div  (:name product)
           [:span ", $" (gstring/format "%.2f" (/ (:price-in-cents product) 100.00))]
           [:span  ", "]
           [:span (:description product)]
           [:button {:on-click #(re-frame/dispatch [::events/delete-product (:id product)])} "×"]]])]]]))
