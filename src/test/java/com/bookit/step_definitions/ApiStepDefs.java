package com.bookit.step_definitions;

import com.bookit.pages.SelfPage;
import com.bookit.utilities.BookItApiUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DBUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiStepDefs {

    String token;
    Response response;
    String emailGlobal;
    @Given("I logged Bookit api using {string} and {string}")
    public void i_logged_Bookit_api_using_and(String email, String password) {

       token = BookItApiUtils.generateToken(email,password);
        emailGlobal = email;

    }

    @When("I get the current user information from api")
    public void i_get_the_current_user_information_from_api() {
        //send get request to retrieve current user information
        String url = ConfigurationReader.get("qa2api.uri")+"/api/users/me";

       response=     given().accept(ContentType.JSON)
                                     .and()
                                     .header("Authorization",token)
                               .when()
                                       .get(url);


    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {

        Assert.assertEquals(statusCode,response.statusCode());

    }

    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {
        //API -DB
        //get information from database
        String query = "select id,firstname,lastname,role\n" +
                "from users\n" +
                "where email ='"+emailGlobal+"';";

        Map<String, Object> rowMap = DBUtils.getRowMap(query);
        System.out.println("rowMap = " + rowMap);
        long expectedId = (long) rowMap.get("id");
        String expectedFirstName = (String) rowMap.get("firstname");
        String expectedLastName = (String) rowMap.get("lastname");
        String expectedRole = (String) rowMap.get("role");

        //get information from api
        JsonPath jsonPath = response.jsonPath();

        long actualId = jsonPath.getLong("id");
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //compare API - DB
        Assert.assertEquals(expectedId,actualId);
        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);




    }

    @Then("UI,API and Database user information must be match")
    public void ui_API_and_Database_user_information_must_be_match() {
        //API and DB
        //get information from database
        String query = "select id,firstname,lastname,role\n" +
                "from users\n" +
                "where email ='"+emailGlobal+"';";

        Map<String, Object> rowMap = DBUtils.getRowMap(query);
        System.out.println("rowMap = " + rowMap);
        long expectedId = (long) rowMap.get("id");
        String expectedFirstName = (String) rowMap.get("firstname");
        String expectedLastName = (String) rowMap.get("lastname");
        String expectedRole = (String) rowMap.get("role");

        //get information from api
        JsonPath jsonPath = response.jsonPath();

        long actualId = jsonPath.getLong("id");
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //compare API - DB
        Assert.assertEquals(expectedId,actualId);
        Assert.assertEquals(expectedFirstName,actualFirstName);
        Assert.assertEquals(expectedLastName,actualLastName);
        Assert.assertEquals(expectedRole,actualRole);

        //GET INFORMATION FROM UI
        SelfPage selfPage = new SelfPage();

        String actualUIFullName = selfPage.name.getText();
        String actualUIRole = selfPage.role.getText();

        //UI vs DB
        String expectedFullName = expectedFirstName+" "+expectedLastName;

        Assert.assertEquals(expectedFullName,actualUIFullName);
        Assert.assertEquals(expectedRole,actualUIRole);

        //UI vs API
        //Create a fullname for api
        String actualFullName = actualFirstName+" "+actualLastName;

        Assert.assertEquals(actualFullName,actualUIFullName);
        Assert.assertEquals(actualRole,actualUIRole);


    }


}
