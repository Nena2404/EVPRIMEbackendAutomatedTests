import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import models.request.SignUpLoginRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static objectBuilder.SignUpLoginObjectBuilder.createBodyForSignUpLogin;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignUpTests {

    private SignUpLoginRequest signUpRequest;


    @BeforeEach
    public void setUp() {
        signUpRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + "@gmail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();
    }

    @Test
    public void successfulSignUp() {
        Response signUpResponse = new EVPrimeClient()
                .signUp(signUpRequest);

        assertEquals(201, signUpResponse.statusCode());

    }

    @Test
    public void unsuccessfulSignUpInvalidEmail() {
        signUpRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail("emailValue1provider.com")
                .setPassword("passwordvalue1")
                .createRequest();

        Response signUpResponse = new EVPrimeClient()
                .signUp(signUpRequest);

        String errorMessage = signUpResponse.asString();

        assertEquals(422, signUpResponse.statusCode());
        assertEquals("Invalid email.", signUpResponse.jsonPath().getString("errors.email"));
    }

    @Test
    public void unsuccessfulSignUpEmailExists() {
        signUpRequest = new SignUpLoginDataFactory(createBodyForSignUpLogin())
                .setEmail("emailValue1@provider.com")
                .setPassword("passwordvalue1")
                .createRequest();

        Response signUpResponse = new EVPrimeClient()
                .signUp(signUpRequest);

        assertEquals(422, signUpResponse.statusCode());
        assertEquals("Email exists already.", signUpResponse.jsonPath().getString("errors.email"));

    }
}
