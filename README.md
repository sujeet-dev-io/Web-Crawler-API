# Web Crawler API

## Overview
This project is a **Spring Boot-based web crawler** that crawls websites and stores the URLs found on a given seed URL. It supports scheduling, pagination, and proper exception handling.

## Features
- Start a crawl job for a given URL.
- Retrieve crawl results by ID.
- Paginate crawl results.
- **Swagger UI** for API testing.
- **Quartz Scheduler** for handling high load.
- Exception handling using `@RestControllerAdvice`.

---

## Prerequisites
Ensure you have the following installed:
- **JDK 17+** (Oracle OpenJDK 23 recommended)
- **Maven 3.6+**
- **Spring Boot 3.4.0**
- **PostgreSQL/MySQL** (Optional, if using persistent storage)

---

## How to Run
### 1. Clone the Repository
```sh
git clone https://github.com/sujeet-dev-io/web-crawler-api.git
cd web-crawler-api
```

### 2. Build & Run the Application
```sh
mvn clean install
mvn spring-boot:run
```

---

## API Documentation & Testing

### **Swagger UI**
To access **Swagger UI**, disable global exception handling temporarily:
1. **Comment out `@RestControllerAdvice`** in `com.biztel.ai.exception` package.
2. Start the application.
3. Open: [Swagger UI](http://localhost:8080/swagger-ui/index.html).

---

## API Endpoints

### **1. Start a Crawl Job**
**POST /api/v1/crawl/start**

#### Request Body:
```json
{
    "url": "https://sujeetportfolios.netlify.app"
}
```

#### Response:
```json
{
    "status": "SUCCESS",
    "data": {
        "crawlId": 11,
        "seedUrl": "https://sujeetportfolios.netlify.app",
        "crawledUrls": [],
        "status": "IN_PROGRESS",
        "createdAt": "2025-03-16T21:17:06.5471557"
    }
}
```

---

### **2. Get Crawl Results by ID**
**GET /api/v1/crawl/getById/{id}**

#### Example Response:
```json
{
    "status": "SUCCESS",
    "data": {
        "crawlId": 1,
        "seedUrl": "https://sujeetportfolios.netlify.app",
        "crawledUrls": [
            "https://github.com/sujeet-dev-io",
            "https://sujeetportfolios.netlify.app#home",
            "https://github.com/sujeet-dev-io/sujeet-dev-io",
            "https://mail.google.com/mail/?view=cm&fs=1&to=sujeetprajap02@gmail.com"
        ],
        "status": "COMPLETED",
        "createdAt": "2025-03-16T21:35:36.64024"
    }
}
```

---

### **3. Get Paginated Crawl Results**
**GET /api/v1/crawl/pagination?page=0&size=3**

#### Example Response:
```json
{
    "status": "SUCCESS",
    "data": {
        "content": [
            {
                "crawlId": 1,
                "seedUrl": "https://sujeetportfolios.netlify.app",
                "crawledUrls": [
                    "https://github.com/sujeet-dev-io",
                    "https://sujeetportfolios.netlify.app#home"
                ],
                "status": "COMPLETED",
                "createdAt": "2025-03-16T21:35:36.64024"
            },
            {
                "crawlId": 2,
                "seedUrl": "https://example.com",
                "crawledUrls": [
                    "https://www.iana.org/domains/example"
                ],
                "status": "COMPLETED",
                "createdAt": "2025-03-16T21:40:15.421379"
            }
        ],
        "totalPages": 1,
        "totalElements": 3,
        "size": 3
    }
}
```

---

## **Exception Handling**
All API errors are handled globally using `@RestControllerAdvice`. Typical error response:
```json
{
    "status": "ERROR",
    "message": "Invalid URL provided. Please check the input."
}
```

---

## **Performance Handling with Quartz Scheduler**
- The system supports high-load scenarios using **Quartz Scheduler**.
- Ensures that crawling tasks run asynchronously in the background.
- Cleans up old crawl results automatically using:
  ```java
  @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
  public void clearOldCrawlResults() {
      crawlRepository.deleteAll();
  }
  ```

---

## **Contributors**
- **Sujeet Prajapati** ([GitHub](https://github.com/sujeet-dev-io))

---

## **License**
This project is open-source and available under the MIT License.

