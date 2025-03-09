# Two-Tier-Java-Client-Server-Application-with-MySQL-and-JDBC

**Overview**

This project is a two-tier Java-based client-server application that interacts with a MySQL database using JDBC for connectivity. It consists of two applications:

**General Client Application – Allows users with varying permissions to execute SQL queries (DDL & DML) against a MySQL database.  
**Specialized Accountant Application – Provides a restricted view-only interface to monitor SQL transaction logs.  

**Key Features**

* User Authentication: Users must provide credentials that match predefined properties files before accessing databases.  
* JDBC Database Connectivity: Connects to MySQL using JDBC for executing SQL queries.  
* Permissions-Based Access: Different users have different levels of access to databases and commands.  
* GUI Interface: A Java Swing-based front-end for executing SQL commands and displaying results.  
* Transaction Logging: Logs the number of queries and updates executed by each user in a separate operations log database.  
* Multiple Concurrent Connections: Allows multiple instances of the application to run and connect to the MySQL database simultaneously.  
* SQL Execution Tracking: The specialized accountant application queries and displays transaction logs.  
* Graceful Connection Management: Users can disconnect and reconnect without restarting the application.  

**How to Run**

1. To run this project, execute the following SQL scripts in MySQL Workbench or command line:

* bikedb.sql - Creates and populates the bikedb database.  
* project3dbscript.sql – Creates and populates the project3 database.  
* project3operationslog.sql – Sets up the operationslog database for tracking user transactions.  
* clientCreationScriptProject3.sql – Creates client users (client1, client2, project3app, theaccountant).  
* clientPermissionsScriptProject3.sql – Assigns necessary permissions to each user.

Ensure MySQL is running and set up with the required databases and users.

*Users & Permissions*

* User	Database	Permissions  
* root	project3, bikedb	Full Access  
* client1	project3, bikedb	SELECT  
* client2	project3, bikedb	SELECT, UPDATE  
* project3app	operationslog	SELECT, INSERT, UPDATE  
* theaccountant	operationslog	SELECT  

2. Compile & Run Java Files

Update the properties files with correct database connection details!  

Use the following commands to compile files:  

* javac SQLClientApplicationFall2024.java  
* javac SQLAccountantApplicationFall2024.java  
* Javac ResultSetTableModel.java  

Use the following commands to run the files:  

* java SQLClientApplicationFall2024  
* java SQLAccountantApplicationFall2024  

3. Use the GUI to enter credentials, execute SQL commands, and view transaction logs.
