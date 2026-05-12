# Domain Availability Search API

## About the Project

This project is a RESTful API that allows users to search for domain name availability. When a requested domain is unavailable, the service integrates with a Large Language Model (LLM) to generate creative alternative suggestions, which are then verified in parallel until ten available options are found and returned to the user.

The service is built on an efficient architecture combining in-memory caching (Redis), a NoSQL database (MongoDB), and containerization (Docker), ensuring high performance and scalability.

### Main Goals

- Provide a fast and intuitive domain search experience.
- Minimize redundant calls to the domain verification API through intelligent caching.
- Leverage LLM capabilities to generate relevant and creative domain suggestions.
- Simplify deployment in containerized environments.

---

## Technologies Used

### Java 21
The latest LTS version of Java, focused on performance and modern language features.
Documentation: https://docs.oracle.com/en/java/javase/17/

### Lombok
Reduces boilerplate code by automatically generating getters, setters, constructors, and more.
Documentation: https://projectlombok.org/

### Redis
An in-memory data store used as a cache layer to avoid redundant calls to the domain verification API and the LLM.
Documentation: https://redis.io/docs/

### MongoDB
A NoSQL database used to persist domain search history in a flexible and scalable way.
Documentation: https://www.mongodb.com/docs/

### Spring AI
The primary integration layer for all AI and external API communication. Spring AI provides a unified abstraction over LLM providers, eliminating the need for low-level HTTP clients like OpenFeign for those interactions. It handles prompt templating, model configuration, and response parsing out of the box, keeping the codebase clean and provider-agnostic.
Documentation: https://docs.spring.io/spring-ai/reference/

### Docker
A containerization platform used to create consistent and portable environments for deployment.
Documentation: https://docs.docker.com/

### Domain Verification API
Integration with an external API (such as GoDaddy Domains API or Domainr) responsible for checking whether a given domain name is available for registration. This HTTP communication is handled through Spring AI's built-in REST client support, keeping the stack consistent without requiring a separate Feign dependency.

### LLM Integration (Anthropic Claude API)
When a domain is unavailable, the service uses Spring AI's `ChatClient` to call the configured LLM provider and generate 20 to 30 creative domain name variations. The API then verifies each suggestion in parallel and returns the first ten available ones. Spring AI's abstraction makes it straightforward to swap providers (for example, from Claude to OpenAI) by changing configuration alone, without touching service logic.

---

## Features

- Search domain availability by name and TLD.
- Automatic LLM-powered suggestions when a domain is taken.
- Parallel verification of suggested domains with rate-limit control.
- Smart caching with Redis to speed up repeated searches.
- Search history persistence with MongoDB.
- Full Docker support for fast deployment in any environment.

---

## Request and Response Example

```
GET /domains/search?name=mystore&tlds=.com,.io,.dev
```

```json
{
  "searched": "mystore.com",
  "available": false,
  "suggestions": [
    "getmystore.com",
    "mystore.io",
    "mystoreapp.com",
    "trymystore.io",
    "mystorelab.dev",
    "usemystore.com",
    "mystorehq.io",
    "themystore.com",
    "mystore.app",
    "mystoreapi.dev"
  ],
  "cachedResult": false
}
```

---

## Project Architecture

```
src/
├── controller/
│   └── DomainController.java
├── service/
│   ├── DomainSearchService.java
│   ├── DomainCheckService.java
│   └── DomainSuggestionService.java
├── client/
│   └── DomainApiClient.java
├── model/
│   ├── DomainResult.java
│   └── SearchHistory.java
└── cache/
    └── DomainCacheService.java
```

---

## How to Run the Project

### Prerequisites

- Docker and Docker Compose
- Java 17
- Maven (or use the included `mvnw` wrapper)

### Steps

1. Clone the repository:

```bash
git clone https://github.com/your-username/domain-search-api.git
cd domain-search-api
```

2. Set up environment variables. Create a `.env` file at the project root:

```env
DOMAIN_API_KEY=your_domain_api_key
SPRING_AI_ANTHROPIC_API_KEY=your_anthropic_api_key
REDIS_HOST=localhost
REDIS_PORT=6379
MONGO_URI=mongodb://localhost:27017/domainsearch
```

3. Start the infrastructure containers:

```bash
docker-compose up -d
```

4. Build and run the application:

```bash
./mvnw spring-boot:run
```

5. Access the API at `http://localhost:8080`.

### Docker Compose Overview

The `docker-compose.yml` file provisions the following services:

- Redis on port 6379
- MongoDB on port 27017
- The Spring Boot application on port 8080

---

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request.

---

## License

MIT License

Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
