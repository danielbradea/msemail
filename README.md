# Email Service
## Overview
The Email Service is a REST API application built in Java, providing an easy-to-use email sending solution with support for both HTML and plain text formats.
It leverages RabbitMQ for message queuing, allowing for scalable email processing, and Spring Boot for rapid application development and dependency management.

## Features
 - Send emails in plain text or HTML format.
 - Enqueue email requests in RabbitMQ for asynchronous processing.
 - Validate email addresses before sending.
 - Secure API endpoints with JWT Bearer token authentication (OpenAPI documentation included).
 - Custom exception handling and structured error responses.

## Technologies Used
 - Java 17 or higher
 - Maven
 - RabbitMQ
 - JavaMailSender for sending emails
 - OpenAPI (Swagger) for API documentation

## Setup
1. Clone Repository
    ```sh
    git clone https://github.com/danielbradea/msemail.git
    cd msemail
    ```
2. Build the project
    ```sh
    mvn clean install
   ```
3. Run the app
    ```sh
   PORT:8082
   EMAIL_HOST:smtp.gmail.com
   EMAIL_PORT:587}
   EMAIL_USER:user@gmail.com
   EMAIL_PASSWORD:password
   APP_URL=http://localhost:8080
   RMQ_HOST=localhost \
   RMQ_PORT=5672 \
   RMQ_USER=user \
   RMQ_PASSWORD=password \
   RMQ_VHOST=msemail \
   JWT_SECRET=d5501539-43e9-4e97-8256-4ab29a5bf539 \
   AUTH_SERVICE:http://localhost:8080/mslogin
   mvn spring-boot:run
   ```

## Accessing Swagger API Documentation
Once the application is running, you can access the Swagger UI to view and interact with the API documentation:
- Open a web browser and go to: `http://localhost:8082/msemail/swagger-ui/index.html`
