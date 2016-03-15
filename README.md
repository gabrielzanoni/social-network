# Social Network

A Clojure library designed to do social network analysis for prospective customers.

## Problem

Centrality metrics try to approximate a measure of influence of an individual within a social network. The distance between any two vertices is their shortest path. The *farness* of a given vertex *v* is the sum of all distances from each vertex to *v*. Finally, the *closeness* of a vertex *v* is the inverse of the *farness*.

The first part of the challenge is to rank the vertices in a given undirected graph by their *closeness*. The graph is provided in the attached file; each line of the file consists of two vertex names separated by a single space, representing an edge between those two nodes.

The second part of the challenge is to create a RESTful web server with endpoints to register edges and to render a ranking of vertexes sorted by centrality. We can think of the centrality value for a node as an initial "score" for that customer.

The third and final part is to add another endpoint to flag a customer node as "fraudulent". It should take a vertex id, and update the internal customer score as such:
- The fraudulent customer score should be zero.
- Customers directly related to the "fraudulent" customer should have their score halved.
- More generally, scores of customers indirectly referred by the "fraudulent" customer should be multiplied by a coefficient F:

F(k) = (1 - (1/2)^k)


where k is the length of the shortest path from the "fraudulent" customer to the customer in question.


## Solution
The solution for the problem was based on the [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm). The algorithm is applied to every vertex on the graph and, with the shortest distance map of each vertex in hand, we can calculate de centrality using this formula:

![Centrality Formula](https://upload.wikimedia.org/math/b/e/9/be90ddd8ead1f4c7e07f1f7b0e4c14ac.png)

The centrality was used then as the initial scoreof each vertex (or "costumer"). When a costumer is flagged as "fraudulent" his score is set to 0. As consequence, a coefficient-f-map is calculated for this node - using the vertex as the keys and the result of F(k) as the values.

Finally, to generate the final score the system multiple the centrality-map by all the coefficent-f-map.


## Running / Testing

Run the server using port 3000:

```
$ lein ring server
```

Testing:
```
$ lein test
```

## Endpoints
- Vertex list with centrality values:
```
Method: GET
Path: /vertex-centrality
```

- Vertex list with final score values:
```
Method: GET
Path: /vertex
```

- Add an edge to **existent** pair of vertex:
```
Method: POST
Path: /vertex
Content-Type: application/json
Body: [vertex1 vertex2]          // ex: [12 20]
```

- Flag an **existent** vertex as fraudulent:
```
Method: POST
Path: /fraudulent
Content-Type: application/json
Body: vertex                    // ex: 12
```

