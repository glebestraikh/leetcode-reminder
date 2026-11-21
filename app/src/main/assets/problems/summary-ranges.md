---
number: 228
title: Summary Ranges
difficulty: EASY
topic: Arrays & Hashing
pattern: Array
time: O(n)
space: O(1) excluding the answer
---

## Description

Given a sorted array of unique integers nums, describe it with the shortest list of ranges: consecutive numbers a, a+1, …, b are written as "a->b", and a single number a as "a".

Example 1:
Input: nums = [0,1,2,4,5,7]
Output: ["0->2","4->5","7"]

Example 2:
Input: nums = [0,2,3,4,6,8,9]
Output: ["0","2->4","6","8->9"]

Constraints:
- 0 ≤ len(nums) ≤ 20
- -2^31 ≤ nums[i] ≤ 2^31 - 1
- All values are unique and sorted in ascending order

## Idea

Remember the start of a range and move i while the next number is exactly one more than the current one. If the range has a single number, write it, otherwise write "start->nums[i]". The outer loop continues from the number after the range.

## Solution

```go
func summaryRanges(nums []int) []string {
	ans := []string{}
	for i := 0; i < len(nums); i++ {
		start := nums[i]
		for i+1 < len(nums) && nums[i+1] == nums[i]+1 {
			i++
		}
		if nums[i] == start {
			ans = append(ans, strconv.Itoa(start))
		} else {
			ans = append(ans, fmt.Sprintf("%d->%d", start, nums[i]))
		}
	}
	return ans
}
```
