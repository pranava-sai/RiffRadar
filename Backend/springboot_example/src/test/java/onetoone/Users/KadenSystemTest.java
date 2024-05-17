package onetoone.Users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import onetoone.Users.AttendeeController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort;	// SBv3

import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class KadenSystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void createLoginAndDeleteUser() {
        // Send request and receive response
        Response postResponse = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("{\n" +
                        "    \"name\": \"Testingtestaccount\",\n" +
                        "    \"loginInfo\": {\n" +
                        "        \"emailId\": \"kadenb4u@gmail.com\",\n" +
                        "        \"password\": \"Test\",\n" +
                        "        \"userType\": \"attendee\"\n" +
                        "    }\n" +
                        "}").
                when().
                post("/attendees");


        // Check status code
        int statusCode = postResponse.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = postResponse.getBody().asString();
        try {
            //JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("{\"message\":\"success\"}", returnString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //New user was created, test the login feature
        Response createLogin = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("{\n" +
                        "\"emailId\":\"testing@gmail.com\",\n" +
                        "\"password\":\"123456789!\"\n" +
                        "}").
                when().
                post("/login");


        // Check status code
        statusCode = createLogin.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String loginString = createLogin.getBody().asString();
            assertEquals("{\"id\":12,\"emailId\":\"testing@gmail.com\",\"password\":\"123456789!\",\"userType\":\"venue\"}", loginString);

        //Get attendee id with name
        Response getAttendeeId = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").body("").
                get("/attendeesbyname/Testingtestaccount");


        // Check status code
        statusCode = getAttendeeId.getStatusCode();
        assertEquals(200, statusCode);
        String getAttendeeIdString = getAttendeeId.getBody().asString();
        int id = 0;
        try {
            JSONObject jsonObject = new JSONObject(getAttendeeIdString);
            id = jsonObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //----------------------------------------
        //Delete the created attendee
        Response deleteAttendee = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                when().
                delete("/attendees/" + id);


        // Check status code
        statusCode = createLogin.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String deleteString = deleteAttendee.getBody().asString();
        assertEquals("{\"message\":\"success\"}", deleteString);



    }
    @Test
    public void createUserGroupAndAddUsersThenDelete() {
        // Create a new userGroup
        Response newGroupResponse = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("{\n" +
                        "\"name\" : \"TestGroup\",\n" +
                        "\"groupBio\": \"Group\",\n" +
                        "\"groupPassword\":\"1234\"\n" +
                        "}").
                when().
                post("/usergroups");


        // Check status code
        int statusCode = newGroupResponse.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = newGroupResponse.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);


        //---------------------------------------------------------------------------
        //Add a user to the userGroup
        Response addUserToGroup = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("").
                when().
                put("/usergroups/TestGroup/attendees/3/password/1234");


        // Check status code
        statusCode = addUserToGroup.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        returnString = addUserToGroup.getBody().asString();

            assertEquals("{\"message\":\"success\"}", returnString);

        //---------------------------------------------------------------------------
        //Delete the usergroup

        // Create a new userGroup
        Response delteGroupResponse = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("").
                when().
                delete("/usergroups/TestGroup");


        // Check status code
        statusCode = delteGroupResponse.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        returnString = delteGroupResponse.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);


    }

    @Test
    public void modifyUsersCart() {
    // Create a new userGroup
        Random rand = new Random();
        int numberOfTickets = rand.nextInt((20 - 1) + 1) + 1;

        Response editUserCart = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("{\n" +
                        "\"concertName\":\"coachella\",\n" +
                        "\"numberOfTickets\":\"" + numberOfTickets + "\",\n" +
                        "\"pricePerTicket\":\"50\"\n" +
                        "}").
                when().put("/carts/3");


        // Check status code
        int statusCode = editUserCart.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = editUserCart.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString);

        //-------------------------------------------------------------------------------

        // Make sure the cart got updated and has the random ticket value
        Response getUserCart = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("").
                when().
                get("/carts/3");


        // Check status code
        statusCode = getUserCart.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        returnString = getUserCart.getBody().asString();
        assertEquals("{\"id\":123,\"concertName\":\"coachella\",\"numberOfTickets\":\"" + numberOfTickets + "\",\"pricePerTicket\":\"50\"}", returnString);




    }

    @Test
    public void AddMultipleUsersToUserGroup() {
        // Create a new order
        // Create a new userGroup
        Response newGroupResponse = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("{\n" +
                        "\"name\" : \"TestGroup\",\n" +
                        "\"groupBio\": \"Group\",\n" +
                        "\"groupPassword\":\"1234\"\n" +
                        "}").
                when().
                post("/usergroups");


        // Check status code
        int statusCode = newGroupResponse.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = newGroupResponse.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString);


        //---------------------------------------------------------------------------
        //Add a user to the userGroup
        Response addUserToGroup = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("").
                when().
                put("/usergroups/TestGroup/attendees/3/password/1234");


        // Check status code
        statusCode = addUserToGroup.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        returnString = addUserToGroup.getBody().asString();

        assertEquals("{\"message\":\"success\"}", returnString);

        //------------------------------------------------------------------------------
        //Check that the user is in the usergroup
        Response check = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("").
                when().
                put("/attendees/3/usergroups");


        // Check status code
        statusCode = addUserToGroup.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        returnString = addUserToGroup.getBody().asString();

        assertNotEquals("", returnString);


        //---------------------------------------------------------------------------
        //Delete the usergroup

        // Create a new userGroup
        Response delteGroupResponse = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset", "utf-8").
                body("").
                when().
                delete("/usergroups/TestGroup");


        // Check status code
        statusCode = delteGroupResponse.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        returnString = delteGroupResponse.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString);



    }


}

