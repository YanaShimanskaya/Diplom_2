import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.CreateUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.example.AppConfig.*;
import static org.example.UserController.createNewUser;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateUserParamTest {
    private final CreateUser invalidUserCredentials;

    public CreateUserParamTest(CreateUser invalidUserCredentials) {
        this.invalidUserCredentials = invalidUserCredentials;
    }

    @Parameterized.Parameters
    public static Object[][] parametersUserInvalidVariable() {
        return new Object[][]{
                {new CreateUser("", PASSWORD, NAME)},
                {new CreateUser(EMAIL, "", NAME)},
                {new CreateUser(EMAIL, PASSWORD, "")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Check invalid user credentials registration")
    @Description("Invalid user credentials registration should return 403 and message")
    public void createInvalidUserCredentialsTest() {
        Response response = createNewUser(invalidUserCredentials);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
}