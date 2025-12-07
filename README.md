# Weather-Web-Application
This is a full-stack Java web application that fetches real-time weather data using the OpenWeatherMap API, displays it on a JSP page, and stores each weather lookup in a PostgreSQL database. Users can also view all stored weather records and delete any specific entry.

-----------------------------------------------
🚀 Features
-----------------------------------------------
✔ Fetch Weather by City
Enter any city name.
Fetches real-time weather from OpenWeatherMap API.
Displays temperature, condition, humidity, wind speed, and date.

✔ Save Weather Data
Each API result is inserted into a PostgreSQL table using JDBC.

✔ View All Weather Records
Displays a table of all saved weather entries.
Latest records appear first.

✔ Delete Stored Weather Data
Delete any record via a Delete link.
Confirmation popup before deleting.

-----------------------------------------------
🛠️ Technologies Used
-----------------------------------------------
Project Tool-Maven

Backend	Java, Servlet, JDBC

Frontend	HTML, CSS, JS, JSP

Database	PostgreSQL

API	OpenWeatherApi

Server	Apache Tomcat

JSON Parsing	Gson

-----------------------------------------------
How It Works 
-----------------------------------------------
User enters a city name on the webpage.

The request goes to MyServlet, which calls the OpenWeatherMap API.

The API returns weather data (temperature, condition, humidity, wind).

The servlet saves the data into PostgreSQL using JDBC.

The results are displayed on index.jsp with weather icons.

Users can view all saved data through DisplayWeather servlet.

Records can be deleted using the DeleteData servlet.

-----------------------------------------------
📦 Project Structure
-----------------------------------------------

src/
 ├── myPackage/

 │    └── MyServlet.java         
 │
 ├── crud/
 
 │    ├── DisplayWeather.java    
 
 │    └── DeleteData.java     
 
 │
WebContent/
 
 ├── index.jsp                 
 
 ├── style.css                   
 
 ├── myScript.js            
 
 └── images/                    
       └── weather-logo.png      


-----------------------------------------------
 Project Setup
-----------------------------------------------
Install Requirements:
Java JDK, Apache Tomcat, PostgreSQL, and a Java IDE.

Create Database:

CREATE DATABASE mydata;
CREATE TABLE weather (
    id SERIAL PRIMARY KEY,
    city VARCHAR(100),
    date TIMESTAMP,
    temperature INT,
    condition VARCHAR(50),
    humidity INT,
    wind_speed DOUBLE PRECISION
);


Add JARs to WEB-INF/lib:
postgresql-42.x.x.jar
gson-2.x.x.jar

Configure Settings:

Update PostgreSQL credentials in MyServlet.java

Add your OpenWeather API key

Run on Tomcat:
Import as Dynamic Web Project → Add to Server → Run.



-----------------------------------------------
 Screenshots
 -----------------------------------------------
## Main Page
![Main Page Screenshot](https://github.com/user-attachments/assets/4d73009d-d7ba-4127-93f2-dc7cbf9258d4)

## Weather results
<img width="1577" height="918" alt="Screenshot 2025-12-07 194246" src="https://github.com/user-attachments/assets/9397d377-ffaf-4dbc-8516-325d2ffff73a" />

## All data table 
<img width="1073" height="378" alt="Screenshot 2025-12-07 194436" src="https://github.com/user-attachments/assets/c60e0e20-abc2-4557-9a49-909e1fe4ef86" />

## DataBase
<img width="1167" height="423" alt="image" src="https://github.com/user-attachments/assets/0e9b63c4-bec3-4500-ac04-67e104d645a6" />

