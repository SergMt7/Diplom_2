package model;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class OrderClient extends Client {
    private static final String CREATE = "api/orders";
    private static final String GET_ORDERS = "api/orders";
    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .spec(getSpec().log().all())
                .header("Authorization", accessToken)
                .when()
                .body(order)
                .post(CREATE)
                .then().log().all();
    }
    public ValidatableResponse getOrders(String accessToken) {
        return given()
                .spec(getSpec().log().all())
                .header("Authorization", accessToken)
                .when()
                .get(GET_ORDERS)
                .then().log().all();
    }
}
