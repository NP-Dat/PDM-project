# README for the Java Swing Food Delivery Application

## Overview
This Java Swing-based Food Delivery Application provides a comprehensive suite of functionalities, encompassing user and restaurant management, order placement, and password reset capabilities. The application is divided into several packages, each handling specific aspects of the system.

## Packages and Their Components

### `user` Package
- Manages user-specific functionalities including login, signup, adding items to cart, viewing the cart, and processing payments.
- Classes like `UAddToCartPanel`, `UCartPanel`, `UPaymentPanel`, etc., facilitate these functionalities.

### `restaurant` Package
- Handles restaurant-side operations such as managing menus, processing orders, and restaurant authentication.
- Key classes include `RControlPanel`, `RHomepageFrame`, `RListMenuFrame`, `RListOrderFrame`, `RLoginPanel`, and `RSignupPanel`.

### `resetPassword` Package
- Provides interfaces for users and restaurants to reset their passwords.
- Includes `ResetPasHomePageFrame`, `ResetPasHomePagePanel`, `RResetPassFrame`, and `UResetPassFrame` for handling password reset processes.

### `orderFood` Package
- Facilitates order placement and food selection functionalities.
- Comprises classes like `AddToCartForm`, `FoodInfo`, `PlaceOrder`, and `PlaceOrderForm` to manage various aspects of food ordering and cart management.

### `action` Package
- Supports database connectivity, session management, and password hashing.
- Contains `ConnectDB`, `PasswordHash`, and `SessionData` classes.

## Functionalities

- **User Authentication**: Users and restaurants can log in and sign up using their credentials.
- **Menu Management**: Restaurants can view, add, and update their menu items.
- **Order Processing**: Users can browse menus, add items to their cart, and proceed to checkout.
- **Password Reset**: Both users and restaurants have the ability to reset their passwords.
- **Database Interaction**: The application connects to a SQL Server database for data retrieval and management.

## Requirements

- Java JDK 8 or later.
- SQL Server (for the database backend).
- Any Java IDE (i.e. JetBrains IntelliJ IDEA)
## Setup

1. **Database Configuration**: Set up your SQL Server by execute SQL file and update the connection string in `ConnectDB.java(action package)` as per your setup.
    The string should look like this: `jdbc:sqlserver://localhost:1433;databaseName=FoodDelivery;user=sa;password=quoctuan123;encrypt=false`
2. **Install jdbc driver**: You can use the newest jdbc version
3. **Compilation and Execution**: Compile the Java files using a Java compiler and run the `Main.java`( PDM_Project_Group_9\Official_Product\src) then choose the role.


## How to Use

### For Users
1. **Login/Signup**: Create a new account, then log in.
2. **Browsing and Ordering**: Browse restaurants, add food items to your cart, and checkout.
3. **View Cart**: View cart before check out.If you want to remove food from cart, click the "Remove" button on the right side for the item which you want to remove or "Remove All" button.Then press "Back to Menu" to refresh the cart.

### For Restaurants
1. **Login/Signup**: Create a new account, then log in.
2. **Menu Management**: Press "Menu",then "Refresh" to see the present Menu. Press "Add Dishes" to change to adding dishes panel. After adding dishes, please press "Menu",then "Refresh" to see the present Menu.
3. **Order Management**: Manage and assign incoming orders.

### Password Reset
1. **Access**: From the home page, select the password reset option.
2. **Resetting**: Choose user or restaurant role and reset the password. You have to remember the username to reset the password.  

### General Navigation
- Navigate back or cancel actions through provided buttons.
- Fill out all forms correctly before submission.

### Error Handling
- The application will notify of any errors or invalid inputs. Follow the instructions to rectify issues.

## Note
This application is suitable for demonstration. Implement secure password handling, robust error handling, and thorough testing for a production environment.