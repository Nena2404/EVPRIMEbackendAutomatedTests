BACKEND

EVPrime USERS analysis

SIGNUP
- POST - create new user -
Description: before logging in and creating events, the user must sign up using an email and password
Response body: "user": "id": "{}", "token"
Expected response: "User created.", 201
Negative scenario :
   
- POST - create a new user with an email that is already registered -
Description: before logging in and creating events, the user must sign up using an email and password
Response body: "User signup failed due to validation errors.",
Expected response: "Email exists already." 422
Negative scenario : user cannot sign up with an already registered email

 - POST - trying to create new user, but already exist -
Description: before logging in and creating events, the user must sign up using an email and password
Response body: "User signup failed due to validation errors.",
Expected response: "Invalid email.", 422
Negative scenario: user cannot sign up with an invalid email format (missing @ symbol)


LOGIN
- POST user login -
Description: after signing up, you must log in using the same email and password
Response body: "token": value with expiration Time: e.g.: "2026-01-16T02:39:13.887Z"
Expected response: "expirationTime": "2026-01-16T02:39:13.887Z", 200
Negative scenario:"Authentication failed.", because the token is missing and the user cannot log in
    

- POST invalid username or password -
Description: attempt to log in with an incorrect email or password
Response body: "Invalid credentials.",
Expected response: "credentials": "Invalid email or password entered." 422
Negative scenario: unable to log in
        

-POST wrong password value - 
Description: attempt to log in with an incorrect password
Response body: "Invalid credentials.",
Expected response:"Invalid email or password entered. "422
Negative scenario: unable to log in

 
-------------------------------------------------------------

EVPrime EVENTS analysis

GET
- GET all events - 
Description: get list with all events
Response body: list with events and information about them  => title, image, date, location, description
Expected response: Status 200
Negative scenario : If there are no events, an empty list is returned.

- GET single event {id} -
Description: event with information and ID 
Response body: all information for the event with current ID
Expected response: Status 200
Negative scenario :event with the given ID does not exist, 404


POST
-POST new event-
Description: create an event with all needed information => title, image, date, location, description
Response body: Successfully created an event with id
Expected response: Status 201
Negative scenario :response not authorized, no token value, 401

-POST new event without TITLE value -		   
Description: create an event without title name
Response body: Adding the event failed due to validation errors.
Expected response: "Invalid title." 422
Negative scenario : the event cannot be created

- POST new event without IMAGE URL -	
Description: create an event without image URL
Response body: Adding the event failed due to validation errors.
Expected response: "Invalid image." 422
Negative scenario : the event cannot be created

- POST new event without DATE value -
Description: create an event without date information
Response body: Adding the event failed due to validation errors.
Expected response: "Invalid date." 422
Negative scenario : the event cannot be created

- POST new event without LOCATION value -
Description: create an event without information about location
Response body: Adding the event failed due to validation errors.
Expected response: "Invalid location." 422
Negative scenario : the event cannot be created

- POST new event without DESCRIPTION value -
Description: creating an event without description 
Response body: Adding the event failed due to validation errors.
Expected response: "Invalid description." 422
Negative scenario : the event cannot be created


PUT
- PUT change the title in already created event with title name "New event" -
Description: updating the name of the event that is already created, the new title name is "New event coming"
Response body: "Successfully updated the event with id: {}"
Expected response: 201
Negative scenario : response not authorized, no token value 

- PUT event with no title value -
Description: updating an event without title name
Response body: "Updating the event failed due to validation errors.",
Expected response: "Invalid title." , 422
Negative scenario : the event cannot be updated

- PUT event without image URL
Description: updating an event without image 
Response body: "Updating the event failed due to validation errors.",
Expected response:  "Invalid image." 422
Negative scenario :  the event cannot be updated

- PUT event without date value -
Description: updating an event without information about the date
Response body: "Updating the event failed due to validation errors.",
Expected response: "Invalid date." , 422
Negative scenario : the event cannot be updated

- PUT event without location value -
Description: updating an event without information about the location
Response body: "Updating the event failed due to validation errors.", 
Expected response: "Invalid location." , 422
Negative scenario : the event cannot be updated

- PUT event without description value -
Description: updating an event without description
Response body: "Updating the event failed due to validation errors.",
Expected response: "Invalid description.", 422
Negative scenario : the event cannot be updated

- PUT event without token value -
Description: trying to make an update, but without token value
Response body: "Not authenticated."
Expected response: "Not authenticated.", 401
Negative scenario :  the event cannot be updated


DELETE
- DELETE an event -
Description: delete an event that is already created
Response body: "Successfully deleted the event with id: {}"
Expected response: "Successfully deleted the event with id: {}", 200
Negative scenario :  response not authorized, no token value 

- DELETE an event without token value -
Description: attempt to delete an event without authentication token
Response body: "Successfully deleted the event with id: {}"
Expected response: "Successfully deleted the event with id: {}"
Negative scenario :  response not authorized, no token value 

**
- DELETE an event with ID that does not exist -
Description: delete an event that is already created
Response body: "Successfully deleted the event with id: {}"
Expected response: "Successfully deleted the event with id: {}"
Negative scenario :  
** NOTE: The API allows deleting the same ID multiple times and does not return an error


