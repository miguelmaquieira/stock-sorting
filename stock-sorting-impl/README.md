# 🧠 stock-sorting-impl

This module contains the **implementation** of the Stock Sorting API. It handles the logic for sorting products based on weighted **sales** and **stock ratios** across sizes (`S`, `M`, `L`). The algorithm is designed to be flexible and extensible.

---

## 📊 Sorting Algorithm

The core of the module is the `DefaultProductSortingAlgorithm`, which computes a score for each product using weighted rules:

### ➗ Scoring Formula

```yaml
final_score = sales_score * weight_sales + stock_score * weight_stock
```
- `sales_score` = normalized units sold
- `stock_score` = normalized total stock units across sizes
- `weight_sales` and `weight_stock` must sum to **1.0**

Both components are normalized to ensure fair weighting between different product scales.

---

## 🔍 Example

Given:
- Product A: 100 units sold, stock {S:10, M:5, L:5}
- Product B: 50 units sold, stock {S:35, M:9, L:9}

And weights:
```json
{
  "sales": 0.6,
  "stock": 0.4
}
```

The service computes scores as:

* sales_score_A = 1.0        (max = 100)
* stock_score_A = 20/74 ≈ 0.27
* final_score_A = 1.0 * 0.6 + 0.27 * 0.4 = 0.708

