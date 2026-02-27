import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import database.DBClient;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.LoginResponse;
import objectBuilder.PostUpdateEventObjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utili.Configuration;

import static objectBuilder.SignUpLoginObjectBuilder.createBodyForSignUpLogin;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PutEventTests {

    private SignUpLoginRequest loginRequest;
    private LoginResponse loginResponseBody;
    private PostUpdateEventRequest postEventRequest;
    private  String id;
    private DBClient dbClient = new DBClient();
    private String token;

    @BeforeEach
    public void setUp() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail(Configuration.USER_MAIL) // ili(RandomStringUtils.randomAlphanumeric(10) + "@gmail.com"
                .setPassword(Configuration.USER_PASSWORD)  // RandomStringUtils.randomAlphanumeric(10)
                .createRequest();

        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        loginResponseBody = loginResponse.body().as(LoginResponse.class);

        postEventRequest = PostUpdateEventObjectBuilder.createBodyForPostEvent();

        token = loginResponseBody.getToken();

        Response createEvent = new EVPrimeClient()
                .createEvent(postEventRequest, loginResponseBody.getToken());

        String message = createEvent.jsonPath().getString("message");
        id = createEvent.jsonPath().getString("id");

    }

    @Test
    public void successfulUpdateEvent() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, id, loginResponseBody.getToken());

        assertEquals(201, updateEventResponse.statusCode());

    }

    @Test
    public void unsuccessfulUpdateEventNoTitle() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        putUpdateRequest.setTitle("");

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, id, loginResponseBody.getToken());

        assertEquals(422, updateEventResponse.statusCode());
        assertEquals("Invalid title.", updateEventResponse.jsonPath().getString("errors.title"));

    }

    @Test
    public void unsuccessfulUpdateEventNoImageURL() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        putUpdateRequest.setImage("");

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, id, loginResponseBody.getToken());

        assertEquals(422, updateEventResponse.statusCode());
        assertEquals("Invalid image.", updateEventResponse.jsonPath().getString("errors.image"));

    }

    @Test
    public void unsuccessfulUpdateEventNoDateValue() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        putUpdateRequest.setDate("");

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, id, loginResponseBody.getToken());

        assertEquals(422, updateEventResponse.statusCode());
        assertEquals("Invalid date.", updateEventResponse.jsonPath().getString("errors.date"));
    }

    @Test
    public void unsuccessfulUpdateEventNoLocationValue() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        putUpdateRequest.setLocation("");

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, id, loginResponseBody.getToken());

        assertEquals(422, updateEventResponse.statusCode());
        assertEquals("Invalid location.", updateEventResponse.jsonPath().getString("errors.description"));

        // NOTE: Location error is returned under 'description' instead of 'location'.
    }

    @Test
    public void unsuccessfulUpdateEventNoDescriptionValue() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        putUpdateRequest.setDescription("");

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, id, loginResponseBody.getToken());

        assertEquals(422, updateEventResponse.statusCode());
        assertEquals("Invalid description.", updateEventResponse.jsonPath().getString("errors.description"));
    }

    @Test
    public void unsuccessfulUpdateEventInvalidToken() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        String invalidToken = "invalidToken123";

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, id, invalidToken);

        assertEquals(401, updateEventResponse.statusCode());
        assertEquals("Not authenticated.", updateEventResponse.jsonPath().getString("message"));
    }

    @Test
    public void unsuccessfulUpdateEventWithoutToken() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEventWithoutToken(putUpdateRequest, id);

        assertEquals(401, updateEventResponse.statusCode());
        assertEquals("Not authenticated.", updateEventResponse.jsonPath().getString("message"));
    }

    @Test
    public void unsuccessfulUpdateEventWithoutId() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        String emptyId = "";

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, emptyId, token);

        assertEquals(404, updateEventResponse.statusCode());
    }

    @Test
    public void updateEventWithNonExistingId() {
        PostUpdateEventRequest putUpdateRequest =
                PostUpdateEventObjectBuilder.createBodyForPostEvent();

        String nonExistingId = "999999";

        Response updateEventResponse = new EVPrimeClient()
                .putUpdateEvent(putUpdateRequest, nonExistingId, token);

        // NOTE: API returns 201 even when the event does not exist (ID is not validated).
        assertEquals(201, updateEventResponse.statusCode());
    }

    @AfterEach
    public void deleteEvent() {
        new EVPrimeClient()
                .deleteEvent(id, loginResponseBody.getToken());
    }
}