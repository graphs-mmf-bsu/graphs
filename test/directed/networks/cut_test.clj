(ns directed.networks.cut-test
  (:require [clojure.test :refer :all]
            [directed.arc :refer [arc arc-fn]]
            [directed.networks.capacity :refer [capacity-network]]
            [directed.networks.flow :refer [flow? flow-value]]
            [directed.networks.network :refer [network]]
            [vertices :refer :all])
  (:use [directed.networks.cut]))

(deftest make-cut-test
  (let [net (network S T [S A B T] [S A] [S T] [A B] [A T] [B A] [B T])]
    (testing "Разрез ({S}, {A B T})"
      (let [cut (make-cut net #{S})]
        (is (= #{S} (source-vertices cut)))
        (is (= #{A B T} (target-vertices cut)))))
    (testing "Разрез ({S A}, {B T})"
      (let [cut (make-cut net #{S A})]
        (is (= #{S A} (source-vertices cut)))
        (is (= #{B T} (target-vertices cut)))))
    (testing "Разрез ({S A B}, {T})"
      (let [cut (make-cut net #{S A B})]
        (is (= #{S A B} (source-vertices cut)))
        (is (= #{T} (target-vertices cut)))))
    (testing "Разрез включать вершины сети"
      (is (thrown? AssertionError (make-cut net #{S A B F G}))))
    (testing "Разрез должен включать источник"
      (is (thrown? AssertionError (make-cut net #{A B}))))
    (testing "Разрез не должен включать сток"
      (is (thrown? AssertionError (make-cut net #{S T}))))))

(deftest cut-forward-arcs-test
  (let [net (network S T [S A B T] [S A] [S T] [A B] [A T] [B A] [B T])]
    (testing "Разрез ({S}, {A B T})"
      (let [cut (make-cut net #{S})]
        (is (= #{(arc S A) (arc S T)} (cut-forward-arcs net cut)))))
    (testing "Разрез ({S A}, {B T})"
      (let [cut (make-cut net #{S A})]
        (is (= #{(arc S T) (arc A B) (arc A T)} (cut-forward-arcs net cut)))))
    (testing "Разрез ({S A B}, {T})"
      (let [cut (make-cut net #{S A B})]
        (is (= #{(arc S T) (arc A T) (arc B T)} (cut-forward-arcs net cut)))))))

(deftest cut-backward-arcs-test
  (let [net (network S T [S A B T] [S A] [S T] [A B] [A T] [B A] [B T])]
    (testing "Разрез ({S}, {A B T})"
      (let [cut (make-cut net #{S})]
        (is (= #{} (cut-backward-arcs net cut)))))
    (testing "Разрез ({S A}, {B T})"
      (let [cut (make-cut net #{S A})]
        (is (= #{(arc B A)} (cut-backward-arcs net cut)))))
    (testing "Разрез ({S A B}, {T})"
      (let [cut (make-cut net #{S A B})]
        (is (= #{} (cut-backward-arcs net cut)))))))

(deftest cut-divergence-test
  (let [net (network S T [S A B T] [S A] [S T] [A B] [A T] [B A] [B T])
        f (arc-fn [S A 25] [A B 35] [B A 10] [B T 25])]
    (is (flow? net f))
    (is (= 25 (flow-value net f)))
    (testing "Разрез ({S}, {A B T})"
      (let [cut (make-cut net #{S})]
        (is (= (flow-value net f)
               (cut-divergence net f cut)))))))

(deftest cut-capacity-test
  (let [net (capacity-network S T [S A B T] [S A 2] [S B 3] [A B 1] [A T 4] [B T 5])]
    (testing "Разрез ({S}, {A B T})"
      (let [cut (make-cut net #{S})]
        (is (= 5 (cut-capacity net cut)))))
    (testing "Разрез ({S A}, {B T})"
      (let [cut (make-cut net #{S A})]
        (is (= 8 (cut-capacity net cut)))))
    (testing "Разрез ({S A B}, {T})"
      (let [cut (make-cut net #{S A B})]
        (is (= 9 (cut-capacity net cut)))))))
