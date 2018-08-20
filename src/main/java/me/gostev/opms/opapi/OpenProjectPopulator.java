package me.gostev.opms.opapi;

import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

public class OpenProjectPopulator {

	public static void populate(String url, String username, String password, String apikey,
			Map<String, Student> students, Map<String, Project> projects, boolean inviting)
			throws MalformedURLException, APIException {

		OpenProjectUIAPI uiapi = new OpenProjectUIAPI(url);
		OpenProjectRestAPI restapi = new OpenProjectRestAPI(url, apikey);

		uiapi.login(username, password);

		if (inviting) {
			for (Student s : students.values()) {

				NewUserData data = new NewUserData.Builder(s.getUsername()).setFirstName(s.getUsername())
						.setEmail(s.getEmail()).build();

				try {
					s.setOPID(restapi.inviteUser(data));
					s.setAdded(true);
				} catch (APIException ex) {
					continue;
				}
			}
		} else {
			for (Student s : students.values()) {

				NewUserData data = new NewUserData.Builder(s.getUsername()).setFirstName(s.getUsername())
						.setLastName("Please Change").setEmail(s.getEmail())
						.setPassword(RandomStringUtils.random(10, true, true)).build();

				try {
					s.setOPID(restapi.inviteUser(data));
					s.setAdded(true);
				} catch (APIException ex) {
					continue;
				}
			}
		}

		for (Project p : projects.values()) {

			try {
				uiapi.createProject(p.getName(), p.getIdentifier());

				for (Student m : p.getAssignees()) {

					if (m.getAdded())
						uiapi.addMemberToProject(p.getIdentifier(), m.getOPID(), 4);
				}
			} catch (APIException ex) {
				continue;
			}
		}

	}
}
