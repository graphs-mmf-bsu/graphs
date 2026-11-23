(ns directed.networks.capacity
  (:require [directed.arc :refer [arc]]
            [directed.networks.network :refer [arcs make-network]]
            [directed.networks.flow :refer [flow?]]
            [utils :refer [???]]))

(defn make-capacity-network
  "Создаёт сеть с пропускной способностью."
  [network capacity]
  (assoc network
    :capacity (into {} (map (fn [a]
                              (let [c (capacity a)]
                                (assert (pos? c) "Пропускная способность дуги должна быть положительной")
                                [a c]))
                            (arcs network)))))

(defn capacity-network
  "Создаёт сеть с данными источником, стоком, вершинами и дугами с пропускной способностью.
  Дуги заданы в виде троек (начало дуги, конец дуги, пропускная способность дуги)."
  [source target vertices & capacity-arcs]
  (let [capacity (into {} (map (fn [[a b c]] [(arc a b) c])
                               capacity-arcs))]
    (make-capacity-network (make-network source target vertices (keys capacity))
                           capacity)))

(defn capacity
  "Возвращает пропускную способность сети в виде отображения,
  в котором каждой дуге соответствует значение пропускной способности на этой дуге."
  [network]
  (:capacity network))

(defn arc-capacity
  "Возвращает пропускную способность дуги."
  [network arc]
  ((capacity network) arc))

(defn feasible-flow?
  "Проверяет, что поток является допустимым."
  [network f]
  {:pre [(flow? network f)]}
  (???))
