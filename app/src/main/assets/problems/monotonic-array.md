---
number: 896
title: Monotonic Array
difficulty: EASY
topic: Arrays & Hashing
pattern: Array
time: O(n)
space: O(1)
---

## Description

An array is monotonic if it is either non-decreasing or non-increasing. Given an array nums, return true if it is monotonic.

Example 1:
Input: nums = [1,2,2,3]
Output: true

Example 2:
Input: nums = [6,5,4,4]
Output: true

Example 3:
Input: nums = [1,3,2]
Output: false

Constraints:
- 1 ≤ len(nums) ≤ 10^5
- -10^5 ≤ nums[i] ≤ 10^5

## Idea

One pass: record whether an increase (asc) and a decrease (desc) were seen. If both appear, the array is not monotonic.

## Solution

```go
func isMonotonic(nums []int) bool {
	asc, desc := false, false
	for i := 1; i < len(nums); i++ {
		if nums[i-1] < nums[i] {
			asc = true
		} else if nums[i-1] > nums[i] {
			desc = true
		}
		if asc && desc {
			return false
		}
	}
	return true
}
```
