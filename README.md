# Price Comparator Market

A Spring Boot application for comparing product prices, tracking discounts, managing price alerts, and optimizing shopping baskets across multiple stores.

---

## Project Structure

The project follows a standard Spring Boot modular structure to promote clarity, maintainability, and scalability.

Controllers handle HTTP requests and responses.
Services contain business logic and orchestrate data flow.
Repositories abstract database access using Spring Data JPA.
Models represent database entities.
DTOs (Data Transfer Objects) are used for API payloads, ensuring separation between internal models and external representations.
Configuration and exception packages provide application setup and error handling.
Resources include configuration files and sample data for easy setup and testing.

---

## Build & Run Instructions

### **Prerequisites**
- Java 17+
- Gradle (or use the Gradle wrapper)
- PostgreSQL or H2 (configure in `application.yml`)

### **Build**
```sh
./gradlew build
```

### **Run**
```sh
./gradlew bootRun
```
or
```sh
java -jar build/libs/price-comparator-market-*.jar
```

### **Database Setup**
- By default, the app uses the configuration in `src/main/resources/application.yml`.
- On startup, sample data is imported from CSV files in `src/main/resources/csv/`.

---

## Assumptions & Simplifications

- **Product and Discount Data**: Loaded from CSV files at startup for demo purposes.
- **Discounts**: Only one discount per product per store is active at a time; overlapping discounts are merged by max percentage.
- **Price Alerts**: Checked by a scheduled job (default: once per day).
- **Authentication**: Not implemented (all endpoints are open).
- **Notifications**: Alerts are logged, not emailed.

---

## API Endpoints & Example Requests

Below are example endpoints for the main features:

- **Basket Optimization**  
  ```
  POST http://localhost:8080/api/basket/optimize
  ```
  Optimize your shopping basket for the best price across stores.

- **Best Discounts**  
  ```
  GET http://localhost:8080/api/discounts/best
  ```
  Retrieve the best available discounts.

- **New Discounts**  
  ```
  GET http://localhost:8080/api/discounts/new
  ```
  Get newly added discounts.

- **Product Price History**  
  ```
  GET http://localhost:8080/api/price-history?productName=lapte zuzu
  ```
  View the price history for a specific product.

- **Product Substitutes & Recommendations**  
  ```
  GET http://localhost:8080/api/products/substitutes?productName=iaurt grecesc
  ```
  Find substitutes and compare the value per unit for a product.

- **Custom Price Alerts**  
  ```
  POST http://localhost:8080/api/alerts
  ```
  Set a target price alert for a product.

---

## Improvements && Ideas

- **Testing**: Unit tests should be added.
- **Pagination**: Pagination should be added for all getAll type requests, to limit the number of items we get from the requests.
- **Alert**: I chose to use a cron job because it is easier to implement and maintain, while no extra infrastructure was needed, and it's a good way to implement the feature for a small-scale project. It can be less efficient for multiple alerts/products and is not instant, but as the app works with big stores, I thought there shouldn't be frequent changes in prices or discounts. For a more scalable or larger system, or if we are looking for real-time processing we could use Kafka.
- **Price History**: As we are working with the history of prices, we could be looking to cache the response when a request has the same date.
- **Currency Usage**: I didn't use the currency field so far, but a currency converter and adding different currencies for testing would be ideal.

---
