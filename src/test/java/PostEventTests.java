import client.EVPrimeClient;
import data.PostEventDataFactory;
import data.SignUpLoginDataFactory;
import database.DBClient;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import models.response.LoginResponse;
import models.response.PostUpdateDeleteEventRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static objectBuilder.PostUpdateEventObjectBuilder.createBodyForPostEvent;
import static objectBuilder.SignUpLoginObjectBuilder.createBodyForSignUpLogin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostEventTests {

    private SignUpLoginRequest loginRequest;
    private LoginResponse loginResponseBody;
    private PostUpdateEventRequest postEventRequest;
    private static String id;
    private DBClient dbClient = new DBClient();

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

    }

    @Test
    public void successfulPostEventTest() throws SQLException {
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

        assertEquals(201, response.statusCode());
        assertTrue(postResponse.getMessage().contains("Successfully created an event with id: " + id));
        assertEquals(postEventRequest.getTitle(), dbClient.getEventFromDB(id).getTitle());
        assertEquals(postEventRequest.getImage(), dbClient.getEventFromDB(id).getImage());
        assertEquals(postEventRequest.getDate(), dbClient.getEventFromDB(id).getDate());
        assertEquals(postEventRequest.getLocation(), dbClient.getEventFromDB(id).getLocation());
        assertEquals(postEventRequest.getDescription(), dbClient.getEventFromDB(id).getDescription());

    }

    @Test
    public void createEventWithoutTitle() {
        postEventRequest = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("")
                .setImage("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful-spectrum-paint-brush-strokes-over-gm1758686294-544622150")
                .setDate("2026-01-01")
                .setLocation("Skopje")
                .setDescription("best event")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(postEventRequest, loginResponseBody.getToken());

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", response.jsonPath().getString("message"));
        assertEquals("Invalid title.", response.jsonPath().getString("errors.title"));
    }

    @Test
    public void createEventWithoutImage() {
        postEventRequest = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("New event")
                .setImage("")
                .setDate("2026-01-01")
                .setLocation("Skopje")
                .setDescription("best event")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(postEventRequest, loginResponseBody.getToken());

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", response.jsonPath().getString("message"));
        assertEquals("Invalid image.", response.jsonPath().getString("errors.image"));
    }

    @Test
    public void createEventWithoutDate() {
        postEventRequest = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("New event")
                .setImage("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful-spectrum-paint-brush-strokes-over-gm1758686294-544622150")
                .setDate("")
                .setLocation("Skopje")
                .setDescription("best event")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(postEventRequest, loginResponseBody.getToken());

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", response.jsonPath().getString("message"));
        assertEquals("Invalid date.", response.jsonPath().getString("errors.date"));

    }

    @Test
    public void createEventWithoutLocation() {
        postEventRequest = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("New event")
                .setImage("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful-spectrum-paint-brush-strokes-over-gm1758686294-544622150")
                .setDate("2026-01-01")
                .setLocation("")
                .setDescription("best event")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(postEventRequest, loginResponseBody.getToken());

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", response.jsonPath().getString("message"));
        assertEquals("Invalid location.", response.jsonPath().getString("errors.description"));

        // NOTE: Location error is returned under 'description' instead of 'location'.
    }

    @Test
    public void createEventWithoutDescription() {
        postEventRequest = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("New event")
                .setImage("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful-spectrum-paint-brush-strokes-over-gm1758686294-544622150")
                .setDate("2026-01-01")
                .setLocation("Skopje")
                .setDescription("")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(postEventRequest, loginResponseBody.getToken());

        assertEquals(422, response.statusCode());
        assertEquals("Adding the event failed due to validation errors.", response.jsonPath().getString("message"));
        assertEquals("Invalid description.", response.jsonPath().getString("errors.description"));
    }

    @Test
    public void createEventWithoutToken() {
        postEventRequest = new PostEventDataFactory(createBodyForPostEvent())
                .setTitle("New event")
                .setImage("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful-spectrum-paint-brush-strokes-over-gm1758686294-544622150")
                .setDate("2026-01-01")
                .setLocation("Skopje")
                .setDescription("best event")
                .createRequest();

        Response response = new EVPrimeClient()
                .postEvent(postEventRequest, null);

        assertEquals(401, response.statusCode());
        assertEquals("Not authenticated.", response.jsonPath().getString("message"));

    }
    // uste eden test so nevaliden token // cisto kako predlog

    @AfterEach
    public void deleteEvent() throws SQLException {
        new EVPrimeClient()
                .deleteEvent(id, loginResponseBody.getToken());
        id = null;
    }
}
