---
number: 125
title: Valid Palindrome
difficulty: EASY
topic: Two Pointers
pattern: Two Pointers
time: O(n)
space: O(n), the string is copied into []rune
---

## Description

A string is a palindrome if, after converting letters to lowercase and removing everything except letters and digits, it reads the same forwards and backwards. Given a string s, return true if it is a palindrome.

Example 1:
Input: s = "A man, a plan, a canal: Panama"
Output: true
Explanation: "amanaplanacanalpanama" is a palindrome

Example 2:
Input: s = "race a car"
Output: false

Example 3:
Input: s = " "
Output: true
Explanation: After removing non-alphanumeric characters the string is empty, and an empty string is a palindrome

Constraints:
- 1 ≤ len(s) ≤ 2 · 10^5
- s consists of printable ASCII characters

## Idea

Two pointers from both ends. Skip anything that is not a letter or a digit, compare the characters ignoring case and move the pointers towards each other. The first mismatch means it is not a palindrome.

## Solution

```go
func isPalindrome(s string) bool {
	runes := []rune(s)
	l, r := 0, len(runes)-1
	for l < r {
		for l < r && !isAlphaNum(runes[l]) {
			l++
		}
		for l < r && !isAlphaNum(runes[r]) {
			r--
		}
		if unicode.ToLower(runes[l]) != unicode.ToLower(runes[r]) {
			return false
		}
		l++
		r--
	}
	return true
}
func isAlphaNum(r rune) bool {
	if unicode.IsDigit(r) || unicode.IsLetter(r) {
		return true
	}
	return false
}
```
