package model;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class UserClient extends Client {

    private static final String CREATE = "api/auth/register";
    private static final String LOGIN = "api/auth/login";
    private static final String DELETE = "api/auth/user";
    private static final String CHANGE = "api/auth/user";

    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec().log().all())
                .when()
                .body(user)
                .post(CREATE)
                .then().log().all();
    }
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpec().log().all())
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE)
                .then().log().all();
    }

    public ValidatableResponse login(Login login, String accessToken) {
        return given()
                .spec(getSpec().log().all())
                .header("Authorization", accessToken)
                .when()
                .body(login)
                .post(LOGIN)
                .then().log().all();
    }
    public ValidatableResponse change(UserChange userChange, String accessToken) {
        return given()
                .spec(getSpec().log().all())
                .header("Authorization", accessToken)
                .when()
                .body(userChange)
                .patch(CHANGE)
                .then().log().all();
    }
}
