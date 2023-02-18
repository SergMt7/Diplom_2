import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserLoggedTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private String invalidLogin = "12345@yandex.ru";
    private String invalidPassword = "QWERTY";

    @Before
    public void setUp() {
        user = UserGenerator.random();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginUserTest() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        ValidatableResponse loginResponse = userClient.login(Login.from(user), accessToken);
        loginResponse.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Попытка логина с неверным логином")
    public void loginUserInvalidLoginTest() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        user.setEmail(invalidLogin);
        ValidatableResponse loginResponse = userClient.login(Login.from(user), accessToken);
        loginResponse.assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    @Test
    @DisplayName("Попытка логина с неверным паролем")
    public void loginUserInvalidPasswordTest() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        user.setPassword(invalidPassword);
        ValidatableResponse loginResponse = userClient.login(Login.from(user), accessToken);
        loginResponse.assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and().statusCode(401);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}
