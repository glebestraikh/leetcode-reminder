---
number: 3
title: Longest Substring Without Repeating Characters
difficulty: MEDIUM
topic: Sliding Window
pattern: Sliding Window
time: O(n)
space: O(m), m is the number of distinct characters
---

## Description

Given a string s, find the length of the longest substring in which all characters are distinct.

Example 1:
Input: s = "abcabcbb"
Output: 3
Explanation: The substring is "abc"

Example 2:
Input: s = "bbbbb"
Output: 1
Explanation: The substring is "b"

Example 3:
Input: s = "pwwkew"
Output: 3
Explanation: The substring is "wke". "pwke" does not count: it is a subsequence, not a substring

Constraints:
- 0 ≤ len(s) ≤ 5 · 10^4
- s consists of English letters, digits, symbols and spaces

## Idea

A sliding window [l, r] plus a set of the characters inside it. Move r forward; while s[r] is already in the window, remove s[l] and move l. After that the window has no repeats, so update the maximum r − l + 1.

## Solution

```go
func lengthOfLongestSubstring(s string) int {
	charSet := make(map[byte]bool)
	l, res := 0, 0
	for r := 0; r < len(s); r++ {
		for charSet[s[r]] {
			delete(charSet, s[l])
			l++
		}
		charSet[s[r]] = true
		if r - l + 1 > res {
			res = r - l + 1
		}
	}
	return res
}
```
