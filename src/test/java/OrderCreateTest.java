import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private  OrderClient orderClient;
    private String accessToken;
    private String email;
    private String[] ingredients;
    private User user;
    private UserClient userClient;

    public OrderCreateTest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Before
    public void setting() {
        orderClient = new OrderClient();
        user = UserGenerator.random();
        userClient = new UserClient();

    }

    @Parameterized.Parameters
    public static Object[] valueIngredients() {
        return new Object[][]{
                {new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa70"}}
        };
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void orderCreateTestWithoutAuthorized() {
        Order order = new Order(ingredients);
        accessToken = "";
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        responseOrder.assertThat().log().all()
                .body( "order.number", is(notNullValue()))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без ингредиента без авторизации")
    public void orderCreateTestWithoutAuthorizedNull() {
        Order order = new Order(null);
        accessToken = "";
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        responseOrder.assertThat().log().all()
                .body( "message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с невалидным ингредиентом без авторизации")
    public void orderCreateTestWithoutAuthorizedInvalid() {
        Order order = new Order();
        order.setIngredients(new String[]{"qwerty123"});
        accessToken = "";
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        responseOrder.assertThat().log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа c авторизацией")
    public void orderCreateTestWithAuthorized() {
        Order order = new Order(ingredients);
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        email = response.extract().path("user.email").toString();
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        responseOrder.assertThat().log().all()
                .body( "order.owner.email", equalTo(email))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без ингредиента c авторизацией")
    public void orderCreateTestWithAuthorizedNull() {
        Order order = new Order(null);
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        email = response.extract().path("user.email").toString();
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        responseOrder.assertThat().log().all()
                .body( "message", equalTo("Ingredient ids must be provided"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с невалидным ингредиентом c авторизацией")
    public void orderCreateTestWithAuthorizedInvalid() {
        Order order = new Order(ingredients);
        order.setIngredients(new String[]{"qwerty123"});
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        email = response.extract().path("user.email").toString();
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        responseOrder.assertThat().log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("Получение заказа конкретного авторизованного пользователя")
    public void orderGetTestWithAuthorized() {
        Order order = new Order(ingredients);
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        ValidatableResponse responseGet = orderClient.getOrders(accessToken);
        responseGet.assertThat().log().all()
                .body("success", is(true))
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Получение заказа конкретного неавторизованного пользователя")
    public void orderGetTestWithoutAuthorized() {
        Order order = new Order(ingredients);
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken").toString();
        ValidatableResponse responseOrder = orderClient.create(order, accessToken);
        accessToken = "";
        ValidatableResponse responseGet = orderClient.getOrders(accessToken);
        responseGet.assertThat().log().all()
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

