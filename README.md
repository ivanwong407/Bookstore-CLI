## Group Info:
Group:11
1155129246 Lee Siu Cheung
1155161947 Wong Po Wa

## How to run our code
run App.java file
the command line interface will appear in console

## Create Table command format (Noticed that if the table exist, you can't create same table again):
Please enter your choice??..1
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE CUSTOMERS (CUSTOMER_ID VARCHAR(30), CUSTOMER_NAME VARCHAR(30), SHIPPING_ADDRESS VARCHAR(100), CREDIT_CARD_NO VARCHAR(20), PRIMARY KEY (CUSTOMER_ID))
Table created successfully.

Please enter your choice??..1
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE ORDERS (ORDER_ID INTEGER, ORDER_DATE DATE, SHIPPING_STATUS CHAR(1), CHARGE FLOAT, CUSTOMER_ID VARCHAR(30), PRIMARY KEY (ORDER_ID), FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS(CUSTOMER_ID))
Table created successfully.

Please enter your choice??..1
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE BOOKS (ISBN VARCHAR(13), BOOK_TITLE VARCHAR(50), UNIT_PRICE FLOAT, COPIES_AVAILABLE INTEGER, PRIMARY KEY (ISBN))
Table created successfully.

Please enter your choice??..1
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE BOOK_ORDERED (ORDER_ID INTEGER, ISBN VARCHAR(13), QUANTITY INTEGER, PRIMARY KEY (ISBN, ORDER_ID), FOREIGN KEY (ISBN) REFERENCES BOOKS(ISBN))
Table created successfully.

Please enter your choice??..1
Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):
CREATE TABLE AUTHORS (ISBN VARCHAR(13), AUTHOR_NAME VARCHAR(30), PRIMARY KEY (ISBN, AUTHOR_NAME), FOREIGN KEY (ISBN) REFERENCES BOOKS(ISBN))
Table created successfully.

## The data insertion: (Noticed that only the data files inside test_data folder are able to insert)
"your path"/our_porject/test_data"


