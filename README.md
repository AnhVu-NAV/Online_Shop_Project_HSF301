# Online Shop Project

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technologies Used](#technologies-used)
3. [Features](#features)
4. [Project Structure](#project-structure)
5. [Setup and Installation](#setup-and-installation)
6. [Usage](#usage)
7. [Screenshots](#screenshots)
8. [Future Enhancements](#future-enhancements)

---

## Project Overview
The **Online Shop Project** is a web-based e-commerce application that allows customers to browse products, manage a shopping cart, place orders, and view order history. Admins can manage products and orders, ensuring smooth operations of the online store.

---

## Technologies Used
- **Backend**: Java (Spring Framework, Spring MVC, Hibernate)
- **Frontend**: Thymeleaf, Bootstrap, HTML5, CSS3, JavaScript
- **Database**: MySQL
- **Tools**: Apache Tomcat, Maven, NetBeans IDE
- **Libraries**:
  - Hibernate for ORM
  - Thymeleaf for server-side rendering
  - Font Awesome and Owl Carousel for UI components

---

## Features
### Customer Features:
1. **User Registration and Login**: 
   - Register with unique username and associated role (default: Customer).
2. **Product Browsing**:
   - Filter products by price and brand.
   - Search products by keywords.
3. **Product Detail View**:
   - View detailed information about a product, including price, description, and available quantity.
4. **Shopping Cart Management**:
   - Add products to cart.
   - Update quantities in the cart.
   - Remove items from the cart.
5. **Checkout**:
   - Confirm and place orders.
   - Pay through integrated payment methods (e.g., VietQR).
6. **Order Management**:
   - View order history.
   - View detailed information for specific orders.

### Admin Features:
1. **Product Management**:
   - Add, update, and delete products.
2. **Order Management**:
   - View all orders and their statuses.

---

## Project Structure
```
src/main/java/com/onlineshop/
├── controller/          # Controllers for handling HTTP requests
├── dao/                 # Data Access Object (DAO) layer for database interactions
├── entity/              # JPA Entities for database mapping
├── service/             # Service layer for business logic
├── utils/               # Utility classes
└── config/              # Spring and Hibernate configuration files

src/main/resources/
├── templates/           # Thymeleaf templates
│   ├── fragments/       # Header, Footer, Sidebar
│   ├── customer/        # Customer-related views (cart, orders, etc.)
│   ├── admin/           # Admin-related views
│   └── login.html       # Login page
├── static/              # Static resources (CSS, JS, images)
└── application.properties # Database and server configuration
```

---

## Setup and Installation

### Prerequisites
- Java Development Kit (JDK) 11 or above
- Apache Maven
- MySQL Server
- Apache Tomcat
- IDE (e.g., NetBeans, IntelliJ IDEA, or Eclipse)

### Installation Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/online-shop.git
   cd online-shop
   ```

2. **Configure Database**:
   - Create a MySQL database named `onlineshop`.
   - Import the provided SQL file (`onlineshop.sql`) into the database.
   - Update `application.properties` with your database credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/onlineshop
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Deploy to Tomcat**:
   - Copy the WAR file from the `target/` directory to the `webapps/` folder of your Tomcat installation.
   - Start the Tomcat server.

5. **Access the Application**:
   - Visit `http://localhost:9998/Project_ASM_HSF301_G2/` in your browser.

---

## Usage
- **Customer**:
  - Navigate to `/customer` for shopping.
  - Register and log in to manage your orders.
- **Admin**:
  - Access `/admin` to manage products and orders.

---

## Screenshots
### Home Page
![Home Page](screenshots/home-page.png)

### Product Details
![Product Details](screenshots/product-details.png)

### Shopping Cart
![Shopping Cart](screenshots/cart-page.png)

---

## Future Enhancements
1. Add product reviews and ratings.
2. Integrate a secure payment gateway.
3. Add user profile management for customers.
4. Implement inventory alerts for low stock.
5. Introduce discount codes and promotions.

---

## Contributors
- **AnhVuNAV** (Lead Developer)
- [Your Name] (Contributor)
- [Your Name] (Contributor)

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
