---
number: 161
title: One Edit Distance
difficulty: MEDIUM
topic: Two Pointers
pattern: Two Pointers
time: O(m), m is the length of s
space: O(1)
---

## Description

Given strings s and t, return true if they are exactly one edit apart. An edit is inserting one character, deleting one character or replacing one character.

Example 1:
Input: s = "ab", t = "acb"
Output: true
Explanation: Insert 'c'

Example 2:
Input: s = "", t = ""
Output: false
Explanation: The strings are equal — zero edits, but exactly one is required

Example 3:
Input: s = "a", t = ""
Output: true

Constraints:
- 0 ≤ len(s), len(t) ≤ 10^4
- s and t consist of English letters and digits

## Idea

Make s the shorter string by swapping if needed. If t is longer by more than one character, return false. Walk to the first difference: with equal lengths, the rests after it must match (a replace); with different lengths, s[i:] must match t[i+1:] (an insert). If there is no difference, the answer is true only when t is exactly one character longer.

## Solution

```go
func isOneEditDistance(s, t string) bool {
	if len(s) > len(t) {
		s, t = t, s
	}
	if len(t) > len(s)+1 {
		return false
	}
	for i := range s {
		if len(s) == len(t) && s[i] != t[i] {
			return s[i+1:] == t[i+1:]
		}
		if len(s) != len(t) && s[i] != t[i] {
			return s[i:] == t[i+1:]
		}
	}
	return len(t) == len(s)+1
}
```
