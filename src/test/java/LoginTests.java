import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.SignUpLoginRequest;
import models.response.LoginResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static objectBuilder.SignUpLoginObjectBuilder.createBodyForSignUpLogin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTests {

    private SignUpLoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@gmail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        Response signUpLoginResponse = new EVPrimeClient()
                .signUp(loginRequest);

    }

    @Test
    public void successfulLogin() {
        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        LoginResponse body = loginResponse.as(LoginResponse.class);

        assertEquals(200, loginResponse.statusCode());
        assertNotNull(body.getToken());
    }

    @Test
    public void unsuccessfulLoginWrongEmail() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail("emailValue1provider.com")
                .setPassword("passwordvalue1")
                .createRequest();

        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        assertEquals(401, loginResponse.statusCode());
        assertEquals("Authentication failed.", loginResponse.jsonPath().getString("message"));

        // API returns 401 instead of 422 Unprocessable Entity for invalid email format
    }

    @Test
    public void unsuccessfulLoginWrongPassword() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail("emailValue1@provider.com")
                .setPassword("passwordvalue111")
                .createRequest();

        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        assertEquals(422, loginResponse.statusCode());
        assertEquals("Invalid credentials.", loginResponse.jsonPath().getString("message"));

    }

    @Test
    public void unsuccessfulLoginUserNotFound() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@gmail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        assertEquals(401, loginResponse.statusCode());
        assertEquals("Authentication failed.", loginResponse.jsonPath().getString("message"));

    }
    @Test
    public void unsuccessfulLoginEmptyPassField() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@gmail.com")
                .setPassword("")
                .createRequest();

        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        assertEquals(401, loginResponse.statusCode());
        assertEquals("Authentication failed.", loginResponse.jsonPath().getString("message"));

        // User does not exist and API returns 401 Authentication failed
    }

    @Test
    public void unsuccessfulLoginEmptyEmailField() {
        loginRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail("")
                .setPassword("passwordvalue1")
                .createRequest();

        Response loginResponse = new EVPrimeClient()
                .login(loginRequest);

        assertEquals(401, loginResponse.statusCode());
        assertEquals("Authentication failed.", loginResponse.jsonPath().getString("message"));


    }
}


