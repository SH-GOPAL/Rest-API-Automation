import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import files.Reusable;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Base1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response = given().log().all()
				.queryParams("key","qaclick123")
				.header("Content-Type","application/json")
		        .body(payload.AddPlace())
		        .when().post("maps/api/place/add/json")
		        .then().assertThat().statusCode(200)
		        .body("scope", equalTo("APP"))
		        .header("server","Apache/2.4.52 (Ubuntu)")
		        .extract().response().asString();
		
		System.out.println(response);
		JsonPath js = Reusable.rawToJson(response); // for parsing Json
		String placeid = js.getString("place_id");
		
		// Update Place
		String newAddress = "Summer walk, USA";				
		given().log().all()
		.queryParam("key","qaclick123")
		.header("Content-Type","application/json")
		.body("{\r\n" + 
						"\"place_id\":\""+placeid+"\",\r\n" + 
						"\"address\":\""+newAddress+"\",\r\n" + 
						"\"key\":\"qaclick123\"\r\n" + 
						"}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all()
		.statusCode(200)
		.body("msg", equalTo("Address successfully updated"));
						
	    //Get Place
		String resp =given().log().all()
				.queryParam("key","qaclick123")
				.queryParam("place_id", placeid)
				.when().get("maps/api/place/get/json")
				.then().assertThat().log().all().statusCode(200)
				.extract().response().asString();
		
		JsonPath js1 = Reusable.rawToJson(resp); // for parsing Json
		String actualaddress = js1.getString("address");
		Assert.assertEquals(newAddress, actualaddress);			
	}

}
