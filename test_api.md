Here are all the possible REST API endpoints for the employee management application, along with the corresponding request payloads for testing in Postman:

### API Endpoints

#### 1. **Get All Employees**
- **Endpoint**: `GET /api/employees`
- **Request**: No payload needed.
- **Example Request**:
  ```
  GET http://localhost:8080/api/employees
  ```

#### 2. **Get Employee by ID**
- **Endpoint**: `GET /api/employees/{id}`
- **Request**: Replace `{id}` with the employee number.
- **Example Request**:
  ```
  GET http://localhost:8080/api/employees/1
  ```

#### 3. **Create Employee**
- **Endpoint**: `POST /api/employees`
- **Request Payload**:
  ```json
  {
      "employeeNumber": 1,
      "lastName": "Doe",
      "firstName": "John",
      "extension": "123",
      "email": "john@example.com",
      "officeCode": "1",
      "reportsTo": null,
      "jobTitle": "Developer"
  }
  ```
- **Example Request**:
  ```
  POST http://localhost:8080/api/employees
  Content-Type: application/json

  {
      "employeeNumber": 1,
      "lastName": "Doe",
      "firstName": "John",
      "extension": "123",
      "email": "john@example.com",
      "officeCode": "1",
      "reportsTo": null,
      "jobTitle": "Developer"
  }
  ```

#### 4. **Update Employee**
- **Endpoint**: `PUT /api/employees/{id}`
- **Request Payload**:
  ```json
  {
      "employeeNumber": 1,
      "lastName": "Doe",
      "firstName": "John",
      "extension": "456",
      "email": "john.doe@example.com",
      "officeCode": "1",
      "reportsTo": null,
      "jobTitle": "Senior Developer"
  }
  ```
- **Example Request**:
  ```
  PUT http://localhost:8080/api/employees/1
  Content-Type: application/json

  {
      "employeeNumber": 1,
      "lastName": "Doe",
      "firstName": "John",
      "extension": "456",
      "email": "john.doe@example.com",
      "officeCode": "1",
      "reportsTo": null,
      "jobTitle": "Senior Developer"
  }
  ```

#### 5. **Delete Employee**
- **Endpoint**: `DELETE /api/employees/{id}`
- **Request**: Replace `{id}` with the employee number.
- **Example Request**:
  ```
  DELETE http://localhost:8080/api/employees/1
  ```

### Summary of Requests

1. **Get All Employees**: 
   - **Method**: GET
   - **URL**: `/api/employees`
   
2. **Get Employee by ID**: 
   - **Method**: GET
   - **URL**: `/api/employees/{id}`
   
3. **Create Employee**: 
   - **Method**: POST
   - **URL**: `/api/employees`
   - **Body**: JSON object with employee details.
   
4. **Update Employee**: 
   - **Method**: PUT
   - **URL**: `/api/employees/{id}`
   - **Body**: JSON object with updated employee details.
   
5. **Delete Employee**: 
   - **Method**: DELETE
   - **URL**: `/api/employees/{id}`

### Testing in Postman

1. Open Postman.
2. Choose the appropriate HTTP method (GET, POST, PUT, DELETE).
3. Enter the URL based on the endpoint you want to test.
4. For POST and PUT requests, select "Body" and choose "raw", then set the type to "JSON" and enter the JSON payload.
5. Click "Send" to make the request and view the response.

Feel free to modify the payloads to test various scenarios, such as creating employees with different attributes or updating existing employees! If you need further assistance, let me know!