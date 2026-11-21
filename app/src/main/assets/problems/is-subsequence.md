---
number: 392
title: Is Subsequence
difficulty: EASY
topic: Two Pointers
pattern: Two Pointers
time: O(n), n is the length of t
space: O(1)
---

## Description

Given strings s and t, return true if s is a subsequence of t, meaning s can be obtained from t by deleting some characters without changing the order of the rest.

Example 1:
Input: s = "abc", t = "ahbgdc"
Output: true

Example 2:
Input: s = "axc", t = "ahbgdc"
Output: false

Constraints:
- 0 ≤ len(s) ≤ 100
- 0 ≤ len(t) ≤ 10^4
- Both strings consist of lowercase English letters

## Idea

Two pointers: i over s, j over t. Walk t; when the characters match, advance i as well. If i reaches the end of s, all its characters were found in t in order.

## Solution

```go
func isSubsequence(s string, t string) bool {
	i, j := 0, 0
	for i < len(s) && j < len(t) {
		if s[i] == t[j] {
			i++
		}
		j++
	}
	return i == len(s)
}
```
