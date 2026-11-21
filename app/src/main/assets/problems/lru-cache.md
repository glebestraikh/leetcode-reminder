---
number: 146
title: LRU Cache
difficulty: MEDIUM
topic: Design
pattern: Design
time: O(1) per Get and Put
space: O(n)
---

## Description

Implement an LRU cache with capacity capacity. Get(key) returns the value for the key, or -1 if the key is missing. Put(key, value) inserts or updates the value; when the cache is full, it evicts the key that was used least recently. Both operations must run in O(1) on average.

Example 1:
Input: LRUCache(2), Put(1, 1), Put(2, 2), Get(1), Put(3, 3), Get(2), Put(4, 4), Get(1), Get(3), Get(4)
Output: -, -, -, 1, -, -1, -, -1, 3, 4
Explanation: Put(3, 3) evicts key 2, Put(4, 4) evicts key 1

Constraints:
- 1 ≤ capacity ≤ 3000
- 0 ≤ key ≤ 10^4
- 0 ≤ value ≤ 10^5
- At most 2 · 10^5 calls to Get and Put

## Idea

A map from key to node gives O(1) lookup, and a doubly linked list keeps the usage order: the oldest on the left, the newest on the right. Dummy nodes left and right remove nil checks. Get and Put move the node to the right end; on overflow, remove the node right after left.

## Solution

```go
type LRUCache struct {
	cap    int
	caches map[int]*Node
	left   *Node
	right  *Node
}
type Node struct {
	key  int
	val  int
	prev *Node
	next *Node
}
func Constructor(capacity int) LRUCache {
	lru := LRUCache{
		cap:    capacity,
		caches: map[int]*Node{},
		left:   &Node{},
		right:  &Node{},
	}
	lru.left.next = lru.right
	lru.right.prev = lru.left
	return lru
}
func (lru *LRUCache) remove(node *Node) {
	prev, next := node.prev, node.next
	prev.next = next
	next.prev = prev
}
func (lru *LRUCache) insert(node *Node) {
	prevRight := lru.right.prev
	prevRight.next = node
	lru.right.prev = node
	node.next = lru.right
	node.prev = prevRight
}
func (lru *LRUCache) Get(key int) int {
	if node, exists := lru.caches[key]; exists {
		lru.remove(node)
		lru.insert(node)
		return node.val
	}
	return -1
}
func (lru *LRUCache) Put(key, val int) {
	if node, exists := lru.caches[key]; exists {
		lru.remove(node)
		node.val = val
		lru.insert(node)
		return
	}
	if len(lru.caches) == lru.cap {
		node := lru.left.next
		lru.remove(node)
		delete(lru.caches, node.key)
	}
	node := &Node{
		key: key,
		val: val,
	}
	lru.insert(node)
	lru.caches[key] = node
}
```
