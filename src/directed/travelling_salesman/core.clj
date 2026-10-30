(ns directed.travelling-salesman.core
  (:require [clojure.set :as set]
            [directed.travelling-salesman.node :as n]
            [directed.travelling-salesman.route :refer [hamiltonian-cycle?]]
            [directed.weighted-graph :as wg]
            [utils :refer [???]]))

(defn shortest-hamiltonian-cycle-little
  "Находит в данном графе гамильтонов цикл наименьшей стоимости с помощью алгоритма Литтла."
  [graph]
  {:post [(hamiltonian-cycle? graph %)]}
  (loop [best-length ##Inf
         best-cycle nil
         nodes #{(???)}]
    (if (empty? nodes)
      best-cycle
      (let [node (???)
            remaining-nodes (???)]
        (if (n/leaf? node)
          (let [cycle (???)
                length (???)]
            (if (< length best-length)
              (recur (???)
                     (???)
                     (set/select #(< (n/lower-bound %) length) remaining-nodes))
              (recur (???)
                     (???)
                     remaining-nodes)))
          (let [[n-without n-with] (n/branch-on-highest-penalty-arc node)]
            (recur best-length
                   best-cycle
                   (???))))))))
