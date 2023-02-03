import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserСhangeTest {
    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.random();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Изменение name авторизованного пользовалеля")
    public void changeNameAuthorizedUserTest() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        user.setName("New_NAME");
        ValidatableResponse changeResponse = userClient.change(UserChange.from(user), accessToken);
        changeResponse.assertThat()
                .body("user.name", equalTo("New_NAME"))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Изменение email авторизованного пользовалеля")
    public void changeLoginAuthorizedUserTest() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        user.setEmail("new_email@yandex.ru");
        ValidatableResponse changeResponse = userClient.change(UserChange.from(user), accessToken);
        changeResponse.assertThat()
                .body("user.email",equalTo("new_email@yandex.ru"))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Изменение name неавторизованного пользовалеля")
    public void changeNameUnauthorizedUserTest() {
        ValidatableResponse response = userClient.create(user);
        user.setName("New_NAME");
        accessToken = "";
        ValidatableResponse changeResponse = userClient.change(UserChange.from(user), accessToken);
        changeResponse.assertThat()
                .body("message", equalTo("You should be authorised"))
                .and().statusCode(401);
    }

    @Test
    @DisplayName("Изменение email неавторизованного пользовалеля")
    public void changeEmailUnauthorizedUserTest() {
        ValidatableResponse response = userClient.create(user);
        user.setName("new_email@yandex.ru");
        accessToken = "";
        ValidatableResponse changeResponse = userClient.change(UserChange.from(user), accessToken);
        changeResponse.assertThat()
                .body("message", equalTo("You should be authorised"))
                .and().statusCode(401);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}