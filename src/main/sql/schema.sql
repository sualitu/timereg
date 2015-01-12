/*
Customers -* Projects -* Time Registrations *- Rate
      *      *
       \    /
        User

*/
DROP DATABASE IF EXISTS time_reg;
CREATE DATABASE time_reg;
USE time_reg;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS CUSTOMERS;
DROP TABLE IF EXISTS PROJECTS;
DROP TABLE IF EXISTS RATES;
DROP TABLE IF EXISTS REGISTRATIONS;


CREATE TABLE USERS (
name VARCHAR(255),
password VARCHAR(255),
RID int(11) NOT NULL auto_increment,
primary KEY (RID));

CREATE TABLE CUSTOMERS (
owner INT(12) NOT NULL,
name VARCHAR(255),
description TEXT,
RID int(11) NOT NULL auto_increment,
FOREIGN KEY (owner) REFERENCES USERS(RID),
primary KEY (RID));

CREATE TABLE PROJECTS (
customer INT(12) NOT NULL,
user INT(12) NOT NULL,
name VARCHAR(255),
description TEXT,
RID int(11) NOT NULL auto_increment,
FOREIGN KEY (user) REFERENCES USERS(RID),
FOREIGN KEY (customer) REFERENCES CUSTOMERS(RID),
primary KEY (RID));

CREATE TABLE RATES (
rate INT(12),
description TEXT,
RID int(11) NOT NULL auto_increment,
primary KEY (RID));

CREATE TABLE REGISTRATIONS (
duration INT(32),
project INT(12) NOT NULL,
rate INT(12) NOT NULL,
RID int(11) NOT NULL auto_increment,
FOREIGN KEY (rate) REFERENCES RATES(RID),
FOREIGN KEY (project) REFERENCES PROJECTS(RID),
primary KEY (RID));
