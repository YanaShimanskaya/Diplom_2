import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.AppConfig.*;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.example.UserController.createNewUser;
import static org.example.UserController.deleteUser;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Check new user registration")
    @Description("New user registration should return 200 and body")
    public void newUserRegistrationTest() {
        Response response = createNewUser(CREATE_USER);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(CREATE_USER.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and().body("accessToken", startsWith("Bearer"))
                .and().body("refreshToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check same user registration")
    @Description("Same user registration should return 403 and message")
    public void sameUserRegistrationTest() {
        createNewUser(CREATE_USER);
        Response response = createNewUser(CREATE_USER);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @After
    public void cleanUp() {
        deleteUser(LOGIN_USER);
    }
}