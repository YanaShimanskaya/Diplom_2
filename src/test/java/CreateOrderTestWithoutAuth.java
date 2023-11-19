import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.example.AppConfig.APP_URL;
import static org.example.AppConfig.CREATE_ORDER;
import static org.example.OrderController.newOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTestWithoutAuth {
    @Before
    public void setUp() {
        RestAssured.baseURI = APP_URL;
    }

    @Test
    @DisplayName("Check Order creation without user Auth")
    @Description("Valid ingredients and without user Auth should return 200 and body")
    public void createOrderWithoutAuthValidIngredientsTest() {
        Response response = newOrder(CREATE_ORDER);
        response.then().assertThat().body("name", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }
}