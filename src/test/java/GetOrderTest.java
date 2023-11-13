import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.OrderController;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.AppConfig.*;
import static org.example.OrderController.newOrder;
import static org.example.UserController.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderTest {
    private static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Check valid user login")
    @Description("Valid user login should return 200 and orders")
    public void getUserOrdersTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        newOrder(CREATE_ORDER);
        Response response = OrderController.getOrder(token, true);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check invalid user login and message")
    @Description("Invalid user login should return 401 status code and message")
    public void getInvalidUserTest() {
        Response response = OrderController.getOrder(token,false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @AfterClass
    public static void cleanUp() {
        deleteUser(token);
    }
}