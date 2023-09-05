(ns problem1
  (:require [clojure.edn :as edn]))

(def invoice (clojure.edn/read-string (slurp "invoice.edn")))

(defn satisfies-conditions? [invoice]
  (let [iva-19%? (some #(= (:tax/category %) :iva :tax/rate 19) (:taxable/taxes invoice))
        ret-fuente-1%? (some #(= (:retention/category %) :ret_fuente :retention/rate 1) (:retentionable/retentions invoice))]
    (and (or iva-19%? ret-fuente-1%?)     ; Must have at least one condition
         (not (and iva-19%? ret-fuente-1%?)) ; Cannot have both conditions
         (every? (complement nil?) [iva-19%? ret-fuente-1%?])))) ; Every item must satisfy exactly one condition

(def invoices-satisfying-conditions
  (->> (:invoice/items invoice)
       (group-by :invoice-item/id)
       (vals)
       (filter satisfies-conditions?)
       (map :invoice-item/id)))

(prn invoices-satisfying-conditions)