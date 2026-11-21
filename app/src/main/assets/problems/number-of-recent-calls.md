---
number: 933
title: Number of Recent Calls
difficulty: EASY
topic: Stack & Queue
pattern: Queue
time: O(1) amortized per Ping
space: O(W), W is the number of requests in the window
---

## Description

Implement RecentCounter. Ping(t) records a request at time t (in milliseconds) and returns the number of requests in the last 3000 ms, that is in the range [t − 3000, t]. Every t is strictly larger than the previous one.

Example 1:
Input: Ping(1), Ping(100), Ping(3001), Ping(3002)
Output: 1, 2, 3, 3
Explanation: At t = 3002 the request at 1 is already outside the window [2, 3002]

Constraints:
- 1 ≤ t ≤ 10^9
- t strictly increases between calls
- At most 10^4 calls to Ping

## Idea

A queue of the requests in the window: append the new t to the back and drop from the front everything older than t − 3000. Each request is added and removed once, so Ping is O(1) on average. The alternative keeps the whole history and binary-searches the first time ≥ t − 3000: O(log n) per call but O(n) memory.

## Solution

```go
type RecentCounter struct {
	q []int
}
func Constructor() RecentCounter {
	return RecentCounter{
		q: []int{},
	}
}
func (this *RecentCounter) Ping(t int) int {
	this.q = append(this.q, t)
	for this.q[0] < t - 3000 {
		this.q = this.q[1:]
	}
	return len(this.q)
}
```
