# 🎬 Movie Management Application

![App Screenshot](./screenshot.png)

A full-stack movie management system with Angular frontend and Spring Boot backend.

## ✨ Features

### 🎥 Movie Features
- Search movies from OMDB API
- View detailed movie information
- Rate movies (1-10 scale)
- Save favorite movies

### 👥 User Management
- User registration/login
- Role-based access (User/Admin)
- Personalized movie ratings
- User dashboard

### 🛠️ Admin Features
- Add/remove movies in bulk
- Manage all movies in database
- Special admin dashboard

## 🛠️ Tech Stack

### Frontend
- ![Angular](https://img.shields.io/badge/Angular-16+-red)
- ![Angular Material](https://img.shields.io/badge/Material_UI-8.0+-blue)
- ![TypeScript](https://img.shields.io/badge/TypeScript-4.9+-brightgreen)
- ![RxJS](https://img.shields.io/badge/RxJS-7.5+-pink)

### Backend
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0+-green)
- ![Spring Security](https://img.shields.io/badge/Spring_Security-6.0+-yellow)
- ![H2 Database](https://img.shields.io/badge/H2_Database-2.1+-lightgrey)

## 🚀 Getting Started

### Prerequisites
- Node.js v16+
- Angular CLI v15+
- Java JDK 17+
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yasmeenesaad/Movie_Managment2
```
2. **Backend Setup**
```bash
cd Movie_Managment
mvn spring-boot:run
```
3. **Frontend Setup**
```bash
cd movie-management-frontend
npm install
ng serve
```
# Access the application

**Frontend:** [http://localhost:4200](http://localhost:4200)  
**Backend API:** [http://localhost:8080](http://localhost:8080)  

## ⚙️ Configuration

### Backend Configuration
Create `application.properties` in `Movie_Managment/src/main/resources`:

```properties
# OMDB API
omdb.api.key=1d56bb56
omdb.api.url=http://www.omdbapi.com/
omdb.api.i=tt3896198
```
### Frontend Configuration
Environment variables in frontend/src/environments/environment.ts:

``` typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```
## 🔍 API Documentation

| Endpoint                     | Method | Description          | Access   |
|------------------------------|--------|----------------------|----------|
| `/api/auth/register`          | POST   | Register new user    | Public   |
| `/api/auth/login`             | POST   | User login           | Public   |
| `/api/movies/search`          | GET    | Search movies        | User+    |
| `/api/movies/{imdbId}`        | GET    | Get movie details    | User+    |
| `/api/movies/rate`            | POST   | Rate a movie         | User+    |
| `/api/admin/movies/batch`     | POST   | Batch add movies     | Admin    |
| `/api/admin/movies/{imdbId}`  | DELETE | Remove movie         | Admin    |


## 🏗️ Project Structure
```
movie-management/
├── backend/
│   ├── src/
│   │   ├── main/java/com/example/movieapp/
│   │   │   ├── config/       # app config
│   │   │   ├── controller/   # REST APIs
│   │   │   ├── dto/          # Data transfer objects
│   │   │   ├── exceptiion    
│   │   │   ├── mapper
│   │   │   ├── model/        # JPA entities
│   │   │   ├── repository/   # Database repos
│   │   │   ├── security      #  Security config
│   │   │   ├── service/      
│   │   │   |──├── impl    # Business logic
│   │   │   └── MovieAppApplication.java
│   │   └── resources/        # Config files
│   └── pom.xml               # Maven config
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── admin/        # Admin components
│   │   │   ├── auth/         # Auth components
│   │   │   ├── shared/       # Shared modules
│   │   │   ├── user/         # User components
│   │   │   └── *.module.ts   # Angular modules
│   │   ├── assets/           # Static files
│   │   └── environments/     # Environment configs
│   └── angular.json          # Angular config
└── README.md
```
