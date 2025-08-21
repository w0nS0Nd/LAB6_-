import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiLoginTest {

    @Test
    public void testLoginSuccess() {
        // тіло запиту
        String requestBody = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";

        // надсилаємо POST-запит
        Response response = RestAssured.given()
                .baseUri("https://reqres.in")
                .basePath("/api/login")
                .header("x-api-key", "reqres-free-v1") // якщо треба ключ
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post();

        // вивід для зручності
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());

        // перевіряємо, що API повертає 200
        Assert.assertEquals(response.getStatusCode(), 200, "Login request failed!");

        // перевіряємо, що у відповіді є токен
        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Token is missing in the response!");
    }
}
