---
number: 356
title: Line Reflection
difficulty: MEDIUM
topic: Arrays & Hashing
pattern: Hash Map
time: O(n)
space: O(n)
---

## Description

Given points on a plane, determine whether there is a vertical line that the whole set is symmetric about: the reflection of every point is also in the set.

Example 1:
Input: points = [[1,1],[-1,1]]
Output: true
Explanation: The line x = 0

Example 2:
Input: points = [[1,1],[-1,-1]]
Output: false

Constraints:
- 1 ≤ len(points) ≤ 10^4
- -10^8 ≤ points[i][j] ≤ 10^8

## Idea

If the line exists, the leftmost point reflects into the rightmost one, so the line sits exactly in the middle: c = (minX + maxX) / 2. Reflecting x across it gives 2c − x = minX + maxX − x, so no division is needed. Put all points into a set and check that every point has its reflection.

## Solution

```go
func isReflected(points [][]int) bool {
	inf := 1 << 30
	minX, maxX := inf, -inf
	checkSet := map[[2]int]bool{}
	for _, p := range points {
		minX = min(minX, p[0])
		maxX = max(maxX, p[0])
		checkSet[[2]int{p[0],p[1]}] = true
	}
	c := minX + maxX
	for _, p := range points {
		if !checkSet[[2]int{c - p[0],p[1]}] {
			return false
		}
	}
	return true
}
```
