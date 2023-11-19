package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.AppConfig.*;
import static org.hamcrest.Matchers.equalTo;

public class UserController {
    @Step("Создание нового пользователя")

    public static Response createNewUser(CreateUser createUser) {
        Response response =
                given()
                        .relaxedHTTPSValidation()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .body(createUser)
                        .when()
                        .post(CREATE_USER_PATH);
        return response;
    }

    @Step("Удаление залогированного пользователя")
    public static Response deleteUser(LoginUser loginUser) {
        Response response =
                given()
                        .relaxedHTTPSValidation()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .auth().oauth2(getUserToken(loginUser))
                        .when()
                        .delete(DELETE_USER_PATH);
        return response;
    }

    @Step("Удаление пользователя")

    public static Response deleteUser(String token) {
        Response response = given()
                .relaxedHTTPSValidation()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .auth().oauth2(token)
                .when()
                .delete(DELETE_USER_PATH);
        return response;
    }

    @Step("Получение токена пользователя")

    public static String getUserToken(LoginUser loginUser) {
        Response response = loginUser(loginUser);
        String accessToken = response.jsonPath().get("accessToken");
        return accessToken.replace("Bearer ", "");

    }

    @Step("Авторизация пользователя")

    public static Response loginUser(LoginUser loginUser) {
        return
                given()
                        .relaxedHTTPSValidation()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .body(loginUser)
                        .when()
                        .post(LOGIN_USER_PATH);
    }

    @Step("Изменение данных пользователя")
    public static Response changeUserData(LoginUser userBefore, ChangeUser changeUser, boolean useAuth) {
        String token;
        Response response;
        if (useAuth) {
            token = getUserToken(userBefore);
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .auth().oauth2(token)
                    .body(changeUser)
                    .when()
                    .patch(CHANGE_USER_PATH);
        } else {
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .body(changeUser)
                    .when()
                    .patch(CHANGE_USER_PATH);
        }
        return response;
    }

    @Step("Валидация данных пользователя")
    public static void checkInvalidCredential(LoginUser credential) {
        Response response = loginUser(credential);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
}