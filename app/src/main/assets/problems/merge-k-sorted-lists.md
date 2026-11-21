---
number: 23
title: Merge k Sorted Lists
difficulty: HARD
topic: Linked List
pattern: Linked List
time: O(n log k)
space: O(k)
---

## Description

Given an array of k linked lists, each sorted in ascending order, merge them into one sorted list and return its head.

Example 1:
Input: lists = [[1,4,5],[1,3,4],[2,6]]
Output: [1,1,2,3,4,4,5,6]

Example 2:
Input: lists = []
Output: []

Example 3:
Input: lists = [[]]
Output: []

Constraints:
- 0 ≤ k ≤ 10^4
- 0 ≤ length of each list ≤ 500
- -10^4 ≤ node values ≤ 10^4
- At most 10^4 nodes in total

## Idea

Merge the lists in pairs, like in merge sort: each round turns k lists into k/2. There are log k rounds, and each round touches all n nodes. Two lists are merged using a dummy head.

## Solution

```go
func mergeKLists(lists []*ListNode) *ListNode {
	if len(lists) == 0 {
		return nil
	}
	for len(lists) > 1 {
		var mergedLists []*ListNode
		for i := 0; i < len(lists); i += 2 {
			l1 := lists[i]
			var l2 *ListNode
			if i + 1 < len(lists) {
				l2 = lists[i+1]
			}
			mergedLists = append(mergedLists, mergeLists(l1, l2))
		}
		lists = mergedLists
	}
	return lists[0]
}
func mergeLists(l1, l2 *ListNode) *ListNode {
	dummy := &ListNode{}
	tail := dummy
	for l1 != nil && l2 != nil {
		if l1.Val < l2.Val {
			tail.Next = l1
			l1 = l1.Next
		} else {
			tail.Next = l2
			l2 = l2.Next
		}
		tail = tail.Next
	}
	if l1 != nil {
		tail.Next = l1
	} else {
		tail.Next = l2
	}
	return dummy.Next
}
```
