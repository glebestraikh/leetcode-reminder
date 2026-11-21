---
number: 387
title: First Unique Character in a String
difficulty: EASY
topic: Arrays & Hashing
pattern: Counting
time: O(n)
space: O(1), the alphabet has 26 letters
---

## Description

Given a string s, find the first character that appears in it exactly once and return its index. If there is no such character, return -1.

Example 1:
Input: s = "leetcode"
Output: 0

Example 2:
Input: s = "loveleetcode"
Output: 2

Example 3:
Input: s = "aabb"
Output: -1

Constraints:
- 1 ≤ len(s) ≤ 10^5
- s consists of lowercase English letters

## Idea

First pass: count how many times each letter appears, in an array of 26. Second pass over the string: the first character with a count of 1 is the answer.

## Solution

```go
func firstUniqChar(s string) int {
	cnt := [26]int{}
	for i := range s {
		cnt[s[i] - 'a'] += 1
	}
	for i := range s {
		if cnt[s[i] - 'a'] == 1 {
			return i
		}
	}
	return -1
}
```
