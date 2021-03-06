# Associahedron

A little study of the associahedron,
based on the paper Realization of the Stasheﬀ polytope
by Jean-Louis Loday

## Usage

`(use 'associahedron.core)`

The function `psi` computes the weight (vertex) from the given permutation and returns this weight plus its tree representation

```
(psi '[3 1 2])
=> {:tree [[0 1] [2 3]], :weight [1 4 1], :permutation [3 1 2]}
```

`associahedron` returns all the edges of an associahedron for the given list of symbols


```
(associahedron '(a b c d e))
=>
([((a b) (c (d e))) (a (b (c (d e))))]
 [(((a b) c) (d e)) ((a b) (c (d e)))]
 [(((a b) c) (d e)) ((a (b c)) (d e))]
 [((a b) ((c d) e)) (a (b ((c d) e)))]
 [((a b) ((c d) e)) ((a b) (c (d e)))]
 [(((a b) (c d)) e) ((a b) ((c d) e))]
 [(((a b) (c d)) e) ((a (b (c d))) e)]
 [(((a b) c) (d e)) ((a b) (c (d e)))]
 [(((a b) c) (d e)) ((a (b c)) (d e))]
 [((((a b) c) d) e) (((a b) c) (d e))]
 [((((a b) c) d) e) (((a b) (c d)) e)]
 [((((a b) c) d) e) (((a (b c)) d) e)]
 [((a b) (c (d e))) (a (b (c (d e))))]
 [(((a b) c) (d e)) ((a b) (c (d e)))]
 [(((a b) c) (d e)) ((a (b c)) (d e))]
 [((a b) (c (d e))) (a (b (c (d e))))]
 [((a (b c)) (d e)) (a ((b c) (d e)))]
 [(a ((b c) (d e))) (a (b (c (d e))))]
 [((a b) ((c d) e)) (a (b ((c d) e)))]
 [((a b) ((c d) e)) ((a b) (c (d e)))]
 [(((a b) (c d)) e) ((a b) ((c d) e))]
 [(((a b) (c d)) e) ((a (b (c d))) e)]
 [((a b) ((c d) e)) (a (b ((c d) e)))]
 [((a b) ((c d) e)) ((a b) (c (d e)))]
 [(a (b ((c d) e))) (a (b (c (d e))))]
 [((a (b (c d))) e) (a ((b (c d)) e))]
 [(a ((b (c d)) e)) (a (b ((c d) e)))]
 [((a (b c)) (d e)) (a ((b c) (d e)))]
 [(((a (b c)) d) e) ((a (b c)) (d e))]
 [(((a (b c)) d) e) ((a ((b c) d)) e)]
 [((a (b c)) (d e)) (a ((b c) (d e)))]
 [(a ((b c) (d e))) (a (b (c (d e))))]
 [((a ((b c) d)) e) (a (((b c) d) e))]
 [((a ((b c) d)) e) ((a (b (c d))) e)]
 [(a (((b c) d) e)) (a ((b c) (d e)))]
 [(a (((b c) d) e)) (a ((b (c d)) e))])
```

```
(require '[loom.io :as lio])
(require '[loom.graph :as lg])

(lio/view
  (apply lg/digraph
    (associahedron '(a b c d e f))))
```

## License

Copyright © Matthew Chadwick 2019

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
