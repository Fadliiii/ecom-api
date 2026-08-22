<img width="786" height="1054" alt="ecommerce - public" src="https://github.com/user-attachments/assets/cebe8da5-d192-43e9-b458-b752ffb6dad4" /># 🛒 E-Commerce RESTful API Backend

A robust and secure RESTful API built with **Java Spring Boot** for an E-Commerce platform. This backend service handles user authentication, product catalog management, shopping cart operations, and complex order processing logic.

## 🚀 Tech Stack
- **Language:** Java 21 
- **Framework:** Spring Boot, Spring Data JPA, Hibernate
- **Security:** Spring Security, JWT (JSON Web Tokens), Role-Based Access Control (RBAC)
- **Database:** MySQL
- **Data Mapping:** ModelMapper / DTO Pattern
- **Build Tool:** Maven

## ✨ Key Features
1. **Authentication & Authorization**
   - Stateless authentication using JWT stored in HttpOnly cookies (preventing XSS & CSRF).
   - Role-Based Access Control (Admin vs. User permissions).
   - Mitigation against IDOR (Insecure Direct Object Reference) vulnerabilities.

2. **Product & Catalog Management**
   - CRUD operations for products and categories.
   - Dynamic pricing management (Handling special prices and discounts).
   - Image upload and handling.

3. **Cart & Order Processing**
   - Advanced shopping cart logic: Managing `CartItem` state and dynamic totals.
   - Seamless transition from `CartItem` to `OrderItem` during checkout.
   - Real-time stock deduction and inventory validation.
   - Payment Gateway integration mock-up.


### Sample Endpoints:
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/register` | Register a new user | Public |
| POST | `/api/auth/login` | Authenticate user & return JWT | Public |
| GET | `/api/products` | Get all products with pagination | Public |
| POST | `/api/cart/add` | Add item to shopping cart | User |
| POST | `/api/orders/checkout`| Process cart to order | User |

## 🛠️ Getting Started
To run this project locally, follow these steps:

### Prerequisites
- Java Development Kit (JDK) 21
- MySQL Server
- Maven / Intellij

### Installation
1. Clone the repository:
   ```bash
   git clone [https://github.com/Fadliiii/Online-Shop.git](https://github.com/Fadliiii/Online-Shop.git)
