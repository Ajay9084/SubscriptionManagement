# Best Practices Implemented

This document explains every production-level improvement made to this codebase, what was changed, and why it matters.

---

## 1. Proper Logging — No `System.out.println`

**What we did:**
Replaced all `System.out.println(...)` calls with `log.info(...)` / `log.warn(...)` / `log.error(...)` using SLF4J + Lombok's `@Slf4j`.

**Why:**
- `System.out.println` has no log level — you can't filter it, disable it, or route it to a log file in production
- SLF4J logs include timestamp, thread name, log level, and class name automatically
- You can control log verbosity per environment without changing code (e.g. `DEBUG` locally, `WARN` in production)

**Files changed:** `CacheConfig.java`, `InternalSubscriptionController.java`, `PurchaseController.java`, `ProductController.java`

---

## 2. Removed Empty Files

**What we did:**
Deleted files that contained only a package declaration and no code:
- `ProductController.java` (subscription service)
- `AdminController.java` (subscription service)
- `exception/ErrorResponse.java` (both services — empty shell, real class was in `dto/response/`)

**Why:**
- Empty files mislead other developers into thinking functionality exists when it doesn't
- Dead code creates maintenance confusion and inflates the apparent size of the codebase

---

## 3. Global Exception Handling

**What we did:**
Both services have a `GlobalExceptionHandler` (`@RestControllerAdvice`) that catches every exception type and returns a structured `ErrorResponse` JSON instead of a raw Spring error page.

Added **FeignException handling** to purchase-service — previously a non-409 Feign failure (e.g. subscription service is down) would fall through to the generic handler with no context. Now it returns a clear `503 SERVICE_UNAVAILABLE` with the message "Subscription service is unavailable. Please try again later."

**Why:**
- Without this, Spring returns an HTML error page or an unstructured JSON dump to API clients
- Clients (mobile apps, frontend, other services) need predictable, structured error responses
- FeignException must be caught separately because it's a library exception — not your own custom exception

---

## 4. Request/Response Logging Middleware

**What we did:**
Added `RequestLoggingFilter` (extends `OncePerRequestFilter`) to both services. It logs every request as:
```
GET /subscriptions/1 -> 200 (23ms)
POST /api/purchase -> 409 (11ms)
```

**Why:**
- Without request logging, there's no way to trace what API calls came in, how long they took, or what status was returned — you're flying blind in production
- `OncePerRequestFilter` guarantees the filter runs exactly once per request even in complex filter chains

---

## 5. Input Validation with `@Valid` and `@FutureOrPresent`

**What we did:**
- Added `@Valid` to `InternalSubscriptionController` — it was missing, so the internal endpoint accepted completely unvalidated request bodies
- Added `@FutureOrPresent` to all date fields in `CreateSubscriptionRequest` and `UpdateSubscriptionRequest`

**Why:**
- Without `@Valid`, Bean Validation annotations (`@NotNull`, `@FutureOrPresent`) on the DTO are silently ignored — the endpoint accepts garbage
- `@FutureOrPresent` prevents creating subscriptions with a start or expiry date in the past, which would immediately put them in an invalid state

---

## 6. Batch Database Writes in the Scheduler

**What we did:**
Replaced a `for` loop calling `repository.save()` on each subscription individually with a single `repository.saveAll(expired)` call.

**Before:**
```java
for (Subscription s : expired) {
    s.setStatus(EXPIRED);
    repository.save(s);   // one SQL UPDATE per record
}
```

**After:**
```java
expired.forEach(s -> s.setStatus(EXPIRED));
repository.saveAll(expired);  // one batch operation
```

**Why:**
- N individual `save()` calls = N separate SQL round-trips to the database
- `saveAll()` sends them as a batch — dramatically faster when many subscriptions expire at once
- Also added `@Transactional` to the scheduler method so all updates succeed or all roll back together

---

## 7. Configurable Scheduler Cron

**What we did:**
Replaced the hardcoded `@Scheduled(fixedRate = 30000)` with `@Scheduled(cron = "${subscription.expiry.cron:0 0 0 * * *}")`.

The cron is now set in `application.properties`:
```properties
# Local dev — runs every 30 seconds so you can see it working
subscription.expiry.cron=0/30 * * * * *
```
For production, set this environment variable to `0 0 0 * * *` (once at midnight).

**Why:**
- A 30-second interval is fine for local testing but burns CPU in production hitting the DB every 30 seconds at midnight when nothing is expiring
- Externalising the cron means you change behaviour per environment without touching code

---

## 8. No Hardcoded URLs or Magic Numbers

**What we did:**
- Moved Feign target URL `http://localhost:8000` out of `SubscriptionClient.java` → now reads from `${subscription.service.url}`
- Moved hardcoded `30` days subscription duration → now reads from `${subscription.default.duration-days:30}`

**Why:**
- Hardcoded values break the moment you deploy to staging or production where the URL is different
- Config values belong in `application.properties` (or environment variables), not scattered across Java files
- Changing the default duration no longer requires finding and editing Java source code

---

## 9. Sort Field Validation

**What we did:**
Created `SortValidator` with an allowlist of valid sort columns:
```java
Set<String> SUBSCRIPTION_SORT_FIELDS = Set.of(
    "id", "customerId", "productId", "status", "startDate", "expiryDate", "createdAt", "updatedAt"
);
```
Before building the `Sort` object, we validate the `sortBy` query param against this set. Invalid values throw `IllegalArgumentException` → caught by `GlobalExceptionHandler` → returns a clean `400 BAD REQUEST`.

**Why:**
- Without this, passing `sortBy=invalidColumn` causes Hibernate to build invalid SQL and the endpoint returns a `500 INTERNAL SERVER ERROR` with a stack trace
- An attacker could probe for field names or cause unhandled errors this way

---

## 10. Cleaner Code — EnumSet for Status Checks

**What we did:**
Replaced a long `if` chain in `cancelSubscription` with an `EnumSet`:

**Before:**
```java
if (status != CREATED && status != ACTIVE && status != SUSPENDED) {
    throw new InvalidStateTransitionException(...);
}
```

**After:**
```java
private static final Set<SubscriptionStatus> CANCELLABLE_STATUSES =
    EnumSet.of(CREATED, ACTIVE, SUSPENDED);

if (!CANCELLABLE_STATUSES.contains(subscription.getStatus())) {
    throw new InvalidStateTransitionException(...);
}
```

**Why:**
- `EnumSet` is faster than repeated `!=` comparisons and more readable
- Adding a new cancellable status means adding one entry to the set, not editing a boolean expression
- The set is a named constant — its intent is clear

---

## 11. Meaningful Method Names

**What we did:**
Renamed `purchaseResponse()` → `createPurchase()` in `PurchaseService` interface and `PurchaseServiceImpl`.

**Why:**
- A method named `purchaseResponse()` sounds like it builds a response object, not that it executes a purchase
- Method names should describe what the method **does**, not what it **returns**
- The controller already called `createPurchase` internally — now the service matches

---

## 12. API Documentation with Swagger / OpenAPI

**What we did:**
- Added `springdoc-openapi-starter-webmvc-ui` to purchase-service (subscription already had it)
- Added `@Tag` on every controller and `@Operation` on every endpoint in both services
- Created `OpenApiConfig` bean in purchase-service with title, description, and contact info

**Why:**
- Swagger UI lets any developer (or QA tester) explore and test all endpoints without Postman
- `@Tag` groups related endpoints; `@Operation` explains what each endpoint does
- Self-documenting APIs reduce onboarding time for new team members

---

## 13. Postman Collection with Environment Variables

**What we did:**
Replaced all hardcoded `http://localhost:8001/...` URLs in the Postman collection with variables:
- `{{subscription_service_url}}`
- `{{purchase_service_url}}`
- `{{subscription_id}}`, `{{product_id}}`, `{{customer_id}}`

Created a companion `Postman Environment` file with default local values.

**Why:**
- With hardcoded URLs, switching from local to staging means manually editing dozens of requests
- With environment variables, you just switch the active environment in one click
- IDs like `subscription_id` can be updated once and all requests that use them update automatically

---

## 14. Unit Tests for Purchase Service

**What we did:**
Added `PurchaseServiceImplTest` with 5 test cases covering:
- Successful purchase
- Product not found
- Duplicate purchase (same customer + product)
- Feign conflict (subscription already exists)
- Correct total amount calculation

**Why:**
- The purchase service had zero unit tests — a bug in `createPurchase` would only be caught in manual testing
- Unit tests (with Mockito mocks) run in milliseconds and don't need a database
- Each test covers one specific failure path so it's easy to pinpoint what broke when a test fails

---

## 15. Shared Library (`common-lib`)

**What we did:**
Created a third Maven module `common-lib` that both services depend on. Moved into it:
- `RequestLoggingFilter` — was copy-pasted identically in both services
- `ErrorResponse` — was duplicated with slight differences between services

Both services now declare `common-lib` as a Maven dependency. The root `pom.xml` builds all three modules together with `mvn clean install`.

**Why:**
- Copy-pasted code means bug fixes must be applied in multiple places — you will eventually forget one
- A single shared class means one fix, both services benefit on the next build
- The `ErrorResponse` format is now guaranteed to be identical across services — consistent API contracts

---

## Summary Table

| # | Practice | Impact |
|---|---|---|
| 1 | SLF4J logging | Filterable, structured logs in production |
| 2 | Remove dead files | Cleaner, honest codebase |
| 3 | Global exception handling + FeignException | Consistent, structured error responses |
| 4 | Request logging middleware | Full request traceability |
| 5 | `@Valid` + `@FutureOrPresent` | Catches bad input at the boundary |
| 6 | `saveAll()` batch writes | Fewer DB round-trips in scheduler |
| 7 | Configurable cron | Environment-specific scheduling without code changes |
| 8 | Externalized config | No hardcoded URLs or magic numbers in Java |
| 9 | `sortBy` allowlist validation | Prevents 500s from invalid sort columns |
| 10 | `EnumSet` for status checks | Cleaner, faster, more maintainable |
| 11 | Rename `purchaseResponse` → `createPurchase` | Method name matches what it actually does |
| 12 | Swagger/OpenAPI on both services | Self-documenting, browser-testable APIs |
| 13 | Postman environment variables | One-click environment switching |
| 14 | Unit tests for purchase service | Regression safety net, was at zero coverage |
| 15 | `common-lib` shared module | Single source of truth for shared code |
