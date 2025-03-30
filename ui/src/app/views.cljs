(ns app.views
  (:require
   [re-frame.core :as re-frame]
   [app.subs :as subs]
   [goog.string :as gstring]
   [app.events :as events]))

(defn simple-text-input
  [value callback-fn & [placeholder]]
  [:input
   {:type "text"
    :value value
    :placeholder placeholder
    :class "mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
    :on-change #(callback-fn (-> % .-target .-value))}])

(defn simple-number-input
  [value callback-fn]
  [:input
   {:type "number"
    :step "0.01"
    :placeholder "0.00"
    :class "mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
    :value value
    :on-change #(callback-fn (-> % .-target .-valueAsNumber))}])

(defn new-product-form
  [product]
  [:div {:class "bg-white shadow-md rounded-lg p-6 mb-8 max-w-md mx-auto"}
   [:h3 {:class "text-lg font-medium leading-6 text-gray-900 mb-4"} "Add a new product"]
   [:div {:class "space-y-4"}
    [:div
     [:label {:class "block text-sm font-medium text-gray-700"} "Name"]
     [simple-text-input (:name product) #(re-frame/dispatch [::events/update-new-product :name %]) "Enter product name"]]

    [:div
     [:label {:class "block text-sm font-medium text-gray-700"} "Price"]
     [simple-number-input (:price product) #(re-frame/dispatch [::events/update-new-product :price %])]]

    [:div
     [:label {:class "block text-sm font-medium text-gray-700"} "Description"]
     [simple-text-input (:description product) #(re-frame/dispatch [::events/update-new-product :description %]) "Enter product description"]]

    [:button {:class "w-full bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition-colors"
              :on-click #(re-frame/dispatch [::events/add-product product])}
     "Add Product"]]])

(defn main-panel
  []
  (let [loading? (re-frame/subscribe [::subs/loading?])
        product  (re-frame/subscribe [::subs/new-product])
        products (re-frame/subscribe [::subs/products])]
    [:div {:class "min-h-screen bg-gray-100 py-6 px-4 sm:px-6 lg:px-8"}
     [:div {:class "max-w-7xl mx-auto"}
      [new-product-form @product]

      [:div {:class "bg-white shadow-md rounded-lg p-6"}
       [:div {:class "flex justify-between items-center mb-6"}
        [:h2 {:class "text-xl font-semibold text-gray-900"} "Products"]
        [:button {:class "inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                 :on-click #(re-frame/dispatch [::events/load-products])}
         "Load Products"]]

       (when @loading?
         [:div {:class "flex justify-center items-center py-4"}
          [:div {:class "animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"}]])

       [:div {:class "grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3"}
        (for [product @products]
          ^{:key (:id product)}
          [:div {:class "relative bg-white rounded-lg border border-gray-200 p-4 hover:shadow-lg transition-shadow"}
           [:div {:class "flex justify-between items-start"}
            [:div
             [:h3 {:class "text-lg font-medium text-gray-900"} (:name product)]
             [:p {:class "mt-1 text-sm text-gray-500"} (:description product)]
             [:p {:class "mt-2 text-lg font-semibold text-indigo-600"}
              "$" (gstring/format "%.2f" (/ (:price-in-cents product) 100.00))]]
            [:button {:class "text-gray-400 hover:text-red-500 focus:outline-none"
                     :on-click #(re-frame/dispatch [::events/delete-product (:id product)])}
             [:svg {:class "h-5 w-5"
                   :xmlns "http://www.w3.org/2000/svg"
                   :viewBox "0 0 20 20"
                   :fill "currentColor"}
              [:path {:fill-rule "evenodd"
                     :d "M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                     :clip-rule "evenodd"}]]]]])]]]]))
