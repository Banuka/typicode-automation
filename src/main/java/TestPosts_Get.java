import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestPosts_Get {

    @Test
    public void test_list_one_post() {
        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/posts/1");
        response.then().log().all();

        System.out.println("responseBody" + response);
        JsonPath json = new JsonPath(response.asString());
        String userId = json.getString("userId");
        String id = json.getString("id");
        String title = json.getString("title");
        String body = json.getString("body");

        Assert.assertTrue((userId != null), "The User ID Key is null.");
        Assert.assertTrue((id != null), "The id Key is null.");
        Assert.assertTrue((title != null), "The title Key is null.");
        Assert.assertTrue((body != null), "The body Key is null.");

    }

    @Test
    public void test_list_all_post() {
        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .get("https://jsonplaceholder.typicode.com/posts");
        response.then().log().all();

        System.out.println("responseBody" + response);
        JsonPath json = new JsonPath(response.asString());

        List<Map> posts = json.getJsonObject("");

        posts.stream().forEach((Map p) ->
                Assert.assertTrue((p.get("userId")!= null), "The User ID Key is null.")
        );
    }

    @Test
    public void test_create_post() {
        JSONObject body = new JSONObject();
        body.put("title","foo");
        body.put("body","bar");
        body.put("userId",1);
        Response response = given()
                .body(body.toString())
                .when()
                .header("Content-type", "application/json; charset=UTF-8")
                .post("https://jsonplaceholder.typicode.com/posts");
        response.then().log().all();
        response.then().log().all();
        System.out.println("responseBody" + response);
        JsonPath json = new JsonPath(response.asString());

        String userId = json.getString("userId");
        String id = json.getString("id");
        String title = json.getString("title");
        String kbody = json.getString("body");

        Assert.assertEquals(response.getStatusCode(),201, "Incorrect status code returned.");
        Assert.assertEquals(userId,1, "The User ID value is incorrect in response.");
        Assert.assertEquals(title,"bar", "The body value is incorrect in response.");
        Assert.assertEquals(kbody,"foo", "The title value is incorrect in response.");
    }
}
