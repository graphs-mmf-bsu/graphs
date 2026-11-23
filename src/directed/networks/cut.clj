(ns directed.networks.cut
  (:require [clojure.set :as set]
            [directed.arc :refer [from to]]
            [directed.networks.capacity :refer [capacity]]
            [directed.networks.network :as n]
            [utils :refer [???]]))

(defn make-cut
  "Создаёт разрез по данному подмножеству вершин"
  [network vertex-set]
  {:pre [(set? vertex-set) (set/subset? vertex-set (n/vertices network))
         (contains? vertex-set (n/source network)) (not (contains? vertex-set (n/target network)))]}
  {:source-vertices vertex-set
   :target-vertices (set/difference (n/vertices network) vertex-set)})

(defn source-vertices
  "Возвращает часть разреза, включающую источник."
  [cut]
  (:source-vertices cut))

(defn target-vertices
  "Возвращает часть разреза, включающую сток."
  [cut]
  (:target-vertices cut))

(defn cut-forward-arcs
  "Возвращает дуги, идущие через разрез в прямом направлении (от источника к стоку)."
  [network cut]
  (???))

(defn cut-backward-arcs
  "Возвращает дуги, идущие через разрез в обратном направлении (от стока к источнику)."
  [network cut]
  (???))

(defn cut-divergence
  "Возвращает дивергенцию потока на разрезе."
  [network flow cut]
  (???))

(defn cut-capacity
  "Возвращает пропускную способность разреза."
  [network cut]
  (???))
