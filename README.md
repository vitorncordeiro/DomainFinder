# Domain Availability Search API

## About the Project

This project is a RESTful API that allows users to search for domain name availability. When a requested domain is unavailable, the service integrates with a Large Language Model (LLM) to generate creative alternative suggestions, which are then verified individually until ten available options are found and returned to the user.

Domain availability is checked directly against official RDAP (Registration Data Access Protocol) servers. The application periodically fetches the IANA RDAP bootstrap file, which maps every TLD in existence to its official RDAP server, and caches this map in Redis. When a domain is searched, the application resolves the correct RDAP server for its TLD and queries it directly — no third-party domain APIs or paid services involved.

For every available domain found — whether the originally searched one or an AI-generated suggestion — the API automatically includes a direct registration link through the Hostinger affiliate program, allowing users to register the domain in one click.

Users can authenticate with their Google account to access their personal search history and saved domains.

### Main Goals

- Provide a fast and reliable domain search experience backed by official registry data.
- Keep domain verification free and independent by querying RDAP servers directly.
- Leverage LLM capabilities to generate relevant and creative domain suggestions when the searched name is taken.
- Surface a direct Hostinger registration link for every available domain found.
- Persist search history per authenticated user.
- Simplify deployment in containerized environments.

---

## Technologies Used

### Java 21
The latest LTS version of Java, focused on performance and modern language features.
Documentation: https://docs.oracle.com/en/java/javase/21/

### Spring Boot 3
The core application framework, providing auto-configuration, dependency injection, scheduling, and the embedded web server.
Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/

### Spring Security + OAuth2
Handles authentication via Google OAuth2. Users log in with their Google account and their session is stored server-side in Redis via Spring Session.
Documentation: https://docs.spring.io/spring-security/reference/

### Spring AI
The integration layer for LLM communication. Spring AI provides a unified abstraction over providers like Anthropic Claude, handling prompt templating, model configuration, and response parsing out of the box. Swapping providers requires only a configuration change.
Documentation: https://docs.spring.io/spring-ai/reference/

### Spring Retry
Used in the IANA bootstrap scheduler to automatically retry failed HTTP requests with configurable backoff. Prevents a transient network failure from leaving the TLD cache stale.
Documentation: https://docs.spring.io/spring-retry/docs/current/reference/html/

### IANA RDAP Bootstrap
The application fetches the official IANA RDAP bootstrap file (`https://data.iana.org/rdap/dns.json`) on startup and on a daily schedule. This file maps every TLD to its official RDAP server. The parsed map is cached in Redis and used to resolve the correct RDAP endpoint for any domain search. Compound TLDs such as `.com.br` are handled with a fallback strategy that first attempts to match the full suffix before falling back to the rightmost label.
IANA bootstrap file: https://data.iana.org/rdap/dns.json

### RDAP (Registration Data Access Protocol)
The modern standard for querying domain registration data, replacing WHOIS. Each TLD has its own RDAP server. The application queries the appropriate server directly:
- HTTP `404` means the domain is not registered — available.
- HTTP `200` means the domain is registered — not available.
  Documentation: https://about.rdap.org/

### Redis
Used for two purposes: caching domain search results and the IANA TLD map to avoid redundant lookups, and storing authenticated user sessions via Spring Session.
Documentation: https://redis.io/docs/

### MongoDB
A NoSQL database used to persist user accounts and domain search history in a flexible and scalable way.
Documentation: https://www.mongodb.com/docs/

### RabbitMQ
Used for asynchronous processing. After a domain search completes, the result is published to a RabbitMQ queue and consumed in the background to persist the search history in MongoDB. This keeps the HTTP response time low by decoupling persistence from the request lifecycle.
Documentation: https://www.rabbitmq.com/docs

### Lombok
Reduces boilerplate code by automatically generating getters, setters, constructors, and builders.
Documentation: https://projectlombok.org/

### Hostinger Affiliate Program
When a domain is confirmed as available, the API builds a registration URL pointing to Hostinger through its affiliate program. The link is returned alongside each available domain in the response so any frontend can render a direct register button without additional logic. The affiliate code is externalized as an environment variable.
Affiliate program: https://www.hostinger.com/affiliates

### Docker
A containerization platform used to create consistent and portable environments for deployment.
Documentation: https://docs.docker.com/

---

## How It Works

### IANA Bootstrap Job

On startup and every day at 3am, the application fetches the IANA RDAP bootstrap file and parses it into a flat map of `TLD → RDAP server URL`. This map is stored in Redis with a 7-day TTL.

If the fetch fails, Spring Retry automatically retries up to 3 times with exponential backoff (5s, 10s, 20s) before logging the error and keeping the previous cached map.

### Domain Search Flow

```
GET /domains/search?name=mystore&tlds=.com,.io,.dev

1. Check Redis cache — return immediately if found

2. Resolve the TLD from the domain name
   "mystore.com" -> TLD "com" -> "https://rdap.verisign.com/com/v1/"

3. Query the RDAP server
   GET https://rdap.verisign.com/com/v1/domain/mystore.com
   404 -> available   |   200 -> not available

4. If available -> build response with Hostinger registration URL and return

5. If not available -> call Spring AI (Claude) for 25 domain suggestions

6. For each suggestion, resolve TLD and query the respective RDAP server

7. Collect the first 10 available suggestions, each with its Hostinger URL

8. Publish DomainSearchedEvent to RabbitMQ (history saved asynchronously)

9. Save result to Redis cache

10. Return response
```

### Google Authentication Flow

```
1. Frontend redirects to GET /oauth2/authorization/google

2. Spring redirects the user to Google's consent screen

3. User approves

4. Google redirects to GET /login/oauth2/code/google?code=...

5. Spring exchanges the code for an access token and fetches user info

6. CustomOAuth2UserService finds or creates the user in MongoDB

7. Spring creates a session stored in Redis and sets a SESSION cookie

8. All subsequent requests are authenticated via the cookie
```

---

## Features

- Search domain availability for any TLD known to the IANA bootstrap registry.
- Compound TLD support (e.g. `.com.br`, `.co.uk`) with automatic RDAP server resolution.
- Automatic LLM-powered suggestions when a domain is taken, verified against official RDAP servers.
- Smart Redis caching for search results (24h TTL) and the IANA TLD map (7d TTL).
- Daily IANA bootstrap refresh with retry and exponential backoff.
- Asynchronous search history persistence via RabbitMQ.
- Google OAuth2 authentication — users see only their own history.
- Direct Hostinger affiliate registration link for every available domain.
- Admin endpoints to inspect and manually refresh the IANA cache.
- Full Docker support for fast deployment in any environment.

---

## API Reference

### Domain Search

```
GET /domains/search?name=mystore&tlds=.com,.io,.dev
```

Public endpoint. Authentication is optional — if authenticated, the search is linked to the user's history.

```json
{
  "searched": "mystore.com",
  "available": false,
  "registrationUrl": null,
  "suggestions": [
    {
      "domain": "getmystore.com",
      "available": true,
      "registrationUrl": "https://www.hostinger.com/register-domain?domain=getmystore.com&REFERRALCODE=YOUR_CODE"
    },
    {
      "domain": "mystore.io",
      "available": true,
      "registrationUrl": "https://www.hostinger.com/register-domain?domain=mystore.io&REFERRALCODE=YOUR_CODE"
    }
  ],
  "cachedResult": false
}
```

### Authentication

```
GET  /oauth2/authorization/google   Start Google login flow
GET  /auth/me                       Returns authenticated user data (401 if not logged in)
POST /auth/logout                   Invalidates session
```

### Search History (requires authentication)

```
GET    /domains/history             Full search history for the authenticated user
GET    /domains/history/{domain}    History for a specific domain
DELETE /domains/history/{id}        Delete a history entry
```

### Admin

```
GET  /admin/iana/bootstrap          Inspect the current cached TLD map
POST /admin/iana/bootstrap/refresh  Manually trigger a bootstrap refresh
```

---

## Project Architecture

```
src/
├── controller/
│   ├── DomainController.java
│   ├── AuthController.java
│   └── AdminController.java
├── service/
│   ├── domain/
│   │   ├── DomainSearchService.java
│   │   ├── DomainCheckService.java
│   │   └── TldExtractor.java
│   ├── iana/
│   │   ├── IanaBootstrapClient.java
│   │   ├── IanaBootstrapParser.java
│   │   └── IanaCacheService.java
│   ├── suggestion/
│   │   └── DomainSuggestionService.java
│   └── affiliate/
│       └── AffiliateService.java
├── client/
│   └── RdapClient.java
├── scheduler/
│   └── IanaBootstrapScheduler.java
├── messaging/
│   ├── publisher/
│   │   └── DomainEventPublisher.java
│   └── consumer/
│       └── SearchHistoryConsumer.java
├── model/
│   ├── DomainResult.java
│   ├── DomainSearchResult.java
│   ├── SearchHistory.java
│   └── User.java
├── cache/
│   └── DomainCacheService.java
├── security/
│   ├── AuthenticatedUser.java
│   └── CustomOAuth2UserService.java
├── config/
│   ├── SecurityConfig.java
│   ├── RedisConfig.java
│   ├── RabbitConfig.java
│   └── SpringAiConfig.java
└── exception/
    ├── GlobalExceptionHandler.java
    ├── TldNotSupportedException.java
    └── RdapServerException.java
```

---

## How to Run the Project

### Prerequisites

- Docker and Docker Compose
- Java 17
- Maven (or use the included `mvnw` wrapper)
- A Google Cloud project with OAuth2 credentials configured (see Google Auth Guideline)

### Steps

1. Clone the repository:

```bash
git clone https://github.com/your-username/domain-search-api.git
cd domain-search-api
```

2. Create a `.env` file at the project root based on `.env.example`:

```env
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
SPRING_AI_ANTHROPIC_API_KEY=your_anthropic_api_key
REDIS_HOST=localhost
REDIS_PORT=6379
MONGO_URI=mongodb://localhost:27017/domainsearch
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=admin
RABBITMQ_PASS=admin
HOSTINGER_AFFILIATE_CODE=your_affiliate_code
```

3. Start the infrastructure:

```bash
docker-compose up -d
```

4. Build and run the application:

```bash
./mvnw spring-boot:run
```

5. Access the API at `http://localhost:8080`.
6. Access the RabbitMQ management UI at `http://localhost:15672` (user: admin / pass: admin).
7. Access the Swagger UI at `http://localhost:8080/swagger-ui.html`.

### Docker Compose Services

- MongoDB on port 27017
- Redis on port 6379
- RabbitMQ on port 5672 (management UI on 15672)
- Spring Boot application on port 8080

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