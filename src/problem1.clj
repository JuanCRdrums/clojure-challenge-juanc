(ns problem1
  (:require [clojure.edn]))

(def invoice (clojure.edn/read-string (slurp "invoice.edn")))

(defn xor [a b]
  (or (and a (not b))
      (and b (not a))))

(defn has-iva-19% [item]
  (some (fn [tax]
          (and (= (:tax/category tax) :iva)
               (= (:tax/rate tax) 19)))
        (:taxable/taxes item)))

(defn has-ret-fuente-1% [item]
  (some (fn [retention]
          (and (= (:retention/category retention) :ret_fuente)
               (= (:retention/rate retention) 1)))
        (:retentionable/retentions item)))

(defn filter-items-satisfying-conditions [invoice]
  (->> invoice
       :invoice/items
       (filter (fn [item]
                 (let [has-iva-19% (has-iva-19% item)
                       has-ret-fuente-1% (has-ret-fuente-1% item)]
                   (xor has-iva-19% has-ret-fuente-1%))))))


(def filtered-items (filter-items-satisfying-conditions invoice))

;; Print the filtered items using prn
(prn filtered-items)