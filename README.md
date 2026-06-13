# API Design document

# 1\. Database diagram

Draw an ER diagram of your schema: tables, columns (with types), primary and foreign keys, and the relationships between them.

**Suggested tool:** [dbdiagram.io](https://dbdiagram.io/) — free, fast, and made for ER diagrams. 

![DB](https://iili.io/CCLeglp.png)

# 2\. Endpoint summary

List every endpoint you plan to build in the following list:  
(Replace the example endpoints with your own)

| Method | Path | Description |
| :---- | :---- | :---- |
| **POST** | /users | Create a new user. |
| **GET** | /users | Get all users. |
| **GET** | /users/{id} | Get a single user's details by ID. |
| **PUT** | /users/{id} | Update existing user details. |
| **DELETE** | /users/{id} | Delete User by ID. |
| **GET** | /projects | Get all Projects. |
| **POST** | /tickets | Create a new ticket attached to a project. |
| **PUT** | /tickets/{id} | Update ticket fields (title, text, status, project), also update\_date |
| **GET** | /tickets/{id} | Get a single ticket’s details by ID. |
| **GET** | /tickets | Get all Projects and use the Query String to search by title, description, or status. No filter returns all. |
| **POST** | /tickets/{id}/assignees | Add a user to a ticket  |
| **DELETE** | /tickets/{id}/assignees | Remove a user from a ticket  |

# 3\. Endpoint description

In this section, list every endpoint in depth. The first endpoint is an example \- remove it before submission.

# **USERS**

### POST /users 

Add a new user to the database. Generate a random user ID in a string format.

| Endpoint | /users |
| :---- | :---- |
| **Method** | POST |
| **Request body** | `{`     `"name":` `"string",`     `"email":` `"string", }` |
| **Response body** | `{     "id": "integer",`         `"name":` `"string",`          `"email":` `"string", }` |
| **Validations** | Name is required. Name must be at least 3 characters. Email is required. Email must be valid. Email must be unique. |

### GET /users

Return all users.

| Endpoint | /users |
| :---- | :---- |
| **Method** | GET |
| **Request body** |  |
| **Response body** | `[   {     "id": 1,     "name": "string",     "email": "string"   } ]` |
| **Validations** |  |

### GET /users/{id}

Get a single user.

| Endpoint | /users/{id} |
| :---- | :---- |
| **Method** | GET |
| **Request body** |  |
| **Response body** | `{   "id": 1,   "name": "Salem",   "email": "salem@example.com" }` |
| **Validations** | Valid ID, user must exist. |

### PUT /users/{id}

Update existing user.

| Endpoint | /users/{id} |
| :---- | :---- |
| **Method** | PUT |
| **Request body** | `{   "name": "Salem Updated",   "email": "salem2@example.com" }` |
| **Response body** | `{   "message": "User update successfully" }` |
| **Validations** | User must exist. Name minimum of 3 chars. Email valid. Email unique. |

### DELETE /users/{id}

Delete user.

| Endpoint | /users/{id} |
| :---- | :---- |
| **Method** | DELETE |
| **Request body** |  |
| **Response body** | `HTTP 204` |
| **Validations** | User must exist. |

# **PROJECTS** 

### GET /projects

Return all projects.

| Endpoint | /projects |
| :---- | :---- |
| **Method** | GET |
| **Request body** |  |
| **Response body** | `[   {     "id": 1,     "name": "AWS Migration"   } ]` |
| **Validations** |  |

# **TICKETS** 

### POST /tickets

Create a new ticket.

| Endpoint | /tickets |
| :---- | :---- |
| **Method** | POST |
| **Request body** | `{   "title": "Server not responding",   "description": "Production server unreachable",   "projectId": 1 }` |
| **Response body** | `{   "id": 1,   "title": "Server not responding",   "description": "Production server unreachable",   "projectId": 1,   "status": "open",   "createdAt": "2026-06-12T12:00:00" }` |
| **Validations** | Title required. Title minimum 3 chars. Project must exist. Status must be: open In progress closed I’ll use enrum in Java for status. |

### GET /tickets

Get all Projects.

### GET /tickets?status=open\&title=Bug\&description=server

and use the Query String to search by title, description, or status. No filter returns all.

| Endpoint | /tickets |
| :---- | :---- |
| **Method** | GET |
| **Request body** |  |
| **Response body** | `[   {     "id": 1,     "title": "Critical Bug in login flow",     "description": "Users are getting a 500 error on the login screen.",     "status": "open",     "projectId": 1,     "createdAt": "2026-06-12T12:00:00Z",     "updatedAt": null,     "assignedBy": [       {         "id": 10,         "name": "salem"       },       {         "id": 11,         "name": "ali"       }     ]   } ]` |
| **Validations** | status: Must be a valid status string. |

### GET /tickets/{id}

Get ticket details.

| Endpoint | /tickets/{id} |
| :---- | :---- |
| **Method** | GET |
| **Request body** |  |
| **Response body** | `{   "id": 1,   "title": "Server not responding",   "description": "description",   "status": "open",   "projectId": 1,   "createdAt": "2026-06-12T12:00:00",   "updatedAt": null   “assigneBy” : [ { salem, ali } ] }` |
| **Validations** |  |

### PUT /tickets/{id}

Update ticket.

| Endpoint | /tickets/{id} |
| :---- | :---- |
| **Method** | PUT |
| **Request body** | `{   "title": "Server down",   "description": "Updated description",   "status": "in_progress",   "projectId": 1 }` |
| **Response body** | `{   "message": "Ticket update successfully" }` |
| **Validations** | Ticket exists. Status must be: Open In progress closed 3.Project exists. 4\. Update\_date, automatically |

### POST /tickets/{ticketId}/assignees

Assign a user to a ticket.

| Endpoint | /tickets/{ticketId}/assignees/{userId} |
| :---- | :---- |
| **Method** | POST |
| **Request body** | `{   "userId": 5 }` |
| **Response body** | `{   "message": "User assigned successfully" }` |
| **Validations** | Ticket exists. User exists. User is not already assigned. |

### DELETE /tickets/{ticketId}/assignees/{userId}

Remove the user from the ticket.

| Endpoint | /tickets/{ticketId}/assignees/{userId} |
| :---- | :---- |
| **Method** | DELETE |
| **Request body** |  |
| **Response body** | `{   "message": "User removed successfully" }` |
| **Validations** | Ticket exists. User exists. Assignment exists. |

# 4\. Email notifications

Describe your email plan in a few lines:

* **When** is an email sent? Every time a ticket is updated   
* **Who** receives it? All users currently assigned to that ticket.  
* **What** does the email contain? The ticket ID, ticket title, ticket description, status, and user assigned to the ticket.  
* **What happens if sending fails** — does the request still succeed? Yes the request succeed.

[image1]: <data:image/png;base64,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>