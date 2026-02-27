import client.EVPrimeClient;
import data.PostEventDataFactory;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.LoginResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static objectBuilder.PostUpdateEventObjectBuilder.createBodyForPostEvent;
import static objectBuilder.SignUpLoginObjectBuilder.createBodyForSignUpLogin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeleteEventTests {

    private SignUpLoginRequest loginRequest;
    private LoginResponse loginResponseBody;
    private PostUpdateEventRequest postEventRequest;
    private String eventId;
    private String message;

    @BeforeEach
    public void setUp() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@gmail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        new EVPrimeClient()
                .signUp(loginRequest);

        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        loginResponseBody = loginResponse.body().as(LoginResponse.class);

        postEventRequest = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("New event")
                .setImage("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful-spectrum-paint-brush-strokes-over-gm1758686294-544622150")
                .setDate("2026-01-01")
                .setLocation("Skopje")
                .setDescription("best event")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(postEventRequest, loginResponseBody.getToken());

        message = response.jsonPath().getString("message");

        eventId = message.substring(message.lastIndexOf(" ") + 1);
        assertNotNull(eventId);
    }

    @Test
    public void successfulDeleteEvent() {
        Response deleteResponse = new EVPrimeClient()
                .deleteEvent(eventId, loginResponseBody.getToken());

        assertEquals(200, deleteResponse.statusCode());

        String message = deleteResponse.jsonPath().getString("message");
        assertEquals("Successfully deleted the event with id: " + eventId, deleteResponse.jsonPath().getString("message"));
    }

    @Test
    public void deleteEventWithoutToken() {
        Response deleteResponse = new EVPrimeClient()
                .deleteEvent(eventId, null);

        assertEquals(401, deleteResponse.statusCode());
        assertEquals("Not authenticated.", deleteResponse.jsonPath().getString("message"));
    }

    @Test
    public void deleteEventWithNonExistingId() {
        String nonExistingId = "123";
        Response deleteResponse = new EVPrimeClient()
                .deleteEvent(nonExistingId, loginResponseBody.getToken());

        assertEquals(200, deleteResponse.statusCode());
        assertEquals("Successfully deleted the event with id: " + nonExistingId, deleteResponse.jsonPath().getString("message"));
// NOTE: The API returns a successful response when the event id does not exist
    }

    @Test
    public void deleteEventThatIsAlreadyDeleted() {
        new EVPrimeClient()
                .deleteEvent(eventId, loginResponseBody.getToken());

        Response deleteResponse = new EVPrimeClient()
                .deleteEvent(eventId, loginResponseBody.getToken());

        assertEquals(200, deleteResponse.statusCode());
        assertEquals("Successfully deleted the event with id: " + eventId, deleteResponse.jsonPath().getString("message"));

        //  NOTE: The API returns a successful response even the event with the same id is already deleted.
    }

    @Test
    public void deleteEventWithInvalidToken() {
        String invalidToken = "invalidToken";
        Response deleteResponse = new EVPrimeClient()
                .deleteEvent(eventId, invalidToken);

        assertEquals(401, deleteResponse.statusCode());
        assertEquals("Not authenticated.", deleteResponse.jsonPath().getString("message"));
    }

}