**Group Info:**
Group:11
1155129246 Lee Siu Cheung
1155161947 Wong Po Wa

**How to run our code**
run App.java file
the command line interface will appear in the console

**Create Table: (Noticed that you can't create the table that has already existed)**
Sequence to create tables:
BOOKS->AUTHORS->CUSTOMERS->ORDERS->BOOK_ORDERED

BOOKS:
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE BOOKS (ISBN VARCHAR(13), BOOK_TITLE VARCHAR(50), UNIT_PRICE FLOAT, COPIES_AVAILABLE INTEGER, PRIMARY KEY (ISBN))

AUTHORS:
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE AUTHORS (ISBN VARCHAR(13), AUTHOR_NAME VARCHAR(30), PRIMARY KEY (ISBN, AUTHOR_NAME), FOREIGN KEY (ISBN) REFERENCES BOOKS(ISBN))

CUSTOMERS:
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE CUSTOMERS (CUSTOMER_ID VARCHAR(30), CUSTOMER_NAME VARCHAR(30), SHIPPING_ADDRESS VARCHAR(100), CREDIT_CARD_NO VARCHAR(20), PRIMARY KEY (CUSTOMER_ID))

ORDERS:
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE ORDERS (ORDER_ID INTEGER, ORDER_DATE DATE, SHIPPING_STATUS CHAR(1), CHARGE FLOAT, CUSTOMER_ID VARCHAR(30), PRIMARY KEY (ORDER_ID), FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS(CUSTOMER_ID))

BOOK_ORDERED:
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE BOOK_ORDERED (ORDER_ID INTEGER, ISBN VARCHAR(13), QUANTITY INTEGER, PRIMARY KEY (ISBN, ORDER_ID), FOREIGN KEY (ISBN) REFERENCES BOOKS(ISBN))

**Delete Table sequence:**
BOOK_ORDERED->ORDERS->AUTHORS->CUSTOMERS->BOOKS

** The data insertion: (Noticed that only the data files inside the test_data folder are able to be inserted) **
C:\{your_path}\CSCI3170_test\src\test_data

**Please ensure the filenames are the same as in test_data:**
orders.txt
ordering.txt
customer.txt
book.txt
book_author.txt

**Successful Insertion will show:**

Data successfully inserted into BOOKS
Data successfully inserted into CUSTOMERS
Data successfully inserted into ORDERS
Data successfully inserted into BOOK_ORDERED
Data successfully inserted into AUTHORS Processing... Data is loaded!


