(ns directed.networks.min-cut-test
  (:require [clojure.test :refer :all]
            [directed.arc :refer [arc-fn]]
            [directed.networks.capacity :refer [capacity-network]]
            [directed.networks.cut :refer [cut-capacity make-cut]]
            [directed.networks.flow :refer [flow-value]]
            [vertices :refer :all])
  (:use [directed.networks.min-cut]))

(deftest find-min-cut-test
  (testing "S -> T"
    (let [net (capacity-network S T [S T] [S T 10])
          f (arc-fn [S T 10])]
      (is (= 10 (flow-value net f)) "Мощность потока")
      (let [cut (find-min-cut net f)]
        (is (= (make-cut net #{S}) cut) "Разрез")
        (is (= 10 (cut-capacity net cut)) "Пропускная способность разреза"))))
  (testing "S -> A -> T"
    (let [net (capacity-network S T [S A T] [S A 20] [A T 15])
          f (arc-fn [S A 15] [A T 15])]
      (is (= 15 (flow-value net f)) "Мощность потока")
      (let [cut (find-min-cut net f)]
        (is (= (make-cut net #{S A}) cut) "Разрез")
        (is (= 15 (cut-capacity net cut)) "Пропускная способность разреза"))))
  (testing "S <-> A <-> T"
    (let [net (capacity-network S T [S A T] [S A 30] [A S 30] [A T 25] [T A 25])
          f (arc-fn [S A 30] [A S 5] [A T 25])]
      (is (= 25 (flow-value net f)) "Мощность потока")
      (let [cut (find-min-cut net f)]
        (is (= (make-cut net #{S A}) cut) "Разрез")
        (is (= 25 (cut-capacity net cut)) "Пропускная способность разреза"))))
  (testing "S -> A -> T - поток не максимальной мощности"
    (let [net (capacity-network S T [S A T] [S A 20] [A T 15])
          f (arc-fn [S A 10] [A T 10])]
      (is (= 10 (flow-value net f)) "Мощность потока")
      (let [cut (find-min-cut net f)]
        (is (nil? cut) "Разрез не найден")))))
