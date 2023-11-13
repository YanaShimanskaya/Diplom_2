import org.example.ChangeUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.AppConfig.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.UserController.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserTest {
    private static String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
    }

    @Test
    @DisplayName("Check changing User email with Auth")
    @Description("Invalid authorization should return 200 and body")
    public void changeUserEmailWithAuthTest() {
        loginUser(LOGIN_USER);
        ChangeUser changeUserEmail = new ChangeUser(EMAIL, CREATE_USER.getName());
        Response response = changeUserData(LOGIN_USER,changeUserEmail, true);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(changeUserEmail.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check changing User name with Auth")
    @Description("Invalid authorization should return 200 and body")
    public void changeUserNameWithAuthTest() {
        loginUser(LOGIN_USER);
        Response response = changeUserData(LOGIN_USER,CHANGE_USER_NAME, true);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(CREATE_USER.getEmail()))
                .and().body("user.name", equalTo(CHANGE_USER_NAME.getName()))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check changing User Email without Auth")
    @Description("Invalid authorization should return 401 and message")
    public void changeEmailWoAuthTest() {
        loginUser(LOGIN_USER);
        Response response = changeUserData(LOGIN_USER,CHANGE_USER_EMAIL, false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
    @Test
    @DisplayName("Check changing User name without Auth")
    @Description("Invalid authorization should return 401 and message")
    public void changeUserNameWoAuthTest() {
        loginUser(LOGIN_USER);
        Response response = changeUserData(LOGIN_USER,CHANGE_USER_NAME, false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void cleanUp() {
        deleteUser(token);
    }
}