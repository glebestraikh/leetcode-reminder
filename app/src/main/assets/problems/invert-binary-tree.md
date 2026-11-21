---
number: 226
title: Invert Binary Tree
difficulty: EASY
topic: Trees & Backtracking
pattern: Tree
time: O(n)
space: O(h), h is the height of the tree
---

## Description

Given the root of a binary tree, mirror the tree — swap the left and right children of every node — and return the root.

Example 1:
Input: root = [4,2,7,1,3,6,9]
Output: [4,7,2,9,6,3,1]

Example 2:
Input: root = [2,1,3]
Output: [2,3,1]

Example 3:
Input: root = []
Output: []

Constraints:
- 0 ≤ number of nodes ≤ 100
- -100 ≤ Node.val ≤ 100

## Idea

Recursion: return an empty node as is, otherwise swap Left and Right and mirror both subtrees the same way. Each node is visited once, and the recursion depth equals the height of the tree.

## Solution

```go
func invertTree(root *TreeNode) *TreeNode {
	if root == nil {
		return nil
	}
	root.Left, root.Right = root.Right, root.Left
	root.Left = invertTree(root.Left)
	root.Right = invertTree(root.Right)
	return root
}
```
