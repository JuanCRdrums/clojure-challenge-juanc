(ns problem3
  (:require [invoice-item]
            [clojure.test :refer :all]))


(deftest test-subtotal
         ; Test with default values (no discount)
         (is (= (invoice-item/subtotal {:invoice-item/precise-quantity 2
                                :invoice-item/precise-price 10})
                20))

         ; Test with a discount rate of 5%
         (is (= (invoice-item/subtotal {:invoice-item/precise-quantity 3
                                :invoice-item/precise-price 15
                                :invoice-item/discount-rate 5})
                42.75))

         ; Test with a discount rate of 10%
         (is (= (invoice-item/subtotal {:invoice-item/precise-quantity 5
                                :invoice-item/precise-price 8.5
                                :invoice-item/discount-rate 10})
                38.25))

         ; Test with zero quantity
         (is (= (invoice-item/subtotal {:invoice-item/precise-quantity 0
                                :invoice-item/precise-price 10
                                :invoice-item/discount-rate 5})
                0))

         ; Test with zero price
         (is (= (invoice-item/subtotal {:invoice-item/precise-quantity 4
                                :invoice-item/precise-price 0
                                :invoice-item/discount-rate 8})
                0)))

(run-tests 'test-subtotal)



