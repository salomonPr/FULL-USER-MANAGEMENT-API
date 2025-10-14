 User Management API
 ===================

  Final Checklist
  ===============

✅ All CRUD operations work
✅ All search endpoints work
✅ Validation catches invalid data
✅ Passwords are encrypted
✅ Passwords are NOT in responses
✅ Duplicate prevention works
✅ Error messages are clear
✅ Timestamps are recorded
✅ Logging works properly
✅ Database connection is secure

Production-ready REST API for user management with full CRUD operations.
------------------------------------------------------------------------

 Features
 ---------
- 1 Create, Read, Update, Delete users
- 2 Search by ID, email, phone, address, age
- 3 Password encryption with BCrypt
- 5 Input validation
- 6 Comprehensive error handling
- 7 Logging
- 8 MySQL database

 Endpoints
==========

 Create User
 -----------
 
POST /api/users

### Get All Users
GET /api/users/users

### Get User by ID
GET /api/users/{id}

### Search Operations
- GET /api/users/email/{email}
- GET /api/users/phoneNumber/{phoneNumber}
- GET /api/users/address/{address}  **here is all user with the same address e.g kigali, Rwanda  
- GET /api/users/age/{age}  ** and also here you will see all with same age 

### Update User
PUT /api/users/updateUsers/{id} : ** Full update 
PATCH /api/users/patchUpdates/{id} : ** Partial update

### Delete User
DELETE /api/users/delete/{id}

## Setup
1. Create MySQL database: `CREATE DATABASE userdb;`
2. Update application.yml with your database credentials
3. Run: `mvn spring-boot:run`
4. API available at: http://localhost:9000

## Testing
===========
all end point are tested in postman and it is working collectly,
Import the Postman collection and test all endpoints.


