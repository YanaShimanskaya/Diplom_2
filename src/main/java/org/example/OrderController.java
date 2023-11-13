package org.example;

import io.restassured.response.Response;

import static org.example.AppConfig.*;
import static io.restassured.RestAssured.given;

public class OrderController {
    public static Response newOrder(CreateOrder createOrder) {
        Response response =
                given()
                        .relaxedHTTPSValidation().log().all()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .body(createOrder)
                        .when()
                        .post(ORDERS_PATH);
        return response;
    }

    public static Response getOrder(LoginUser loginUser) {
        Response response =
                given()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .auth().oauth2(UserController.getUserToken(loginUser))
                        .when()
                        .get(ORDERS_PATH);
        return response;
    }

    public static Response getOrder(String token, boolean useAuth) {
        Response response;
        if (useAuth) {
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .auth().oauth2(token)
                    .when()
                    .get(ORDERS_PATH);
        } else {
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .when()
                    .get(ORDERS_PATH);
        }
        return response;
    }
}