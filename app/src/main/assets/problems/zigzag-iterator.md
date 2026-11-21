---
number: 281
title: Zigzag Iterator
difficulty: MEDIUM
topic: Design
pattern: Design
time: Next is O(1), HasNext is O(k)
space: O(k), k is the number of lists
---

## Description

Given several lists of numbers, implement an iterator that returns their elements in turns: the first element of the first list, the first element of the second list and so on in a circle. Exhausted lists are skipped. HasNext() reports whether elements remain, Next() returns the next one.

Example 1:
Input: v1 = [1,2], v2 = [3,4,5,6]
Output: [1,3,2,4,5,6]
Explanation: Elements alternate, and after v1 ends the rest of v2 follows

Example 2:
Input: v1 = [1], v2 = []
Output: [1]

Constraints:
- 0 ≤ length of each list ≤ 1000
- 1 to 2000 elements in total
- -2^31 ≤ v[i] ≤ 2^31 - 1

## Idea

Keep the index of the current list curr and, for every list, the index of its next element. Next takes the element of the current list, advances its index and moves to the next list in a circle. HasNext skips exhausted lists; if it goes full circle back to the start, no elements are left.

## Solution

```go
type ZigZagIterator struct {
	curr    int
	slices  [][]int
	indexes []int
}
func (z *ZigZagIterator) HasNext() bool {
	if len(z.slices) == 0 {
		return false
	}
	start := z.curr
	for z.indexes[z.curr] == len(z.slices[z.curr]) {
		z.curr = (z.curr + 1) % len(z.slices)
		if z.curr == start {
			return false
		}
	}
	return true
}
func (z *ZigZagIterator) Next() int {
	currSlice := z.slices[z.curr]
	currIndex := z.indexes[z.curr]
	v := currSlice[currIndex]
	z.indexes[z.curr]++
	z.curr = (z.curr + 1) % len(z.slices)
	return v
}
```
