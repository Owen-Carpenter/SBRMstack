# SBRMstack - Project Setup and Execution

This guide will help you set up and run the SBRMstack project, which consists of a Spring Boot backend and a React frontend.

## Prerequisites

Ensure you have the following installed:

* **Java 17:** Required for the Spring Boot backend.
* **Maven:** Used for building and managing the Spring Boot project.
* **Node.js & npm:** Required for the React frontend.

## Backend Setup (Spring Boot)

1.  **Open a terminal and navigate to the project root:**

    ```bash
    cd path/to/SBRMstack
    ```

2.  **Build and start the backend:**

    ```bash
    mvn spring-boot:run
    ```

    This command will compile the Java code, package it into an executable JAR, and start the Spring Boot application.

## Frontend Setup (React)

1.  **Open a new terminal and navigate to the frontend directory:**

    ```bash
    cd frontend
    ```

2.  **Install dependencies:**

    ```bash
    npm install
    ```

    This command will download and install all the necessary Node.js packages listed in the `package.json` file.

3.  **Start the frontend server:**

    ```bash
    npm run dev
    ```

    This command will start the development server for the React application, enabling hot reloading and other development features.

## Accessing the Application

* **Backend:** The backend runs on `http://localhost:8080`.
* **Frontend:** The frontend runs on `http://localhost:3000`.

Once both servers are running, you can open your browser and access the application at `http://localhost:3000`.

## Troubleshooting

* **Backend Fails to Start:**
    * Ensure Java 17 and Maven are correctly installed and configured.
    * Check the console logs for any error messages.
    * Verify that port 8080 is not already in use.
* **Frontend Fails to Start:**
    * Ensure Node.js and npm are correctly installed.
    * Try running `npm audit fix --force` before running `npm start` again to resolve any dependency issues.
    * Verify that port 3000 is not already in use.
    * Ensure that you are in the correct frontend directory when running npm commands.
