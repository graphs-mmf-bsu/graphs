(ns directed.networks.min-cut
  (:require [directed.arc :refer [from to]]
            [directed.networks.capacity :refer [arc-capacity feasible-flow?]]
            [directed.networks.cut :refer [make-cut]]
            [directed.networks.network :as n]
            [utils :refer [???]]))

(defn find-min-cut
  "Для максимального потока в сети находит разрез,
  пропускная способность которого равна мощности потока.
  Если поток не максимальный, возвращает nil."
  [network flow]
  {:pre [(feasible-flow? network flow)]}
  (loop [X (???)]
    (let [X' (into X (concat (for [v X
                                   a (???)
                                   :when (???)]
                               (???))
                             (for [v X
                                   a (???)
                                   :when (???)]
                               (???))))]
      (cond (contains? X' (n/target network)) nil
            (???) (make-cut network X)
            :else (???)))))
