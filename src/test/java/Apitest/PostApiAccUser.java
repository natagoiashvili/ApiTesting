package Apitest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
public class PostApiAccUser {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void createUserTest() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", "TestUser_" + System.currentTimeMillis()); //ყოველჯერზე გადაეცემა უნიკალური იუზერი
        requestBody.put("password", "Abc12345!");

        Response response = RestAssured
                 .given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/Account/v1/User")
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201");

        String userId = response.jsonPath().getString("userID");
        Assert.assertNotNull(userId, "Response contain userID");

        System.out.println(userId);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void InvalidPasswordTest() {

        // JSON body დაუშვებელი პაროლით
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", "InvalidUser_" + System.currentTimeMillis());
        requestBody.put("password", "abc");

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/Account/v1/User")
                .then()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 400, "Status code should be 400 invalid password");

        String expectedMessage = "Passwords must have at least one non alphanumeric character, one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z'), one special character and Password must be eight characters or longer.";
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals(actualMessage, expectedMessage, "Error message");

        System.out.println("Returned error message: " + actualMessage);
    }
}
