package stepDefinitions;
import static io.restassured.RestAssured.given;

import org.junit.Assert;

import com.jayway.jsonpath.JsonPath;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import utils.FunctionLibrary;

public class CountriesAPISteps extends FunctionLibrary {
	Response response;
	Scenario scenario;
		
		@Before
		public void setUp(Scenario scenario){
			 this.scenario=scenario;
		}
	
	@Given("User submits GET request to {string} for {string}")
	public void user_submits_GET_request_to_for(String url, String countryName) {
		// this step submit GET request for provided country name
	  response=given().when().get(property.getProperty(url)+countryName);
	    // this step attach response data to cucumber report
	  scenario.embed(response.asString().getBytes(), "application/json");
		
	   
	}

	@Given("User validates response status code is {int}")
	public void user_validates_response_status_code_is(int expectedStatusCode) {
	    // this step validate response status code
		Assert.assertEquals(expectedStatusCode, response.getStatusCode());
		// this step write response data to cucumber report
		scenario.write("Status Code: "+response.getStatusCode());
	}

	
	@Then("User validates capital city is {string}")
	public void user_validates_capital_city_is(String capitalCity) {
		// this step retrieves capital city name from response
		String actualCapital= JsonPath.read(response.asString(), "$..capital[*]").toString();
		// this step writes capital city name to cucumber report
		scenario.write("Actual Capital City: "+actualCapital);
		scenario.write("Expected Capital City: "+capitalCity);
		// Validates if actual capital city name matches with expected name
		Assert.assertTrue( actualCapital.contains(capitalCity));

	}
	
	
	@Then("User validates error message is {string}")
	public void user_validates_error_message_is(String expectedErrorMessage) {
		// this step retrieves error message from response
		String actualErrorMessage= JsonPath.read(response.asString(), "$.message").toString();
		// this step writes error message to cucumber report
		scenario.write("Actual Error Message: "+actualErrorMessage);
		scenario.write("Expected Error Message: "+expectedErrorMessage);
		// Validates if actual error message matches with expected message
		Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
	}

}
