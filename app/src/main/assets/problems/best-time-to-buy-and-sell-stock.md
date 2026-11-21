---
number: 121
title: Best Time to Buy and Sell Stock
difficulty: EASY
topic: Sliding Window
pattern: Sliding Window
time: O(n)
space: O(1)
---

## Description

Given an array prices where prices[i] is the stock price on day i, buy once and sell once on a later day. Return the maximum profit, or 0 if no profit is possible.

Example 1:
Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy at 1 (day 1), sell at 6 (day 4)

Example 2:
Input: prices = [7,6,4,3,1]
Output: 0
Explanation: The price only goes down, so no trade is profitable

Constraints:
- 1 ≤ len(prices) ≤ 10^5
- 0 ≤ prices[i] ≤ 10^4

## Idea

Pointer l is the buy day, r is the sell day. If prices[r] is higher than prices[l], compute the profit and update the maximum. Otherwise a cheaper day was found, so move the buy day: l = r.

## Solution

```go
func maxProfit(prices []int) int {
	l := 0
	maxP := 0
	for r := 1; r < len(prices); r++ {
		if prices[r] > prices[l] {
			profit := prices[r] - prices[l]
			if profit > maxP {
				maxP = profit
			}
		} else {
			l = r
		}
	}
	return maxP
}
```
