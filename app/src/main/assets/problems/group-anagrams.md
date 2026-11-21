---
number: 49
title: Group Anagrams
difficulty: MEDIUM
topic: Arrays & Hashing
pattern: Hash Map
time: O(n · m)
space: O(n · m)
---

## Description

Given an array of strings strs, group the anagrams together — strings made of the same letters in a different order. The groups can be returned in any order.

Example 1:
Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]

Example 2:
Input: strs = [""]
Output: [[""]]

Example 3:
Input: strs = ["a"]
Output: [["a"]]

Constraints:
- 1 ≤ len(strs) ≤ 10^4
- 0 ≤ len(strs[i]) ≤ 100
- strs[i] consists of lowercase English letters

## Idea

Anagrams have the same count of every letter. Count the letters of a string into a [26]int array — it becomes the group key, since an array can be a map key in Go. Put the string into the map under that key and collect all groups at the end.

## Solution

```go
func groupAnagrams(strs []string) [][]string {
	groups := map[[26]int][]string{}
	for _, s := range strs {
		count := [26]int{}
		for _, c := range s {
			count[c-'a']++
		}
		groups[count] = append(groups[count], s)
	}
	res := [][]string{}
	for _, group := range groups {
		res = append(res, group)
	}
	return res
}
```
