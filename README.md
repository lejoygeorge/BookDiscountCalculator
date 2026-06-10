
# 📚 Book Discount Calculator API

A robust Spring Boot RESTful API designed to calculate optimal shopping cart prices and discounts for book orders. Built with a focus on code quality, the application utilizes OpenAPI code generation, strict Jakarta validation, and comprehensive global exception handling.

## ✨ Key Features

* **Smart Discount Calculation:** Core business logic that applies progressive discounts based on the grouping of different books.
* **Contract-First API:** Data Transfer Objects (DTOs) and models (`BookItem`, `BillDetails`) are automatically generated from an OpenAPI/Swagger YAML specification.
* **Strict Validation:** Auto-injected `@NotNull` and `@Min(0)` constraints ensure payload integrity.
* **Global Exception Handling:** A centralized `@RestControllerAdvice` elegantly catches Jackson deserialization errors (invalid enums) and constraint violations, returning clean `400 Bad Request` responses.
* **High Code Quality:** Resolved SonarQube vulnerabilities (e.g., utilizing `Collections.unmodifiableMap` for thread-safe static pricing constants).
* **Test Driven:** Thoroughly tested web layer using `@WebMvcTest`, `MockMvc`, and JUnit 5 `@DisplayName` metrics.
* **Code Coverage:** Integrated JaCoCo reporting to enforce and visualize branch/instruction coverage.

## 🛠️ Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **API Specs:** OpenAPI / Swagger Codegen (Maven Plugin)
* **Testing:** JUnit 5, Mockito, Spring Boot Test (`MockMvc`)
* **Code Quality:** JaCoCo, SonarLint / SonarQube, PiTest

## 📖 Business Logic & Pricing Rules

The application calculates discounts based on unique groupings of book types. The base price of any single book is **50.00**.

| Unique Books in Group | Discount | Final Price per Book |
| :-------------------  | :------- |:---------------------|
| 1 Book                | 0%       | 50.00                |
| 2 Different Books     | 5%       | 47.50                |
| 3 Different Books     | 10%      | 45.00                |
| 4 Different Books     | 20%      | 40.00                |
| 5 Different Books     | 25%      | 37.50                |

*Note: The calculator automatically handles filtering out items submitted with a quantity of `0` before processing.*

## 🚀 API Documentation

### Get Discount Price
Calculates the final price and discount for a given basket of books.

**Endpoint:** `POST /api/orders/discount` *(Note: URL paths are configurable via application properties)*

**Request Header:**
`Content-Type: application/json`

**Request Body (`List<BookItem>`):**
```json
[
  {
    "book": "CLEAN_CODE",
    "quantity": 2
  },
  {
    "book": "THE_CLEAN_CODER",
    "quantity": 1
  }
]

```

**Success Response (`200 OK`):**

```json
{
  "originalPrice": 150.0,
  "finalPrice": 145.0,
  "discount": 5.0
}

```

**Error Handling (`400 Bad Request`):**
The API returns descriptive error messages for malformed inputs, such as:

* Invalid Enums: `"Invalid value 'ALIEN_BOOK' for field 'book'. Accepted values are: [CLEAN_CODE, THE_CLEAN_CODER, CLEAN_ARCHITECTURE, TDD_BY_EXAMPLE, WORKING_EFFECTIVELY_WITH_LEGACY_CODE]"`
* Negative Quantities: `"getDiscountPrice.bookBasket[0].quantity: must be greater than or equal to 0"`
* Empty Baskets: `"Book basket cannot be empty"`

## ⚙️ Setup and Installation

### 1. Clone the repository

```bash
git clone [https://github.com/lejoygeorge/BookDiscountCalculator.git](https://github.com/lejoygeorge/BookDiscountCalculator.git)
cd BookDiscountCalculator

```

### 2. Generate OpenAPI Models

Before running the application, ensure the models are generated from the YAML specification.

```bash
mvn clean compile

```

### 3. Run the Application

```bash
mvn spring-boot:run

```

## 🧪 Testing & Code Coverage

This project uses JaCoCo to ensure high code coverage. The generated models (`com.bookorder.discountcalculator.model.`) are excluded from the coverage report to ensure metrics strictly reflect business and controller logic.

To run the unit tests and generate the coverage report:

```bash
mvn clean test

```

Once the tests pass, view the interactive HTML report by opening:
`target/site/jacoco/index.html` in your web browser.

## 🧪 Mutation Testing (Pitest)
To evaluate the absolute quality and effectiveness of the test suite, this project utilizes Pitest for mutation testing. It intentionally injects flaws (mutants) into the compiled bytecode to ensure the unit tests actually fail when logic is compromised.

Note: Due to Java agent conflicts between JaCoCo and Pitest, you must explicitly skip JaCoCo when running the mutation analysis.

To execute the mutation tests:

```bash
mvn clean test-compile org.pitest:pitest-maven:mutationCoverage -Djacoco.skip=true

```
View the mutation coverage report to see killed and survived mutants by opening:
`target/pit-reports/index.html` in your web browser.
---

*Developed by Lejoy George*
