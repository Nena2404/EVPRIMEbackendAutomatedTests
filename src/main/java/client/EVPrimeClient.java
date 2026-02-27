package client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;
import utili.Configuration;

public class EVPrimeClient {

    public Response signUp(SignUpLoginRequest requestBody) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(requestBody)
                .post(Configuration.SIGNUP)
                .thenReturn();

    }

    public Response login (SignUpLoginRequest requestBody) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(requestBody)
                .post(Configuration.LOGIN)
                .thenReturn();
    }

    public Response postEvent (PostUpdateEventRequest requestBody, String token) {
        return RestAssured
                .given()
                .header("Authorization","Bearer " + token)
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(requestBody)
                .post(Configuration.EVENTS)
                .thenReturn();
    }

    public Response putUpdateEvent (PostUpdateEventRequest requestBody, String id, String token) {
        return RestAssured
                .given()
                .header("Authorization","Bearer " + token)
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(requestBody)
                .put(Configuration.EVENTS + "/" + id)
                .thenReturn();
    }

    public Response getAllEvents ( String token) {
        return RestAssured
                .given()
                .header("Authorization","Bearer " + token)
                .contentType(ContentType.JSON)
                .when().log().all()
                .get(Configuration.EVENTS)
                .thenReturn();
    }

    public Response getEventById ( String id, String token) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().log().all()
                .get(Configuration.EVENTS + "/" + id)
                .thenReturn();
    }

    public Response deleteEvent (String id, String token) {
        return RestAssured
                .given()
                .header("Authorization","Bearer " + token)
                .contentType(ContentType.JSON)
                .when().log().all()
                .delete(Configuration.EVENTS + "/" + id)
                .thenReturn();
    }

    public Response getEventByIdWithoutToken (String id) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .get(Configuration.EVENTS + "/" + id)
                .thenReturn();
    }

    public Response getAllEventsWithoutToken () {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .get(Configuration.EVENTS)
                .thenReturn();
    }

    public Response createEvent( PostUpdateEventRequest request,String token) {
        return RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().log().all()
                .post(Configuration.EVENTS)
                .thenReturn();

    }
    public Response putUpdateEventWithoutToken( PostUpdateEventRequest request, String id) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().log().all()
                .put(Configuration.EVENTS + "/" + id)
                .thenReturn();
    }
}

