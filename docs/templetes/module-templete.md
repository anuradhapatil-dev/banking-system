---
title: "<Module Name>"
date: YYYY-MM-DD
---

# <Module Name>

## Overview
Brief summary of the module’s purpose, responsibilities, and where it fits in the system.

## API Endpoints / Interfaces
List all exposed endpoints with method types and URL paths.

Example:
- `POST /api/<resource>` – Creates a new <resource>
- `GET /api/<resource>/{id}` – Retrieves a <resource> by ID

## Data Models
Define all involved models: entities, DTOs, and any key annotations.

```java
public class <ModelName> {
    private String id;
    private String name;
    ...
}
```
## Example Usage
Provide real sample JSON for request and response.

### Request:
```json
{
  "name": "John Doe",
  "password": "secure123",
  "phone": "9876543210",
  "type": "SAVINGS"
}
```

### Response:
```json
{
  "result": "success",
  "id": "abc123"
}
```
## Testing
- Unit tested with @WebMvcTest
- Integration tested with mock DB using @DataJpaTest
