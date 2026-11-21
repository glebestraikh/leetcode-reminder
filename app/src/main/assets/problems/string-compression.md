---
number: 443
title: String Compression
difficulty: MEDIUM
topic: Two Pointers
pattern: Two Pointers
time: O(n)
space: O(1)
---

## Description

Given an array of characters chars, compress it in place: replace every group of equal consecutive characters with the character itself and, if the group is longer than one, its length as digits. Return the new length of the array.

Example 1:
Input: chars = ["a","a","b","b","c","c","c"]
Output: 6, chars = ["a","2","b","2","c","3"]

Example 2:
Input: chars = ["a"]
Output: 1, chars = ["a"]

Example 3:
Input: chars = ["a","b","b","b","b","b","b","b","b","b","b","b","b"]
Output: 4, chars = ["a","b","1","2"]

Constraints:
- 1 ≤ len(chars) ≤ 2000
- chars[i] is an English letter, a digit or a symbol

## Idea

Two pointers: read scans the groups, write writes the result into the same array. For each group compute its length count, write the character and, if count > 1, the digits of the number. Writing never overtakes reading, so unread data is never overwritten.

## Solution

```go
func compress(chars []byte) int {
	write := 0
	read := 0
	for read < len(chars) {
		ch := chars[read]
		start := read
		for read < len(chars) && chars[read] == ch {
			read++
		}
		count := read - start
		chars[write] = ch
		write++
		if count > 1 {
			for _, digit := range []byte(strconv.Itoa(count)) {
				chars[write] = digit
				write++
			}
		}
	}
	return write
}
```
