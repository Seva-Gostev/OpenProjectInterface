package me.gostev.opms.opapi.test;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

import me.gostev.opms.opapi.APIException;
import me.gostev.opms.opapi.OpenProjectRestAPI;
import me.gostev.opms.opapi.OpenProjectUIAPI;

public class APITest {

	private OpenProjectUIAPI uiapi;
	private OpenProjectRestAPI restapi;

	@Before
	public void createAPI() throws MalformedURLException {
		uiapi = new OpenProjectUIAPI("http://localhost");
		restapi = new OpenProjectRestAPI("http://localhost",
				"2f4b94420bd8ce95000ce821b7d00f5bb0989f314ec708471f56fa0b880e7a18");
	}

	// @Test
	// public void loginTest() throws APIException {
	//
	// uiapi.login("admin", "housemartin");
	// uiapi.createProject("Java auto project", "joutproj");
	// }

	// @Test
	// public void createUserTest() throws APIException {
	//
	// NewUserData data = new
	// NewUserData.Builder("seva").setFirstName("Vsevolod").setLastName("Gostev")
	// .setEmail("vmgostev@gmail.com").setPassword("monster000").build();
	//
	// restapi.createUser(data);
	//
	// }

	@Test
	public void createUserTest() throws APIException {

		uiapi.login("admin", "housemartin");
		// uiapi.createProject("Add Member Test Project",
		// "add-member-test-project");
		//
		// NewUserData data = new
		// NewUserData.Builder("irina").setFirstName("Irina").setLastName("Gosteva")
		// .setEmail("igosteva@gmail.com").setPassword("monster000").build();
		//
		// long newUserID = restapi.createUser(data);

		uiapi.addMemberToProject("add-member-test-project", 5, 4);

	}
}
