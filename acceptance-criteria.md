ACCEPTANCE CRITERIA – BACKEND

SIGNUP

Scenario 1: successful signup
GIVEN user is on /users/signup
WHEN user enters valid email and password and clicks signup
THEN user is created and response contains user data and token

Scenario 2: invalid email format
GIVEN user is on /users/signup
WHEN user enters invalid email and valid password and clicks signup
THEN system shows validation error "Invalid email."

Scenario 3: email already exists
GIVEN user with this email already exists
WHEN user tries to signup with same email and password
THEN system shows validation error "Email exists already."

LOGIN

Scenario 1: successful login
GIVEN user is already registered
WHEN user enters valid email and password and clicks login
THEN system returns token and expiration time

Scenario 2: invalid email format
GIVEN user is on /login
WHEN user enters invalid email and password
THEN system shows error "Invalid credentials."

Scenario 3: wrong password
GIVEN user is already registered
WHEN user enters correct email and wrong password
THEN system shows error "Invalid credentials."

Scenario 4: user not registered
GIVEN user is not registered
WHEN user tries to login with email and password
THEN system shows "Authentication failed."

Scenario 5: empty password field
GIVEN user is on /login
WHEN user enters email and leaves password empty
THEN system shows "Authentication failed."

Scenario 6: empty email field
GIVEN user is on /login
WHEN user enters password and leaves email empty
THEN system shows "Authentication failed."


EVENTS – GET

Scenario 1: get all events
GIVEN user sends GET request to /events
WHEN request is valid
THEN system returns list of events

Scenario 2: get single event
GIVEN user sends GET request to /events/{id}
WHEN event exists
THEN system returns event details

Scenario 3: get all events without token
GIVEN user is not authenticated
WHEN user sends GET request to /events
THEN system returns list of events

Scenario 4: get single event without token (BUG)
GIVEN user is not authenticated
WHEN user sends GET request to /events/{id}
THEN system returns empty list instead of 401

Scenario 5: get events without id
GIVEN user sends GET request to /events
WHEN no id is provided
THEN system returns full list of events

Scenario 6: non-existing event id
GIVEN user sends GET request to /events/{id}
WHEN id does not exist
THEN system returns empty list

EVENTS – POST

Scenario 1: create event successfully
GIVEN user is logged in with valid token
WHEN user sends POST request with all required fields
THEN event is created successfully

Scenario 2: missing title
GIVEN user is logged in
WHEN user sends request without title
THEN system shows validation error "Invalid title."

Scenario 3: missing image
GIVEN user is logged in
WHEN user sends request without image
THEN system shows validation error "Invalid image."

Scenario 4: missing date
GIVEN user is logged in
WHEN user sends request without date
THEN system shows validation error "Invalid date."

Scenario 5: missing location (BUG)
GIVEN user is logged in
WHEN user sends request without location
THEN system shows validation error under description

Scenario 6: missing description
GIVEN user is logged in
WHEN user sends request without description
THEN system shows validation error "Invalid description."

Scenario 7: without token
GIVEN user is not authenticated
WHEN user sends POST request
THEN system shows "Not authenticated."

EVENTS – PUT

Scenario 1: update event successfully
GIVEN user is logged in with valid token
WHEN user updates event with valid data
THEN event is updated successfully

Scenario 2: missing title
GIVEN user is logged in
WHEN user updates event without title
THEN system shows validation error

Scenario 3: missing image
GIVEN user is logged in
WHEN user updates event without image
THEN system shows validation error

Scenario 4: missing date
GIVEN user is logged in
WHEN user updates event without date
THEN system shows validation error

Scenario 5: missing location (BUG)
GIVEN user is logged in
WHEN user updates event without location
THEN system returns error under description

Scenario 6: missing description
GIVEN user is logged in
WHEN user updates event without description
THEN system shows validation error

Scenario 7: without token
GIVEN user is not authenticated
WHEN user sends PUT request
THEN system shows "Not authenticated."

Scenario 8: non-existing id (BUG)
GIVEN user is logged in
WHEN user updates event with non-existing id
THEN system returns success instead of error

EVENTS – DELETE

Scenario 1: delete event successfully
GIVEN user is logged in with valid token
WHEN user deletes event
THEN event is removed

Scenario 2: without token
GIVEN user is not authenticated
WHEN user sends DELETE request
THEN system shows "Not authenticated."

Scenario 3: non-existing id (BUG)
GIVEN user is logged in
WHEN user deletes non-existing event
THEN system returns success

Scenario 4: already deleted event (BUG)
GIVEN event is already deleted
WHEN user deletes same event again
THEN system returns success

Scenario 5: invalid token
GIVEN user has invalid token
WHEN user sends DELETE request
THEN system shows "Not authenticated."





