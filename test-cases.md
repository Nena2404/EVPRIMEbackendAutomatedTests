 
EVPrime USERS Test cases

SIGNUP

Test case 1: successful signup, 201 Created
- user is on the http://localhost:8080/users/signup
- user enters "emailValue1@provider.com" in the email field
- user enters  "passwordvalue1" in the password field
- user clicks the signup button
Expected result: "message": "User created.",
    "user": {
        "id": "2e1e4cef-dc34-408c-8f1a-f00217b7d4d6",
        "email": "emailValue123@provider.com"
    },
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImVtYWlsVmFsdWUxMjNAcHJvdmlkZXIuY29tIiwiaWF0IjoxNzY4NTMwNDQ2LCJleHAiOjE3Njg1MzQwNDZ9.jssPmJF50SDsrGUBQDHGe6Pme41Dh2CzmlXNn3-vSgM"
}

 Test case 2: unsuccessful signup, 422 Unprocessable Entity
- user is on the http://localhost:8080/users/signup
- user enters "emailValue1provider.com" in the email field
- user enters  "passwordvalue1" in the password field
-  user clicks the signup button
Expected result: "message": "User signup failed due to validation errors.",
    "errors": {
        "email": "Invalid email."


 Test case 3: unsuccessful signup, 422 Unprocessable Entity
Precondition: user with this email is already registered
- user is on the http://localhost:8080/users/signup
- user enters "emailValue1@provider.com" in the email field
- user enters  "passwordvalue1" in the password field
- user clicks the signup button
Expected result:  "message": "User signup failed due to validation errors.",
    "errors": {
        "email": "Email exists already."
--------------------------------------------------  
  
LOGIN
Test case 1: successful login, 200 OK
Precondition: user is already signed up
- user is on the localhost:8080/login
- user enters "emailValue1@provider.com" in the email field
- user enters "passwordvalue1" in the password field
- user clicks the login button
Expected result: "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImVtYWlsVmFsdWUxQHByb3ZpZGVyLmNvbSIsImlhdCI6MTc2ODUyNzU1MywiZXhwIjoxNzY4NTMxMTUzfQ.2iy7uqz7EqPJf9s3_r3AMj3j3DDWjFTS0nkOzmSRer0",
    "expirationTime": "2026-01-16T02:39:13.887Z"
}

Test case 2: unsuccessful login, 422 Unprocessable Entity
Precondition: user is already signed up
- user is on the  http://localhost:8080/login
- user enters "emailValue1provider.com" in the email field
- user enters "passwordvalue11" in the password field
- user clicks the login button

Expected result:  "message": "Invalid credentials.",
    "errors": {
        "credentials": "Invalid email or password entered."
    }
}

Test case 3: unsuccessful login, 422 Unprocessable Entity
Precondition: user is already signed up
- user is on the  http://localhost:8080/login
- user enters "emailValue1@provider.com" in the email field
- user enters "passwordvalue11" in the password field
- user clicks the login button
Expected result:
    "message": "Invalid credentials.",
    "errors": {
        "credentials": "Invalid email or password entered."
    }

Test case 4: unsuccessful login, 401 Unauthorized
- user is not signed up
- user enters "emailValue1@gmail.com" in the email field
- user enters "passwordvalue" in the password field
- user clicks the login button
Expected result:

{
    "message": "Authentication failed."
}
    	
Test case 5:  unsuccessful login, 401 Unauthorized
Precondition: user is already signed up
- user is on the  http://localhost:8080/login
- user enters "emailValue1@provider.com" in the email field
- user leaves empty password field
- user clicks the login button
Expected result:
{
    "message": "Authentication failed."
}


Test case 6:  unsuccessful login, 401 Unauthorized
Precondition: user is already signed up
- user is on the  http://localhost:8080/login
- user leaves empty email field
- user enters "passwordvalue" in the password field
- user clicks the login button
Expected result:
{
    "message": "Authentication failed."
}

------------------------------------------

EVENTS 
GET

Test case 1: successful get all events, 200 OK
- user sends GET request to http://localhost:8080/events
Expected result: response body is a list of events that contains the following fields: "id", "title", "image",  "date", "location",  "description"
          
Test case 2: successful get single event, 200 OK
- user sends GET request to http://localhost:8080/events{id}
Expected result: response body is a single event that contains the following fields: "id", "title", "image",  "date", "location",  "description"

Test case 3: successful get all events  without token , 200 OK
- user sends GET request to http://localhost:8080/events
Expected result: response body is a list of events that contains the following fields: "id", "title", "image",  "date", "location",  "description"

Test case 4: successful get single event  without token , 200 OK
- user sends GET request to http://localhost:8080/events{id}
Expected result: response body is a empty list []
NOTE: the API should return 401 Unauthorized, but current behavior returns 200 with empty list.

Test case 5 :get single event without ID, 200 OK
- user sends GET request to http://localhost:8080/events
Expected result: The API does not return an error. Returns the full list of events
       
Test case 6: unsuccessful get single event with non-existing ID, 200 OK 
- user sends GET request to http://localhost:8080/events/123
Expected result: response body is a empty list []



POST

Test case 1: successful create new event, 201 Created
Precondition: user is logged in and has a valid token
- user sends POST request to http://localhost:8080/events{
- request body contains: "title", "image", "date", "location", "description"
Expected result: "Successfully created an event with id"

Test case 2: create new event without title value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends POST request to http://localhost:8080/events
- request body contains: "image", "date", "location", "description"
Expected result: "message": "Adding the event failed due to validation errors.",
    "errors":
        "title": "Invalid title."

Test case 3: create new event without image value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends POST request to http://localhost:8080/events
- request body contains: "title", "date", "location", "description"
Expected result: "message": "Adding the event failed due to validation errors.",
    "errors":
        "image": "Invalid image."

Test case 4: create new event without date value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends POST request to http://localhost:8080/events
- request body contains: "title", "image", "location", "description"
Expected result: "message": "Adding the event failed due to validation errors.",
    "errors":
        "date": "Invalid date."

Test case 5: create new event without location value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends POST request to http://localhost:8080/events
- request body contains: "title", "image", "date", "description"
Expected result: "message": "Adding the event failed due to validation errors.",
    "errors":
        "description": "Invalid location."
*NOTE: When the "location" field is missing, the API returns a validation error under "description".
 
Test case 6: create new event without description value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends POST request to http://localhost:8080/events
- request body contains: "title", "image", "date", "location"
Expected result: "message": "Adding the event failed due to validation errors.",
    "errors":
        "description": "Invalid description."
   
Test case 7: create new event without token, 401 Unauthorized
Precondition: user is not authenticated (does not have a valid token)
- user sends POST request to http://localhost:8080/events
- request body contains: "title", "image", "date", "location", "description"
Expected result: "message": "Not authenticated."
  

PUT

Test case 1: successful update event, 201 Created
Precondition: user is logged in and has a valid token
- user sends PUT request to localhost:8080/events/{id}
- request body contains: "title", "image", "date", "location", "description"
Expected result: "message": "Successfully updated the event with id: {id}"

Test case 2: unsuccessful update event, no title value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends PUT request to localhost:8080/events/{id}
- request body contains:  "image", "date", "location", "description"
Expected result: "message": "Updating the event failed due to validation errors.",
    "errors":
        "title": "Invalid title."

Test case 3: unsuccessful update event, no image value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends PUT request to localhost:8080/events/{id}
- request body contains: "title", "date", "location", "description"
Expected result:  "message": "Updating the event failed due to validation errors.",
    "errors": 
        "image": "Invalid image."

Test case 4: unsuccessful update event, no date value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends PUT request to localhost:8080/events/{id}
- request body contains: "title", "image", "location", "description"
Expected result: "message": "Updating the event failed due to validation errors.",
    "errors":
        "date": "Invalid date."
 
Test case 5: unsuccessful update event, no location value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends PUT request to localhost:8080/events/{id}
- request body contains: "title", "image", "date", "description"
Expected result: "message": "Updating the event failed due to validation errors.",
    "errors": 
        "description": "Invalid location."
*NOTE: When the "location" field is missing, the API returns a validation error under "description".

Test case 6: unsuccessful update event, no description value, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends PUT request to localhost:8080/events/{id}
- request body contains: "title", "image", "date", "location"
Expected result: "message": "Updating the event failed due to validation errors.",
    "errors": 
        "description": "Invalid description."
    
Test case 7: unsuccessful update event, no valid token, 401 Unauthorized 
Precondition: user is not authenticated (does not have a valid token)
- user sends PUT request to localhost:8080/events/{id}
- request body contains: "title", "image", "date", "location", "description"
Expected result:  "message": "Not authenticated."

Test case 8: unsuccessful update event , non existing  id, 422 Unprocessable Entity
Precondition: user is logged in and has a valid token
- user sends PUT request to localhost:8080/events/99999999
- request body contains: "title", "description", "date", "image"
Expected result:  "message": "Updating the event failed due to validation errors."

*NOTE: The API returns a successful response (201 Created) even when a non-existing or invalid id is used.


DELETE

Test case 1: successful delete event, 200 OK
Precondition: user is logged in and has a valid token
- user sends DELETE request to localhost:8080/events/{id}
Expected result: "message": "Successfully deleted the event with id:{id}"

Test case 2: unsuccessful delete event without token, 401 Unauthorized
Precondition: user is not authenticated (does not have a valid token)
- user sends DELETE request to localhost:8080/events/{id}
Expected result: "message": "Not authenticated."

Test case 3: delete event with non-existing ID , 200 OK
Precondition: user is logged in and has a valid token
- user sends DELETE request to localhost:8080/events/123
Expected result: "message" :"Successfully deleted the event with id: 123"

*NOTE: The API returns a successful response when the event id does not exist

Test case 4: delete event that is already deleted, 200 OK
Precondition: user is logged in and has a valid token
- user deletes event with id {id}
- user sends DELETE request to localhost:8080/events/{id}
 Expected result: "message": "Successfully deleted the event with id: {id}"

*NOTE: The API returns a successful response even the event with the same id is already deleted.

Test case 5: delete event with invalid token, 401 Unauthorized
Precondition: user is logged in and has an invalid token
- user sends DELETE request to localhost:8080/events/{id}
Expected result: "message": "Not authenticated."
