# Stock Sorting API

This module defines the API contracts for the **Stock Sorting** service.  
It provides the models and API specification for sorting a list of T-shirt products based on weighted criteria such as sales and stock.

---

## 📌 Purpose

The `stock-sorting-api` module contains:

- **Data models** for product, stock, and sorting request.
- **OpenAPI (or interface) definitions** for exposing the sorting endpoint.
- Shared contracts between the API consumer and the backend implementation.

This ensures a clear separation of concerns between API definitions and their implementation logic.

---

## 🚀 API Overview

### Endpoint
`POST /api/v1/products/sorted`  
Returns a list of products sorted by a weighted score.

### Request body
```json
{
  "weights": {
    "sales": 0.4,
    "stock": 0.6,
    "stock_size": {
      "S": 0.25,
      "M": 0.5,
      "L": 0.25
    }
  }
}
```

- `sales` (required): Weight for sales units (0-1)
- `stock` (required): Weight for stock levels (0-1)
- `stock_size` (optional): Weights for sizes S/M/L (values should sum to 1)

👉 The sum of `sales` + `stock` should equal `1.0`.

---

## ✅ Example response

```json
[
  {
    "id": 1,
    "name": "T-shirt A",
    "sales": 100,
    "stock": {
      "S": 10,
      "M": 5,
      "L": 0
    }
  },
  {
    "id": 2,
    "name": "T-shirt B",
    "sales": 50,
    "stock": {
      "S": 2,
      "M": 0,
      "L": 3
    }
  }
]
```

---

## 🛠 Build

To build the API module:
```bash
mvn clean install
```

---

## ⚠ Validation

The backend enforces:
- `sales + stock = 1.0`
- If `stock_size` provided → `S + M + L = 1.0`
- All values between `0.0` and `1.0`

---

## 💡 Design decision: Strongly typed weights vs. flexible map

This API uses a **strongly typed definition** for the `weights` parameter rather than a flexible map of weights.

### ✅ Why strongly typed?

- **API contract clarity**: Clients know exactly what keys (`sales`, `stock`, `stock_size`) are valid. This prevents accidental use of unsupported or misspelled fields.
- **Validation at API level**: OpenAPI enforces the structure, required fields, and value ranges automatically. Errors like invalid types or missing required weights are caught before reaching backend logic.
- **Better tooling support**: Auto-generated SDKs, Swagger UI, and API clients benefit from knowing the exact schema.
- **Simpler backend logic**: We can map requests directly to DTOs without dynamic inspection or custom key validation.

### ⚠ Why not a flexible map?

- Flexible maps (`additionalProperties: true`) are useful when criteria change frequently or need to be dynamic.
- However, they require custom validation, risk accepting unsupported keys, and make client integration harder.

---

## 📌 API Versioning

This API uses **URI-based versioning** to future-proof the interface and enable clean evolution of the contract.  
All endpoints are prefixed with the API version.

### Current version:
`v1`

### Example endpoint:
`POST /api/v1/products/sorted`

Versioning ensures that:
- Clients can safely upgrade to new API versions when ready.
- Breaking changes can be introduced in new versions without disrupting existing consumers.
- API documentation and tooling clearly indicate supported versions.

---

## 📎 Notes

This module is meant to be consumed by the implementation module (`stock-sorting-impl`), which contains the service logic and REST controllers.