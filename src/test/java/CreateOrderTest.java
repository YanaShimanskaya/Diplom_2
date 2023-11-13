import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.CreateOrder;
import org.example.LoginUser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.AppConfig.*;
import static org.example.OrderController.newOrder;
import static org.example.UserController.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
    private static String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Check valid Order creation")
    @Description("Valid user Auth and ingredients should return 200 and body")
    public void createValidOrderTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(new LoginUser(CREATE_USER.getEmail(), CREATE_USER.getPassword()));
        Response response = newOrder(CREATE_ORDER);
        response.then().log().all().assertThat().body("name", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }



    @Test
    @DisplayName("Check Order creation with user Auth and without ingredients")
    @Description("Invalid ingredients and valid user Auth should return 400 and message")
    public void createOrderWithAuthValidIngredientsTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        CreateOrder createOrder = new CreateOrder(new String[]{});
        Response response = newOrder(createOrder);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Check Order creation without user Auth and ingredients")
    @Description("Without ingredients and without user Auth should return 400 and message")
    public void createOrderWithoutAuthAndIngredientsTest() {
        Response response = newOrder(EMPTY_ORDER);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Check Order creation with user Auth and invalid ingredients")
    @Description("Invalid ingredients and with user Auth should return 500")
    public void createOrderWithAuthInvalidIngredientsTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        Response response = newOrder(INVALID_INGREDIENT_ORDER);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Check Order creation without user Auth and invalid ingredients")
    @Description("Invalid ingredients and without user Auth should return 500")
    public void createOrderWithoutAuthInvalidIngredientsTest() {
        Response response = newOrder(INVALID_INGREDIENT_ORDER);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @AfterClass
    public static void cleanUp() {
        deleteUser(token);
    }
}