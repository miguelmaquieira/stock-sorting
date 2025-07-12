# 🧮 Stock Sorting API

This project provides a RESTful API for sorting a list of products based on dynamic, weighted criteria such as **sales units** and **stock levels per size**. It is designed as a multi-module **Spring Boot** application with OpenAPI specification and Docker support.

---

## 📦 Modules

- **[`stock-sorting-api`](./stock-sorting-api/)** – Contains the OpenAPI 3.0 specification.
- **[`stock-sorting-impl`](./stock-sorting-impl/)** – Implements the service logic and exposes the API endpoints.
- **Postman collection** available at [`product_sorting.dev.test_cases.postman_collection.json`](./product_sorting.dev.test_cases.postman_collection.json)

---

## 🌐 Live Deployment

The application is deployed and publicly accessible via **Render**:

- 🔗 **Base URL**: [https://stock-sorting.onrender.com](https://stock-sorting.onrender.com)
- 📄 **Swagger UI**: [https://stock-sorting.onrender.com/swagger-ui.html](https://stock-sorting.onrender.com/swagger-ui.html)

You can interact with the API directly from the Swagger documentation, or import the Postman collection into your workspace.

---

## 🚀 Getting Started Locally

To build and run the application locally using Docker:

```bash
docker build -t stock-sorting-app .
docker run -p 8080:8080 stock-sorting-app
```

The app will be available at http://localhost:8080, and Swagger UI at http://localhost:8080/swagger-ui.html.

## 🧪 Testing

Run tests with:
```
mvn clean install -Djib.skip=false
```

To run integration tests with initial data:
* Uses H2 in-memory DB
* Loads test-data.sql automatically for dev/test profiles