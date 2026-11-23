(ns directed.networks.capacity-test
  (:require [clojure.test :refer :all]
            [directed.arc :refer [arc arc-fn]]
            [directed.networks.flow :refer [flow?]]
            [directed.networks.network :as n :refer [network]]
            [vertices :refer :all])
  (:use [directed.networks.capacity]))

(deftest make-capacity-network-test
  (testing "Пропускная способность задана функцией"
    (let [net (make-capacity-network (network S T [S A T] [S A] [S T] [A T]) (constantly 42))]
      (is (= {(arc S A) 42, (arc S T) 42, (arc A T) 42} (capacity net)) "Пропускная способность")
      (is (= 42 (arc-capacity net (arc S A))) "Пропускная способность дуги")))
  (testing "Пропускная способность должна быть положительной"
    (is (thrown? AssertionError
                 (make-capacity-network (network S T [S T] [S T] [T S])
                                        {(arc S T) -1, (arc T S) 2}))
        "Отрицательная пропускная способность")
    (is (thrown? AssertionError
                 (make-capacity-network (network S T [S T] [S T] [T S])
                                        {(arc S T) 1, (arc T S) 0}))
        "Нулевая пропускная способность")))

(deftest capacity-network-test
  (let [net (capacity-network S T [S A B C T] [S A 10] [A B 15] [B C 5] [C T 8])]
    (is (= S (n/source net)) "Источник")
    (is (= T (n/target net)) "Сток")
    (is (= #{A B C} (n/internal-vertices net)) "Внутренние вершины")
    (is (= #{A B C S T} (n/vertices net)) "Вершины")
    (is (= #{(arc S A) (arc A B) (arc B C) (arc C T)} (n/arcs net)) "Дуги")
    (is (= {(arc S A) 10, (arc A B) 15, (arc B C) 5, (arc C T) 8} (capacity net)) "Пропускная способность")
    (is (= 15 (arc-capacity net (arc A B))) "Пропускная способность дуги")))

(deftest feasible-flow?-test
  (let [net (capacity-network S T [S A B T] [S A 10] [A B 20] [B A 20] [B T 10])]
    (testing "Функция не является потоком"
      (let [f (constantly 10)]
        (is (not (flow? net f)) "Не поток")
        (is (thrown? AssertionError (feasible-flow? net f)))))
    (testing "Допустимый поток"
      (let [f (arc-fn [A B 15] [B A 15])]
        (is (flow? net f) "Поток")
        (is (feasible-flow? net f) "Допустимый")))
    (testing "Максимальный поток - допустимый"
      (let [f (arc-fn [S A 10] [A B 20] [B A 10] [B T 10])]
        (is (flow? net f) "Поток")
        (is (feasible-flow? net f) "Допустимый")))
    (testing "Нулевой поток - допустимый"
      (let [f (constantly 0)]
        (is (flow? net f) "Поток")
        (is (feasible-flow? net f)) "Допустимый"))
    (testing "Слишком большой поток"
      (let [f (arc-fn [S A 15] [A B 15] [B A 0] [B T 15])]
        (is (flow? net f) "Поток")
        (is (not (feasible-flow? net f)) "Не допустимый")))
    (testing "Поток с отрицательными значениями"
      (let [f (arc-fn [S A -10] [A B -5] [B A 5] [B T -10])]
        (is (flow? net f) "Поток")
        (is (not (feasible-flow? net f)) "Не допустимый")))))

(deftest feasible-flow?-example-test
  (let [net (capacity-network S T [S A B T] [S A 2] [S B 3] [A B 1] [A T 4] [B T 5])
        f (arc-fn [S A 2] [S B 2] [A B 1] [A T 1] [B T 3])]
    (is (flow? net f) "Поток")
    (is (feasible-flow? net f) "Допустимый")))
