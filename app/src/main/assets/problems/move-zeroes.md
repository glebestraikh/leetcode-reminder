---
number: 283
title: Move Zeroes
difficulty: EASY
topic: Two Pointers
pattern: Two Pointers
time: O(n)
space: O(1)
---

## Description

Given an array nums, move all zeros to the end while keeping the order of the non-zero elements. It must be done in place, without copying the array.

Example 1:
Input: nums = [0,1,0,3,12]
Output: [1,3,12,0,0]

Example 2:
Input: nums = [0]
Output: [0]

Constraints:
- 1 ≤ len(nums) ≤ 10^4
- -2^31 ≤ nums[i] ≤ 2^31 - 1

## Idea

Pointer l is the slot for the next non-zero element. Walk the array: on a non-zero number, swap it with nums[l] and move l. The zeros end up at the back by themselves.

## Solution

```go
func moveZeroes(nums []int) {
	l := 0
	for i, v := range nums {
		if v != 0 {
			nums[l], nums[i] = nums[i], nums[l]
			l++
		}
	}
}
```
