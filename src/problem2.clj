(ns problem2
  (:require [clojure.spec.alpha :as s]
            [cheshire.core :as json]
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

(s/def :invoice/issue-date string?)
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
                                (str k " " (custom-explain-data (s/get-spec k spec) nested-error)))
                              v))
            (conj errors (str k " " v))))
        []
        explanation)
      nil)))

(defn read-and-validate-invoice [file-name]
  ;; Step 1: Read the JSON file using slurp
  (let [json-data (json/parse-string (slurp file-name) true)]
    ;; Step 2: Define the spec and validate the parsed data
    (let [invoice-spec ::invoice
          validation-result (s/valid? invoice-spec json-data)]
      (if validation-result
        ;; Step 3: If valid, return the invoice
        json-data
        ;; Step 4: If invalid, handle validation errors
        (do
          (println "Validation Errors:")
          (doseq [error (custom-explain-data invoice-spec json-data)]
            (println error))
          nil)))))

(def invoice (read-and-validate-invoice "invoice.json"))

(if invoice
  (println invoice)
  (println "Invoice is invalid."))
