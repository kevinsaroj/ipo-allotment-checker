# ipo-allotment-checker

Summary of the IPO Allotment Checker Application Implementation
The IPO Allotment Checker Application simplifies the IPO allotment process for investors by integrating with registrars, providing a streamlined user experience, and offering notifications. Below is a summary of the features and implementations so far:

Core Features Implemented
User Authentication:

Implemented secure user registration and login using Spring Security.
Used SecurityFilterChain for configuring security with public endpoints for registration and login.
Allotment Status Checker:

Integrated with Maashitla and Linkintime registrar APIs for fetching IPO allotment statuses.
API endpoints allow users to check their allotment status for all linked accounts.
Possible outcomes include:
X Shares Allotted
Not Allotted
Not Applied
Notification System:

Email notifications configured using Spring Boot's mail integration.
Additional Features
Database Integration:

Configured a MySQL database for storing user details, linked accounts, and allotment status.
Used JPA for entity mapping and data persistence.
Security Configuration:

JWT-based authentication setup planned for secure API access.
Passwords are hashed using BCryptPasswordEncoder.
Public access enabled for /register, /login, and Swagger documentation endpoints.
Protected other endpoints with authentication.
Logging and Error Handling:

Configured application logging to a file for monitoring errors and debugging.
Meaningful error messages for registration, login, and allotment checks.
Swagger Documentation:

Added Swagger UI for API documentation to simplify testing and integration.

