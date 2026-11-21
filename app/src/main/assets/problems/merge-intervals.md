---
number: 56
title: Merge Intervals
difficulty: MEDIUM
topic: Arrays & Hashing
pattern: Intervals
time: O(n log n)
space: O(n)
---

## Description

Given an array of intervals where intervals[i] = [start, end], merge all overlapping intervals and return an array of non-overlapping intervals that cover all the input ones.

Example 1:
Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: [1,3] and [2,6] overlap, so they merge into [1,6]

Example 2:
Input: intervals = [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals that touch at a point are merged too

Constraints:
- 1 ≤ len(intervals) ≤ 10^4
- 0 ≤ start ≤ end ≤ 10^4

## Idea

Sort the intervals by start. Then an interval can only overlap the last one in the result: if its start is not greater than the last end, extend that end to the maximum, otherwise append a new interval.

## Solution

```go
func merge(intervals [][]int) [][]int {
	sort.Slice(intervals, func(i, j int) bool {
		return intervals[i][0] < intervals[j][0]
	})
	out := [][]int{intervals[0]}
	for _, interval := range intervals {
		start, end := interval[0], interval[1]
		prevEnd := out[len(out)-1][1]
		if start <= prevEnd {
			out[len(out)-1][1] = max(prevEnd, end)
		} else {
			out = append(out, []int{start, end})
		}
	}
	return out
}
```
