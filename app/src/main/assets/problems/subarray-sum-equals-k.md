---
number: 560
title: Subarray Sum Equals K
difficulty: MEDIUM
topic: Arrays & Hashing
pattern: Prefix Sum
time: O(n)
space: O(n)
---

## Description

Given an array of integers nums and an integer k, return the number of contiguous subarrays whose sum equals k.

Example 1:
Input: nums = [1,1,1], k = 2
Output: 2

Example 2:
Input: nums = [1,2,3], k = 3
Output: 2
Explanation: [1,2] and [3]

Constraints:
- 1 ≤ len(nums) ≤ 2 · 10^4
- -1000 ≤ nums[i] ≤ 1000
- -10^7 ≤ k ≤ 10^7

## Idea

The sum of nums[i+1..j] equals prefixSum[j] − prefixSum[i], so we look for pairs with prefixSum[i] = prefixSum[j] − k. Walk left to right with the running sum currSum and keep a map of how many times each prefix sum has appeared: at each step add the count of currSum − k to the answer. The initial {0: 1} is the empty prefix.

## Solution

```go
func subarraySum(nums []int, k int) int {
	currSum := 0
	seenCount := map[int]int{0: 1}
	res := 0
	for _, v := range nums {
		currSum += v
		diff := currSum - k
		res += seenCount[diff]
		seenCount[currSum] += 1
	}
	return res
}
```
