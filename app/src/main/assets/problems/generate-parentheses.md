---
number: 22
title: Generate Parentheses
difficulty: MEDIUM
topic: Trees & Backtracking
pattern: Backtracking
time: O(4^n / √n)
space: O(n)
---

## Description

Given n, generate all valid sequences of n pairs of round brackets.

Example 1:
Input: n = 3
Output: ["((()))","(()())","(())()","()(())","()()()"]

Example 2:
Input: n = 1
Output: ["()"]

Constraints:
- 1 ≤ n ≤ 8

## Idea

Encode '(' as +1 and ')' as −1: a sequence is valid when every prefix sum is ≥ 0 and the total is 0. That gives backtracking: add an opening bracket while there are fewer than n of them, add a closing bracket only while there are fewer closing than opening ones. Once 2n characters are placed, record the answer.

## Solution

```go
func generateParenthesis(n int) []string {
	res := []string{}
	stack := []string{}
	var backtracking func(openN, closeN int)
	backtracking = func(openN, closeN int) {
		if len(stack) == 2*n {
			res = append(res, strings.Join(stack, ""))
			return
		}
		if openN < n {
			stack = append(stack, "(")
			backtracking(openN+1, closeN)
			stack = stack[:len(stack)-1]
		}
		if closeN < openN {
			stack = append(stack, ")")
			backtracking(openN, closeN+1)
			stack = stack[:len(stack)-1]
		}
	}
	backtracking(0, 0)
	return res
}
```
