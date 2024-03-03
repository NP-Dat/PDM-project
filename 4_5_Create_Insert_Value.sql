CREATE DATABASE FoodDelivery;
GO

USE FoodDelivery;
GO

CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
	Fullname VARCHAR(255),
    UserName VARCHAR(255) unique,
    UserAddress VARCHAR(255) NOT NULL,
	UserNumber VARCHAR(20) NOT NULL,
	UserPassword VARCHAR(255)NOT NULL
);

CREATE TABLE Restaurant (
    RestaurantID INT IDENTITY(1,1) PRIMARY KEY ,
	RestaurantUsername VARCHAR(255) unique,
    RestaurantName VARCHAR(255) NOT NULL,
    RestaurantAddress VARCHAR(255) NOT NULL,
	RestaurantNumber VARCHAR(20) NOT NULL,
	RestaurantPassword VARCHAR(255) NOT NULL,
);

CREATE TABLE Payment (
    PaymentID INT IDENTITY(1,1)PRIMARY KEY,
    OrderID INT ,
    PaymentMethod VARCHAR(20) NOT NULL,
	PaymentStatus VARCHAR(20) NOT NULL,
	PaymentTime VARCHAR(255) NOT NULL,
	Amount FLOAT NOT NULL
);

CREATE TABLE MenuItem (
    FoodID INT IDENTITY(1,1) PRIMARY KEY,
    FoodName VARCHAR(255) NOT NULL,
	Price FLOAT NOT NULL,
    RestaurantID INT NOT NULL
);

CREATE TABLE DeliveryPerson (
    DeliveryID INT IDENTITY(1,1) PRIMARY KEY,
    DName VARCHAR(255) NOT NULL,
    Number VARCHAR(20) NOT NULL,
    CurrentLocation VARCHAR(255) NOT NULL,
	PersonStatus VARCHAR(10) NOT NULL
);

CREATE TABLE SelectedItem (
    SelectID INT IDENTITY(1,1) PRIMARY KEY,
    FoodID INT NOT NULL,
    Quantity INT NOT NULL,
    CartID INT NOT NULL
);

CREATE TABLE Cart (
    CartID INT IDENTITY(1,1) PRIMARY KEY,
    TotalCost FLOAT);


CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    DeliveryPersonID INT ,
    CartID INT NOT NULL,
    UserID INT NOT NULL,
    RestaurantID INT ,
	DateAndHour VARCHAR(255) ,
	CurrentStatus VARCHAR(20) ,
    DeliveryTime VARCHAR(255) 
);



ALTER TABLE Orders
ADD FOREIGN KEY (DeliveryPersonID) REFERENCES DeliveryPerson(DeliveryID);

ALTER TABLE Orders
ADD FOREIGN KEY (CartID) REFERENCES Cart(CartID);

ALTER TABLE Orders
ADD FOREIGN KEY (UserID) REFERENCES Users(UserID);

ALTER TABLE Orders
ADD FOREIGN KEY (RestaurantID) REFERENCES Restaurant(RestaurantID);


ALTER TABLE Payment
ADD FOREIGN KEY (OrderID) REFERENCES Orders(OrderID);


ALTER TABLE MenuItem
ADD FOREIGN KEY (RestaurantID) REFERENCES Restaurant(RestaurantID);

ALTER TABLE SelectedItem
ADD FOREIGN KEY (FoodID) REFERENCES MenuItem(FoodID);

ALTER TABLE SelectedItem
ADD FOREIGN KEY (CartID) REFERENCES Cart(CartID);


INSERT INTO Users (Fullname, UserName, UserAddress, UserNumber, UserPassword)
VALUES 
('John Doe', 'johndoe', '123 Main St, Townsville', '1234567890', 'password123'),
('Jane Smith', 'janesmith', '789 Park Ave, Townsville', '0987654321', 'mypassword');

INSERT INTO Restaurant (RestaurantUsername, RestaurantName, RestaurantAddress, RestaurantNumber, RestaurantPassword)
VALUES 
('burgerplace', 'Burger Place', '456 Side St, Townsville', '0987654321', 'securepassword'),
('pizzacorner', 'Pizza Corner', '321 Hill Rd, Townsville', '1230987654', 'pizzapass');

INSERT INTO MenuItem (FoodName, Price, RestaurantID)
VALUES 
('Cheeseburger', 8, 1),
('Veggie Pizza', 10, 2);

INSERT INTO DeliveryPerson (DName, Number, CurrentLocation, PersonStatus)
VALUES 
('Alice Smith', '9876543210', 'Central Hub, Townsville', 'Available'),
('Bob Johnson', '8765432109', 'North Hub, Townsville', 'Available');

INSERT INTO Cart (TotalCost)
VALUES 
(16.00),
(20.00);

INSERT INTO SelectedItem (FoodID, Quantity, CartID)
VALUES 
(1, 2, 1),
(2, 2, 2);

INSERT INTO Orders (DeliveryPersonID, CartID, UserID, RestaurantID, DateAndHour, CurrentStatus, DeliveryTime)
VALUES 
(1, 1, 1, 1, '2023-12-05 12:00:00', 'Preparing', '2023-12-05 13:00:00'),
(2, 2, 2, 2, '2023-12-06 18:00:00', 'Delivered', '2023-12-06 19:00:00');

INSERT INTO Payment (OrderID, PaymentMethod, PaymentStatus, PaymentTime, Amount)
VALUES 
(1, 'Credit Card', 'Completed', '2023-12-05 11:50:00', 16.00),
(2, 'PayPal', 'Pending', '2023-12-06 17:50:00', 20.00);



select * from Cart
select * from Payment
select * from Orders
select * from Users
select *from Restaurant
select * from DeliveryPerson
select * from MenuItem