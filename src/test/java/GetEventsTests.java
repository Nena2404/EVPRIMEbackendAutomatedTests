import client.EVPrimeClient;
import data.PostEventDataFactory;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.LoginResponse;
import models.response.PostUpdateDeleteEventRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static objectBuilder.PostUpdateEventObjectBuilder.createBodyForPostEvent;
import static objectBuilder.SignUpLoginObjectBuilder.createBodyForSignUpLogin;
import static org.junit.jupiter.api.Assertions.*;

public class GetEventsTests {

    private SignUpLoginRequest signUpRequest;
    private SignUpLoginRequest loginRequest;
    private LoginResponse loginResponseBody;
    private String id;
    private PostUpdateEventRequest postEventRequest;

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

        PostUpdateDeleteEventRequest postResponse = response.body().as(PostUpdateDeleteEventRequest.class);
        id = postResponse.getMessage().substring(39);

    }

    @Test
    public void getAllEvents() {
        Response getAllEvents = new EVPrimeClient()
                .getAllEvents(loginResponseBody.getToken());

        assertEquals(200, getAllEvents.statusCode());
    }

    @Test
    public void getEventById() {
        Response response = new EVPrimeClient()
                .getEventById(id, loginResponseBody.getToken());

        assertEquals(200, response.statusCode());

        assertEquals(id, response.jsonPath().getString("events[0].id"));
        assertEquals("New event", response.jsonPath().getString("events[0].title"));
        assertEquals("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful" +
                "-spectrum-paint-brush-strokes-over-gm1758686294-544622150", response.jsonPath().getString("events[0].image"));
        assertEquals("2026-01-01", response.jsonPath().getString("events[0].date"));
        assertEquals("Skopje", response.jsonPath().getString("events[0].location"));
        assertEquals("best event", response.jsonPath().getString("events[0].description"));

    }

    @Test
    public void successfulGetAllEventsWithoutToken() {
        Response response = new EVPrimeClient()
                .getAllEventsWithoutToken();

        assertEquals(200, response.statusCode());
        assertFalse(response.jsonPath().getList("events").isEmpty());
    }
    @Test
    public void successfulGetSingleEventWithoutToken() {
        Response response = new EVPrimeClient()
                .getEventByIdWithoutToken(id);

        assertEquals(200, response.statusCode());
        assertEquals(id, response.jsonPath().getString("events[0].id"));
    }

    @Test
    public void getSingleEventWithoutId(){
        String noId ="";
        Response response = new EVPrimeClient()
                .getEventById(noId,loginResponseBody.getToken());

        assertEquals(200, response.statusCode());
        assertFalse(response.jsonPath().getList("events").isEmpty());

        // The API does not return an error. Returns the full list of events
    }

    @Test
    public void unsuccessfulGetSingleEventWithNonExistingId() {
        String nonExistingId = "123";

        Response response = new EVPrimeClient()
                .getEventById(nonExistingId, loginResponseBody.getToken());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.jsonPath().getString("events.list"));
    }


    @AfterEach
    public void deleteEvent() {
        new EVPrimeClient()
                .deleteEvent(id, loginResponseBody.getToken());
        id = null;
    }
}
