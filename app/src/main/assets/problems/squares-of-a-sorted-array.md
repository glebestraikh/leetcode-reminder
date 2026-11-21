---
number: 977
title: Squares of a Sorted Array
difficulty: EASY
topic: Two Pointers
pattern: Two Pointers
time: O(n)
space: O(1) excluding the answer
---

## Description

Given an array nums sorted in non-decreasing order, return an array of the squares of its elements, also sorted in non-decreasing order.

Example 1:
Input: nums = [-4,-1,0,3,10]
Output: [0,1,9,16,100]

Example 2:
Input: nums = [-7,-3,2,3,11]
Output: [4,9,9,49,121]

Constraints:
- 1 ≤ len(nums) ≤ 10^4
- -10^4 ≤ nums[i] ≤ 10^4
- nums is sorted in non-decreasing order

## Idea

The largest square is always at one of the ends of the array. Two pointers from both ends: compare the squares, write the larger one to the end of the result and move that pointer. The answer is filled from right to left.

## Solution

```go
func sortedSquares(nums []int) []int {
	l := 0
	r := len(nums)-1
	out := make([]int, len(nums))
	for i := len(nums)-1; i >= 0; i-- {
		leftSq := nums[l] * nums[l]
		rightSq := nums[r] * nums[r]
		if leftSq >= rightSq {
			out[i] = leftSq
			l++
		} else {
			out[i] = rightSq
			r--
		}
	}
	return out
}
```
