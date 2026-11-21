---
number: 21
title: Merge Two Sorted Lists
difficulty: EASY
topic: Linked List
pattern: Linked List
time: O(n + m)
space: O(1)
---

## Description

Given the heads of two linked lists list1 and list2 sorted in ascending order, merge them into one sorted list made of the same nodes and return its head.

Example 1:
Input: list1 = [1,2,4], list2 = [1,3,4]
Output: [1,1,2,3,4,4]

Example 2:
Input: list1 = [], list2 = []
Output: []

Example 3:
Input: list1 = [], list2 = [0]
Output: [0]

Constraints:
- 0 ≤ number of nodes in each list ≤ 50
- -100 ≤ Node.val ≤ 100
- Both lists are sorted in non-decreasing order

## Idea

Use a dummy head and a tail pointer to the end of the result. While both lists are non-empty, attach the smaller current node to tail and advance its list. At the end, attach the rest of the non-empty list as is.

## Solution

```go
func mergeTwoLists(list1 *ListNode, list2 *ListNode) *ListNode {
	dummy := &ListNode{}
	tail := dummy
	for list1 != nil && list2 != nil {
		if list1.Val < list2.Val {
			tail.Next = list1
			list1 = list1.Next
		} else {
			tail.Next = list2
			list2 = list2.Next
		}
		tail = tail.Next
	}
	if list1 != nil {
		tail.Next = list1
	} else {
		tail.Next = list2
	}
	return dummy.Next
}
```
