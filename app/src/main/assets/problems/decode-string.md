---
number: 394
title: Decode String
difficulty: MEDIUM
topic: Stack & Queue
pattern: Stack
time: O(n + N^2)
space: O(n + N), n is the input length, N is the output length
---

## Description

Given an encoded string, decode it. The encoding rule is k[encoded]: the string encoded is repeated exactly k times. Encodings can be nested. Return the decoded string.

Example 1:
Input: s = "3[a]2[bc]"
Output: "aaabcbc"

Example 2:
Input: s = "3[a2[c]]"
Output: "accaccacc"

Example 3:
Input: s = "2[abc]3[cd]ef"
Output: "abcabccdcdcdef"

Constraints:
- 1 ≤ len(s) ≤ 30
- s consists of lowercase English letters, digits and brackets []
- 1 ≤ k ≤ 300
- The input is always valid

## Idea

Push characters onto a stack until ']' appears. Then pop the substring up to '[', pop the '[' itself and the digits before it — that is k. Repeat the substring k times and push the result back: this way nested brackets are expanded too. At the end, join the stack into a string.

## Solution

```go
func decodeString(s string) string {
	stack := []string{}
	for _, v := range s {
		if v != ']' {
			stack = append(stack, string(v))
		} else {
			substr := ""
			for stack[len(stack)-1] != "[" {
				substr = stack[len(stack)-1] + substr
				stack = stack[:len(stack)-1]
			}
			stack = stack[:len(stack)-1]
			k := ""
			for len(stack) > 0 && stack[len(stack)-1][0] >= '0' && stack[len(stack)-1][0] <= '9' {
				k = stack[len(stack)-1] + k
				stack = stack[:len(stack)-1]
			}
			count, _ := strconv.Atoi(k)
			repeated := strings.Repeat(substr, count)
			stack = append(stack, repeated)
		}
	}
	return strings.Join(stack, "")
}
```
