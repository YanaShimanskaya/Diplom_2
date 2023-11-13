import org.example.LoginUser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import static org.example.AppConfig.*;
import static org.apache.http.HttpStatus.*;
import static org.example.UserController.*;
import static org.hamcrest.Matchers.*;

public class LoginUserTest {
    private static String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
    }

    @Test
    @DisplayName("Check status code 200")
    @Description("Successful current user login response with body values")
    public void validUserLoginTest() {
        Response response = loginUser(LOGIN_USER);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("accessToken", startsWith("Bearer"))
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(LOGIN_USER.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check status code 401 for name")
    @Description("Invalid user name should return 401 status code")
    public void invalidLoginTest() {
        LoginUser invalidLogin = new LoginUser(faker.internet().emailAddress(), PASSWORD);
        checkInvalidCredential(invalidLogin);
    }
    @Test
    @DisplayName("Check status code 401 for password")
    @Description("Invalid password should return 401 status code")
    public void invalidPasswordTest() {
        LoginUser invalidPassword = new LoginUser(EMAIL,faker.internet().password());
        checkInvalidCredential(invalidPassword);
    }

    @After
    public void cleanUp() {
        deleteUser(token);
    }
}