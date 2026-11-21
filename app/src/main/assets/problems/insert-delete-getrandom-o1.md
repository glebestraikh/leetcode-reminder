---
number: 380
title: Insert Delete GetRandom O(1)
difficulty: MEDIUM
topic: Design
pattern: Design
time: O(1) per operation
space: O(n)
---

## Description

Implement RandomizedSet. Insert(val) adds the element if it is missing and returns true, otherwise false. Remove(val) deletes the element if it is present and returns true, otherwise false. GetRandom() returns a random element, each with equal probability. Every operation must run in O(1) on average.

Example 1:
Input: Insert(1), Remove(2), Insert(2), GetRandom(), Remove(1), Insert(2), GetRandom()
Output: true, false, true, 1 or 2, true, false, 2

Constraints:
- -2^31 ≤ val ≤ 2^31 - 1
- At most 2 · 10^5 calls
- The set has at least one element when GetRandom is called

## Idea

Store the elements in a slice so GetRandom just picks a random index. A map from value to its index in the slice gives O(1) checks and lookups. To delete in O(1), move the last element into the removed one's place, update its index in the map and cut the tail of the slice.

## Solution

```go
type RandomizedSet struct {
	nums    []int
	numsMap map[int]int
}
func Constructor() RandomizedSet {
	return RandomizedSet{
		nums:    []int{},
		numsMap: map[int]int{},
	}
}
func (rs *RandomizedSet) Insert(val int) bool {
	if _, exists := rs.numsMap[val]; exists {
		return false
	}
	rs.nums = append(rs.nums, val)
	rs.numsMap[val] = len(rs.nums)-1
	return true
}
func (rs *RandomizedSet) Remove(val int) bool {
	if _, ok := rs.numsMap[val]; !ok {
		return false
	}
	idx := rs.numsMap[val]
	last := rs.nums[len(rs.nums)-1]
	rs.numsMap[last] = idx
	rs.nums[idx] = last
	rs.nums = rs.nums[:len(rs.nums)-1]
	delete(rs.numsMap, val)
	return true
}
func (rs *RandomizedSet) GetRandom() int {
	return rs.nums[rand.Intn(len(rs.nums))]
}
```
