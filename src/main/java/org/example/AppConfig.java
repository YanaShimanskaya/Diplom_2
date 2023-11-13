package org.example;
import com.github.javafaker.Faker;

public class AppConfig {
    public final static String APP_URL = "https://stellarburgers.nomoreparties.site";
    public final static String CREATE_USER_PATH = "/api/auth/register";
    public final static String LOGIN_USER_PATH = "/api/auth/login ";
    public final static String DELETE_USER_PATH = "/api/auth/user";
    public final static String CHANGE_USER_PATH = "/api/auth/user";
    public final static String ORDERS_PATH = "/api/orders";
    public final static String HEADER_CONTENT_TYPE_NAME = "Content-type";
    public final static String CONTENT_TYPE = "application/json";
    public static Faker faker = new Faker();
    public final static String EMAIL = "yana123@io.oi";
    public final static String PASSWORD = "qwerty123";
    public final static String NAME = "Yana";
    public final static CreateUser CREATE_USER = new CreateUser(EMAIL,PASSWORD,NAME);
    public final static LoginUser LOGIN_USER = new LoginUser(EMAIL,PASSWORD);
    public final static CreateOrder CREATE_ORDER = new CreateOrder(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6c"});
    public final static CreateOrder EMPTY_ORDER = new CreateOrder(new String[]{});
    public final static CreateOrder INVALID_INGREDIENT_ORDER = new CreateOrder(new String[]{"invalid23n0c5a71d1f83565bdaaa6d",});
    public final static ChangeUser CHANGE_USER_NAME = new ChangeUser(CREATE_USER.getEmail(), faker.name().fullName());
    public final static ChangeUser CHANGE_USER_EMAIL = new ChangeUser(EMAIL, CREATE_USER.getName());

}