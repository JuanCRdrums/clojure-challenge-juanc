(ns problem2
  (:require [cheshire.core :as json]
            [clojure.spec.alpha :as s]
            [clojure.walk :refer [postwalk]]
            [clj-time.format :as tf]
            ))


(defn not-blank? [value] (-> value clojure.string/blank? not))
(defn non-empty-string? [x] (and (string? x) (not-blank? x)))

(s/def :customer/name non-empty-string?)
(s/def :customer/email non-empty-string?)
(s/def :invoice/customer (s/keys :req [:customer/name
                                       :customer/email]))

(s/def :tax/rate double?)
(s/def :tax/category #{:iva})
(s/def ::tax (s/keys :req [:tax/category
                           :tax/rate]))
(s/def :invoice-item/taxes (s/coll-of ::tax :kind vector? :min-count 1))

(s/def :invoice-item/price double?)
(s/def :invoice-item/quantity double?)
(s/def :invoice-item/sku non-empty-string?)

(s/def ::invoice-item
  (s/keys :req [:invoice-item/price
                :invoice-item/quantity
                :invoice-item/sku
                :invoice-item/taxes]))

(s/def :invoice/issue-date inst?)
(s/def :invoice/items (s/coll-of ::invoice-item :kind vector? :min-count 1))

(s/def ::invoice
  (s/keys :req [:invoice/issue-date
                :invoice/customer
                :invoice/items]))





(defn custom-explain-data [spec data]
  (let [explanation (s/explain spec data)]
    (if explanation
      (reduce
        (fn [errors [k v]]
          (if (sequential? v)
            (into errors (map (fn [nested-error]
                                (str k " " (custom-explain-data (s/get-spec k) nested-error)))
                              v))
            (conj errors (str k " " v))))
        []
        explanation)
      nil)))




(defn modify-json [data]
  (postwalk
    (fn [item]
      ;;(println item)
      (cond
        (and (map? item) (:issue_date item) (:customer item) (:items item)
             (:payment_means_type item) (:order_reference item) (:number item) (:payment_date item)
             (:payment_means item) (:retentions item))
        (-> item
            (dissoc :issue_date)
            (assoc :invoice/issue-date (tf/parse (tf/formatter "dd/MM/yyyy") (:issue_date item)))
            (dissoc :customer)
            (assoc :invoice/customer (:customer item))
            (dissoc :items)
            (assoc :invoice/items (:items item))
            (dissoc :payment_means_type)
            (assoc :invoice/payment_means_type (:payment_means_type item))
            (dissoc :order_reference)
            (assoc :invoice/order_reference (:order_reference item))
            (dissoc :number)
            (assoc :invoice/number (:number item))
            (dissoc :payment_date)
            (assoc :invoice/payment_date (:payment_date item))
            (dissoc :payment_means)
            (assoc :invoice/payment_date (:payment_date item))
            (dissoc :retentions)
            (assoc :invoice/retentions (:retentions item)))
        (and (map? item) (:price item) (:quantity item) (:sku item) (:taxes item))
        (-> item
            (dissoc :price)
            (assoc :invoice-item/price (:price item))
            (dissoc :quantity)
            (assoc :invoice-item/quantity (:quantity item))
            (dissoc :sku)
            (assoc :invoice-item/sku (:sku item))
            (dissoc :taxes)
            (assoc :invoice-item/taxes (:taxes item)))
        (and (map? item) (:tax_category item) (:tax_rate item))
        (-> item
            (dissoc :tax_category)
            (assoc :tax/category :iva)
            (dissoc :tax_rate)
            (assoc :tax/rate (double (:tax_rate item))))
        (and (map? item) (:company_name item) (:email item))
        (-> item
            (dissoc :company_name)
            (assoc :customer/name (:company_name item))
            (dissoc :email)
            (assoc :customer/email (:email item)))
        :else
        item))
    data))





(defn process [file-name]
  (let [json (json/parse-string (slurp file-name) true)  modified-json (modify-json (get json :invoice))]
    modified-json)
  )

(def new-invoice (process "invoice.json"))
(println new-invoice)

(def valid (s/valid? ::invoice new-invoice))
(println valid)




