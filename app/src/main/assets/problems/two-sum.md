---
number: 1
title: Two Sum
difficulty: EASY
topic: Arrays & Hashing
pattern: Hash Map
time: O(n)
space: O(n)
---

## Description

Given an array of integers nums and an integer target, return the indices of the two different elements that add up to target. There is exactly one solution. The indices can be returned in any order.

Example 1:
Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: nums[0] + nums[1] = 2 + 7 = 9

Example 2:
Input: nums = [3,2,4], target = 6
Output: [1,2]

Constraints:
- 2 ≤ len(nums) ≤ 10^4
- -10^9 ≤ nums[i], target ≤ 10^9
- Exactly one valid answer exists

## Idea

One pass with a map from value to index. For each number v, look up its complement target − v: if it was seen before, we have the answer. Otherwise remember v and its index.

## Solution

```go
func twoSum(nums []int, target int) []int {
	prevMap := map[int]int{}
	for i, v := range nums {
		diff := target - v
		if idx, ok := prevMap[diff]; ok {
			return []int{idx, i}
		}
		prevMap[v] = i
	}
	return []int{}
}
```
