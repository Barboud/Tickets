# API Design document

# 1\. Database diagram 

Draw an ER diagram of your schema: tables, columns (with types), primary and foreign keys, and the relationships between them.

**Suggested tool:** [dbdiagram.io](https://dbdiagram.io/) — free, fast, and made for ER diagrams. 

![DB](https://iili.io/CCLeglp.png)

## 2. Endpoint Summary

| Method | Path | Description |
| :---- | :---- | :---- |
| POST | /users | Create a new user |
| GET | /users | Get all users |
| GET | /users/{userId} | Get a single user by ID |
| PUT | /users/{userId} | Update an existing user |
| DELETE | /users/{userId} | Delete a user by ID |
| GET | /projects | Get all projects with ticket counts per status |
| POST | /tickets | Create a new ticket |
| GET | /tickets | Get all tickets, with optional filters by title, description, or status |
| GET | /tickets/{ticketId} | Get a single ticket by ID |
| PUT | /tickets/{ticketId} | Update a ticket |
| POST | /tickets/{ticketId}/assignees | Assign a user to a ticket |
| DELETE | /tickets/{ticketId}/assignees/{userId} | Remove a user from a ticket |
 
---

## 3. Endpoint Descriptions
 
---

### USERS
 
---

#### POST /users

Create a new user.

| | |
| :---- | :---- |
| **Endpoint** | /users |
| **Method** | POST |
| **Request body** | `{ "name": "string", "email": "string" }` |
| **Response body** | `{ "id": 1, "name": "string", "email": "string" }` |
| **Validations** | Name is required. Name must be at least 3 characters. Email is required. Email must be valid. Email must be unique. |
 
---

#### GET /users

Return all users.

| | |
| :---- | :---- |
| **Endpoint** | /users |
| **Method** | GET |
| **Request body** | |
| **Response body** | `[ { "id": 1, "name": "string", "email": "string" } ]` |
| **Validations** | |
 
---

#### GET /users/{userId}

Get a single user by ID.

| | |
| :---- | :---- |
| **Endpoint** | /users/{userId} |
| **Method** | GET |
| **Request body** | |
| **Response body** | `{ "id": 1, "name": "Salem", "email": "salem@example.com" }` |
| **Validations** | User must exist. |
 
---

#### PUT /users/{userId}

Update an existing user.

| | |
| :---- | :---- |
| **Endpoint** | /users/{userId} |
| **Method** | PUT |
| **Request body** | `{ "name": "Salem Updated", "email": "salem2@example.com" }` |
| **Response body** | `{ "message": "User updated successfully" }` |
| **Validations** | User must exist. Name minimum 3 characters. Email must be valid. Email must be unique. |
 
---

#### DELETE /users/{userId}

Delete a user by ID.

| | |
| :---- | :---- |
| **Endpoint** | /users/{userId} |
| **Method** | DELETE |
| **Request body** | |
| **Response body** | `{ "message": "User deleted successfully" }` |
| **Validations** | User must exist. |
 
---

### PROJECTS
 
---

#### GET /projects

Return all projects with ticket counts per status.

| | |
| :---- | :---- |
| **Endpoint** | /projects |
| **Method** | GET |
| **Request body** | |
| **Response body** | `[ { "id": 1, "name": "Project A", "open": 10, "inProgress": 5, "closed": 30 } ]` |
| **Validations** | |
 
---

### TICKETS
 
---

#### POST /tickets

Create a new ticket.

| | |
| :---- | :---- |
| **Endpoint** | /tickets |
| **Method** | POST |
| **Request body** | `{ "title": "Server not responding", "description": "Production server unreachable", "projectId": 1 }` |
| **Response body** | `{ "message": "Ticket created successfully" }` |
| **Validations** | Title is required. Title minimum 3 characters. Project must exist. Status is set automatically to `open`. |
 
---

#### GET /tickets

Get all tickets. Supports optional filters via query string.

`GET /tickets?status=open&text=server`

| | |
| :---- | :---- |
| **Endpoint** | /tickets |
| **Method** | GET |
| **Request body** | |
| **Response body** | `[ { "id": 1, "title": "Critical Bug in login flow", "description": "Users are getting a 500 error on the login screen.", "status": "open", "projectId": 1, "createdAt": "2026-06-12T12:00:00Z", "updatedAt": null, "assignees": [ { "id": 10, "name": "Salem" }, { "id": 11, "name": "Ali" } ] } ]` |
| **Validations** | Status must be a valid value: `open`, `in progress`, or `closed`. |
 
---

#### GET /tickets/{ticketId}

Get a single ticket by ID.

| | |
| :---- | :---- |
| **Endpoint** | /tickets/{ticketId} |
| **Method** | GET |
| **Request body** | |
| **Response body** | `{ "id": 1, "title": "Server not responding", "description": "description", "status": "open", "projectId": 1, "createdAt": "2026-06-12T12:00:00", "updatedAt": null, "assignees": [ { "id": 1, "name": "Salem", "email": "salem@example.com" }, { "id": 3, "name": "Max", "email": "max@example.com" } ] }` |
| **Validations** | Ticket must exist. |
 
---

#### PUT /tickets/{ticketId}

Update a ticket.

| | |
| :---- | :---- |
| **Endpoint** | /tickets/{ticketId} |
| **Method** | PUT |
| **Request body** | `{ "title": "Server down", "description": "Updated description", "status": "IN_PROGRESS", "projectId": 1 }` |
| **Response body** | `{ "message": "Ticket updated successfully" }` |
| **Validations** | Ticket must exist. Status must be a valid value: `OPEN`, `IN_PROGRESS`, or `CLOSED`. Project must exist. `updatedAt` is set automatically. |
 
---

#### POST /tickets/{ticketId}/assignees

Assign a user to a ticket.

| | |
| :---- | :---- |
| **Endpoint** | /tickets/{ticketId}/assignees |
| **Method** | POST |
| **Request body** | `{ "userId": 5 }` |
| **Response body** | `{ "message": "Assignee added successfully" }` |
| **Validations** | Ticket must exist. User must exist. User must not already be assigned. |
 
---

#### DELETE /tickets/{ticketId}/assignees/{userId}

Remove a user from a ticket.

| | |
| :---- | :---- |
| **Endpoint** | /tickets/{ticketId}/assignees/{userId} |
| **Method** | DELETE |
| **Request body** | |
| **Response body** | `{ "message": "Assignee removed successfully" }` |
| **Validations** | Ticket must exist. User must exist. Assignment must exist. |
 
---

## 4. Email Notifications

- **When** is an email sent? Every time a ticket is updated.
- **Who** receives it? All users currently assigned to that ticket.
- **What** does the email contain? The ticket ID, title, description, status, and current assignees.
- **What happens if sending fails?** The request still succeeds. The failure is logged to the console.