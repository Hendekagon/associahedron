(ns associahedron.core
  "
    Function for computing
    vertices and edges of the
    associahedron from the permutahedron

    Rough inefficient code

  "
  (:require
    [clojure.walk :as w]
    [clojure.math.combinatorics :as x]))

(defn ψ
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
   (ψ s
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
        (S x))) t))

(defn vy> [[x y]]
  [(first x) [(last x) y]])

(defn vv
  ([[x y :as l]]
   (let [lx (vector? x) ly (vector? y)]
     (case [lx ly]
       [true true]
         (let [xx (vv x) yy (vv y)]
           (into [(vy> l)] (into (mapv (fn [t] [t y]) xx) (mapv (partial vector x) yy))))
       [true false]
         (if-let [xx (vv x)]
           (into [(vy> l)] (mapv (fn [t] [t y]) xx))
           [(vy> l)])
       [false true]
         (if-let [yy (vv y)]
           (mapv (partial vector x) yy)
           [])
       [false false]
         nil))))

(defn associahedron
  "
    Returns the edges
    of the associahedron
    for the given symbols
    by traversing the permutahedron

    (1N 2N 5N 14N 42N 132N 429N 1430N 4862N 16796N 58786N 208012N 742900N)
  "
  ([symbols]
    (associahedron symbols (into #{} (range 1 (count symbols)))))
  ([symbols S]
   (let [pa (map (fn [p] (let [t (ψ p) ex (as-exp (vec symbols) (:tree t))] (assoc t :exp ex)))
              (x/permutations S))]
     (mapcat (partial associahedron symbols
              (into {} (map (juxt :tree identity) pa))
              (into {} (map (juxt :permutation identity) pa)))
      S (map vector S) (repeat S))))
  ([symbols tt tp x S' S]
    (mapcat
      (fn [y]
        (into
          (if (== 2 (count S))
            (let [xp (tp (conj S' y))]
              (map (fn [ce] [xp (tt ce)]) (vv (:tree xp))))
            [])
          (associahedron symbols tt tp y (conj S' y) (disj S x))))
      (disj S x))))