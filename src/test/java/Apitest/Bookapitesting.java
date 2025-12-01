package Apitest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Bookapitesting {

    @BeforeClass   // ✅ ეს აკლდა!
    public void setup() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void getBooksTest() {

        Response response = RestAssured

                        .given()
                        .header("accept", "application/json")
                        .when()
                        .get("/BookStore/v1/Books")
                        .then()
                        //.statusCode(200)
                        .extract().response();

        String author = response.jsonPath().getString("books[0].author");
        String publisher = response.jsonPath().getString("books[0].publisher");

        //System.out.println("FULL RESPONSE BODY:" + response.getBody().asPrettyString());

        Assert.assertEquals(response.statusCode(), 200, "satus kode is 200");
        Assert.assertEquals(author, "Richard E. Silverman");
        Assert.assertEquals(publisher, "O'Reilly Media");

        System.out.println("Author: " + author);
        System.out.println("Publisher: " + publisher);
        System.out.println("Status code: " + response.statusCode());
    }


}
