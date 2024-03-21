

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Reusable;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class Dynamic_Json {
	@Test(dataProvider="BooksData")
	public void AddBook(String isbn, String aisle )
	{
		RestAssured.baseURI= "http://216.10.245.166";
		String response = given().log().all().header("Content-Type","application/json")							
		        .body(payload.AddBook(isbn,aisle))
		        .when().post("/Library/Addbook.php")
		        .then().log().all().statusCode(200)
                .extract().response().asString();
		
		JsonPath js = Reusable.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
		
		// Delete Book
		given().log().all().header("Content-Type","application/json")
		.body("{\r\n"
				+ " \r\n"
				+ "\"ID\" : \""+id+"\"\r\n"
				+ " \r\n"
				+ "} ")
		.when().post("/Library/DeleteBook.php")
		.then().assertThat().log().all().statusCode(200)
		.body("msg", equalTo("book is successfully deleted"));
	}
		
	@DataProvider(name="BooksData")
	public Object[][] getData()
	{
		//array = collection of elements
		//multidimensional array = collection of arrays
		return new Object[][] {{"afgsh","7267"},{"ghasf","615"},{"gdjsa","836"}};		
	}

	
}
