import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserClient;
import model.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserCreateTest {
    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.random();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createNewUser() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        response.assertThat().log().all()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Попытка создания зарегистрированного пользователя")
    public void createExistUser() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        userClient.create(user);
        ValidatableResponse response2 = userClient.create(user);
        response2.assertThat().body("message", equalTo("User already exists"))
                .and().statusCode(403);
    }

    @Test
    @DisplayName("Попытка создания пользователя без пароля")
    public void createUserWithoutPassword() {
        user.setPassword("");
        ValidatableResponse response = userClient.create(user);
        if (accessToken != null) {
            accessToken = response.extract().path("accessToken").toString();
        }
        response.assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}
