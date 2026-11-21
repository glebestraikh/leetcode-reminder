---
number: 20
title: Valid Parentheses
difficulty: EASY
topic: Stack & Queue
pattern: Stack
time: O(n)
space: O(n)
---

## Description

Given a string s made of the characters '(', ')', '[', ']', '{', '}', check whether the brackets are valid: every opening bracket is closed by a bracket of the same type and in the correct order.

Example 1:
Input: s = "()"
Output: true

Example 2:
Input: s = "()[]{}"
Output: true

Example 3:
Input: s = "(]"
Output: false

Example 4:
Input: s = "([])"
Output: true

Constraints:
- 1 ≤ len(s) ≤ 10^4
- s consists only of the brackets ()[]{}

## Idea

Push opening brackets onto a stack. For a closing bracket, take its matching opening bracket from the map and compare it with the top of the stack: if the stack is empty or they differ, the string is invalid. At the end the stack must be empty.

## Solution

```go
func isValid(s string) bool {
	closeToOpen := map[byte]byte{')': '(', ']': '[', '}': '{'}
	stack := []byte{}
	for i := range s {
		if v, ok := closeToOpen[s[i]]; ok {
			if len(stack) > 0 {
				prev := stack[len(stack)-1]
				stack = stack[:len(stack)-1]
				if prev != v {
					return false
				}
			} else {
				return false
			}
		} else {
			stack = append(stack, s[i])
		}
	}
	return len(stack) == 0
}
```
