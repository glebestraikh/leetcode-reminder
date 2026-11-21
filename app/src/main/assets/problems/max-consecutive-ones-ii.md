---
number: 487
title: Max Consecutive Ones II
difficulty: MEDIUM
topic: Sliding Window
pattern: Sliding Window
time: O(n)
space: O(1)
---

## Description

Given a binary array nums, return the maximum number of consecutive ones if you can flip at most one zero into a one.

Example 1:
Input: nums = [1,0,1,1,0]
Output: 4
Explanation: Flip the first zero to get [1,1,1,1,0]

Example 2:
Input: nums = [1,0,1,1,0,1]
Output: 4

Constraints:
- 1 ≤ len(nums) ≤ 10^5
- nums[i] is 0 or 1

## Idea

A window with at most one zero that never shrinks: when there are more than one zeros, the left edge moves one step together with the right edge. That is why the final window size len(nums) − l equals the best one found.

## Solution

```go
func findMaxConsecutiveOnes(nums []int) int {
	zeroCount := 0
	l := 0
	for _, x := range nums {
		if x == 0 {
			zeroCount += 1
		}
		if zeroCount > 1 {
			if nums[l] == 0 {
				zeroCount -= 1
			}
			l++
		}
	}
	return len(nums) - l
}
```
