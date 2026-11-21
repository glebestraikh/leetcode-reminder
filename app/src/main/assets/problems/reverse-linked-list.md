---
number: 206
title: Reverse Linked List
difficulty: EASY
topic: Linked List
pattern: Linked List
time: O(n)
space: O(1)
---

## Description

Given the head of a singly linked list, reverse the list and return the new head.

Example 1:
Input: head = [1,2,3,4,5]
Output: [5,4,3,2,1]

Example 2:
Input: head = [1,2]
Output: [2,1]

Example 3:
Input: head = []
Output: []

Constraints:
- 0 ≤ number of nodes ≤ 5000
- -5000 ≤ Node.val ≤ 5000

## Idea

Walk the list and re-point the links: remember the next node, point curr.Next to prev, then move prev and curr one step forward. When curr becomes nil, prev is the new head.

## Solution

```go
func reverseList(head *ListNode) *ListNode {
	var prev *ListNode
	curr := head
	for curr != nil {
		temp := curr.Next
		curr.Next = prev
		prev = curr
		curr = temp
	}
	return prev
}
```
