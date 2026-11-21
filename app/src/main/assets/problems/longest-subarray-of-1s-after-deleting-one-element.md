---
number: 1493
title: Longest Subarray of 1's After Deleting One Element
difficulty: MEDIUM
topic: Sliding Window
pattern: Sliding Window
time: O(n)
space: O(1)
---

## Description

Given a binary array nums, you must delete exactly one element from it. Return the length of the longest non-empty subarray of only ones you can get, or 0 if there is none.

Example 1:
Input: nums = [1,1,0,1]
Output: 3
Explanation: Delete the zero to get [1,1,1]

Example 2:
Input: nums = [0,1,1,1,0,1,1,0,1]
Output: 5
Explanation: Delete the zero at index 4 to get [1,1,1,1,1]

Example 3:
Input: nums = [1,1,1]
Output: 2
Explanation: An element must be deleted even if it is a one

Constraints:
- 1 ≤ len(nums) ≤ 10^5
- nums[i] is 0 or 1

## Idea

A window with at most one zero that never shrinks: when there are more than one zeros, the left edge moves one step together with the right edge. nums[l] ^ 1 is 1 for a zero and 0 for a one, so the zero count drops only when a zero leaves the window. The answer is the window size minus the deleted element: len(nums) − l − 1.

## Solution

```go
func longestSubarray(nums []int) int {
	l := 0
	zeroCount := 0
	for i := range nums {
		if nums[i] != 1 {
			zeroCount++
		}
		if zeroCount > 1 {
			zeroCount -= nums[l] ^ 1
			l++
		}
	}
	return len(nums) - l - 1
}
```
