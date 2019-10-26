(ns associahedron.core
  (:require
    [clojure.walk :as w]))

(defn psi
  "

   Loday: arXiv:math/0212126v1

   Under interpreting a permutation as a
   planar binary tree with levels
   (cf. for instance [LR]), and then
   forgetting the levels,
   one gets a well-deﬁned map

   ψ : Sn →→ Yn

  "
  ([s]
   (psi s
    (reductions
      (fn [t i]
        ; [internal-vertex members weight]
        (let [
              [l lm lw] (if-let [u (first (filter (fn [s] (get s (dec i))) (keys t)))] (get t u) [(dec i) #{} []])
              [r rm rw] (if-let [u (first (filter (fn [s] (get s      i))  (keys t)))] (get t u) [i       #{} []])]
          (cond
            (and (number? l) (number? r))
              (let [p #{l r} st [[l r] p [1 1]]]
                (assoc t #{l r} st :i i :p p))
            (and (number? l) (vector? r))
              (let [p (conj rm l) st [[l r] p [1 (reduce + rw)]]]
                (assoc (dissoc t rm) p st :i i :p p))
            (and (vector? l) (number? r))
              (let [p (conj lm r) st [[l r] p [(reduce + lw) 1]]]
                (assoc (dissoc t lm) p st :i i :p p))
            :both-branches
              (let [p (into lm rm) st [[l r] p [(reduce + lw) (reduce + rw)]]]
                (assoc (dissoc t lm rm) p st :i i :p p)))))
        {} s)))
      ([s t]
       (let [w (mapv (fn [[_ w]] (reduce * w))
                 (sort-by first
                   (map (fn [{p :p i :i :as s}] [i (last (get s p))])
                     (rest t))))
             {p :p :as result} (last t)]
         {:tree   (first (get result p))
          :weight w
          :permutation s})))

(defn as-exp [S t]
  (w/postwalk
    (fn [x]
      (if (vector? x)
        (apply list x)
        (S x)))
    t))

(defn y> [[x y]]
  (list (first x) (list (last x) y)))

(defn v
  ([[x y :as l]]
   (let [lx (list? x) ly (list? y)]
     (case [lx ly]
       [true true]
         (let [xx (v x) yy (v y)]
           (cons (y> l) (concat (map (fn [t] (list t y)) xx) (map (partial list x) yy))))
       [true false]
         (if-let [xx (v x)]
           (cons (y> l) (map (fn [t] (list t y)) xx))
           [(y> l)])
       [false true]
         (if-let [yy (v y)]
           (mapv (partial list x) yy)
           [])
       [false false]
         []))))

(defn perm-associahedron
  ([symbols S]
    (mapcat (partial perm-associahedron symbols) S (map vector S) (repeat S)))
  ([symbols x S' S]
    (mapcat
      (fn [y]
        (into
          (if (== 2 (count S))
            (let [xp (as-exp symbols (:tree (psi (conj S' y))))]
              (map (fn [ce] [xp ce]) (v xp)))
            [])
          (perm-associahedron symbols y (conj S' y) (disj S x))))
      (disj S x))))