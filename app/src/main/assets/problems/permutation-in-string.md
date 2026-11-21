---
number: 567
title: Permutation in String
difficulty: MEDIUM
topic: Sliding Window
pattern: Sliding Window
time: O(n)
space: O(1)
---

## Description

Given strings s1 and s2, return true if s2 contains a permutation of s1 — a substring with the same letters in any order.

Example 1:
Input: s1 = "ab", s2 = "eidbaooo"
Output: true
Explanation: s2 contains "ba"

Example 2:
Input: s1 = "ab", s2 = "eidboaoo"
Output: false

Constraints:
- 1 ≤ len(s1), len(s2) ≤ 10^4
- Both strings consist of lowercase English letters

## Idea

A window of length len(s1) slides over s2. Keep the letter counts of s1 and of the window, plus matches — how many of the 26 letters have equal counts. Sliding the window changes only two letters, so matches is updated in O(1). matches == 26 means a permutation is found.

## Solution

```go
func checkInclusion(s1 string, s2 string) bool {
	if len(s1) > len(s2) {
		return false
	}
	s1Count := [26]int{}
	s2Count := [26]int{}
	for i := range s1 {
		s1Count[s1[i]-'a'] += 1
		s2Count[s2[i]-'a'] += 1
	}
	matches := 0
	for i := range s1Count {
		if s1Count[i] == s2Count[i] {
			matches += 1
		}
	}
	l := 0
	for i := len(s1); i < len(s2); i++ {
		if matches == 26 {
			return true
		}
		s2Count[s2[i]-'a'] += 1
		if s2Count[s2[i]-'a'] == s1Count[s2[i]-'a'] {
			matches += 1
		} else if s2Count[s2[i]-'a'] == s1Count[s2[i]-'a'] + 1 {
			matches -= 1
		}
		s2Count[s2[l]-'a'] -= 1
		if s2Count[s2[l]-'a'] == s1Count[s2[l]-'a'] {
			matches += 1
		} else if s2Count[s2[l]-'a'] == s1Count[s2[l]-'a'] - 1 {
			matches -= 1
		}
		l++
	}
	return matches == 26
}
```
