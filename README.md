# Member Service

A Spring Boot-based microservice for managing member information and related operations.

## Features

- Member CRUD operations
- Member address management
- Membership management (SILVER, GOLD, PLATINUM levels)
- Integration with Line Service
- Kafka event publishing for member lifecycle events
- Address validation through TCP socket communication

## Prerequisites

- Java 11
- MySQL 8.0
- Kafka
- Maven

## Database Setup

Before running the application, ensure MySQL is running and configured with the following settings:

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/memberdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: 1234
```

The database will be automatically created if it doesn't exist.

## Running the Application

1. Start MySQL server
2. Start Kafka server
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The service will start on port 8081.

## Testing

### Important Notes

1. **Database Requirement**: 
   - The MySQL database must be running and accessible with the credentials specified in `application.yml`
   - The database will be automatically created if it doesn't exist

2. **Integration Test Requirements**:
   - `MemberControllerIntegrationTest.testCreateAndGetMember` requires the Line Service to be running on port 8082
   - The Line Service must be accessible at `http://localhost:8082`

### Running Tests

```bash
./mvnw test
```

## API Endpoints

### Member Management
- `POST /api/members` - Create a new member
- `GET /api/members/{id}` - Get member by ID
- `GET /api/members` - Get all members
- `PUT /api/members/{id}` - Update member
- `DELETE /api/members/{id}` - Delete member

### Address Management
- `GET /api/members/{memberId}/addresses` - Get all addresses for a member
- `POST /api/members/{memberId}/addresses` - Validate and create address
- `PUT /api/members/{memberId}/addresses/{addressId}` - Update address
- `DELETE /api/members/{memberId}/addresses/{addressId}` - Delete address

### Membership Management
- `GET /api/members/{memberId}/membership` - Get membership details
- `POST /api/members/{memberId}/membership` - Create membership
- `PUT /api/members/{memberId}/membership/{membershipId}/points` - Update points

## Test Data Generation

A Python script is provided to generate test data:
```bash
python src/test/resources/generate_member_test_data.py
```

This script will create sample members, addresses, and membership data in the database. 