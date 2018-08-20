package me.gostev.opms.opapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OpenProjectUIAPI {

	private URL baseURL;
	CloseableHttpClient httpClient;
	URL loginURL;
	URL newProjectFormURL;
	URL newProjectCreateURL;

	public OpenProjectUIAPI(String url) throws MalformedURLException {

		baseURL = new URL(url);

		httpClient = HttpClients.custom().setDefaultCookieStore(new BasicCookieStore()).build();

		loginURL = new URL(baseURL, "login");
		newProjectFormURL = new URL(baseURL, "projects/new");
		newProjectCreateURL = new URL(baseURL, "projects");

	}

	public void login(String username, String password) throws APIException {

		try {

			CloseableHttpResponse response1 = null;
			HttpGet httpget = new HttpGet(loginURL.toString());
			response1 = httpClient.execute(httpget);

			String responseString = IOUtils.toString(response1.getEntity().getContent(), StandardCharsets.UTF_8);

			Document doc = Jsoup.parse(responseString);
			Elements forms = doc.select("form");

			List<Element> loginForms = forms.stream().filter(f -> "/login".equals(f.attr("action")))
					.collect(Collectors.toList());

			Elements inputs = loginForms.get(0).select("input");

			List<BasicNameValuePair> details = new ArrayList<>();

			for (Element el : inputs) {
				String name = el.attr("name");
				String val = el.attr("value");

				if ("username".equals(name))
					val = username;
				else if ("password".equals(name))
					val = password;

				details.add(new BasicNameValuePair(name, val));
			}

			response1.close();

			HttpPost httpPost = new HttpPost(loginURL.toString());

			httpPost.setEntity(new UrlEncodedFormEntity(details, "UTF-8"));

			CloseableHttpResponse response = httpClient.execute(httpPost);

			System.out.println(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));
			System.out.println("Resp code: " + response.getStatusLine().getStatusCode());

			response.close();

		} catch (IOException e) {
			throw new APIException(e.getMessage(), e);
		}
	}

	public void createProject(String projectName, String projectID) throws APIException {

		try {

			CloseableHttpResponse response1 = null;
			HttpGet httpget = new HttpGet(newProjectFormURL.toString());
			response1 = httpClient.execute(httpget);

			String responseString = IOUtils.toString(response1.getEntity().getContent(), StandardCharsets.UTF_8);

			Document doc = Jsoup.parse(responseString);
			Elements forms = doc.select("form");

			List<Element> loginForms = forms.stream().filter(f -> "/projects".equals(f.attr("action")))
					.collect(Collectors.toList());

			Elements inputs = loginForms.get(0).select("input");

			List<BasicNameValuePair> details = new ArrayList<>();

			for (Element el : inputs) {
				String name = el.attr("name");
				String val = el.attr("value");

				if ("project[name]".equals(name))
					val = projectName;

				details.add(new BasicNameValuePair(name, val));
			}

			details.add(new BasicNameValuePair("project[identifier]", projectID));

			response1.close();

			HttpPost httpPost = new HttpPost(newProjectCreateURL.toString());

			httpPost.setEntity(new UrlEncodedFormEntity(details, "UTF-8"));

			CloseableHttpResponse response = httpClient.execute(httpPost);

			System.out.println(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));
			System.out.println("Resp code: " + response.getStatusLine().getStatusCode());

			response.close();

		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}
	}

	public void addMemberToProject(String projectID, long userID, long roleID) throws APIException {

		URL addMemberURL = null;

		try {
			addMemberURL = new URL(baseURL, "projects/" + projectID + "/members");
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}

		try {

			CloseableHttpResponse response1 = null;
			HttpGet httpget = new HttpGet(addMemberURL.toString());
			response1 = httpClient.execute(httpget);

			String responseString = IOUtils.toString(response1.getEntity().getContent(), StandardCharsets.UTF_8);

			Document doc = Jsoup.parse(responseString);
			Elements forms = doc.select("form");

			final URL finalAddMemberURL = addMemberURL;
			List<Element> loginForms = forms.stream().filter(f -> finalAddMemberURL.getPath().equals(f.attr("action")))
					.collect(Collectors.toList());

			Elements inputs = loginForms.get(0).select("input");

			List<BasicNameValuePair> details = new ArrayList<>();

			String authToken = null;

			for (Element el : inputs) {
				String name = el.attr("name");
				String val = el.attr("value");

				if ("authenticity_token".equals(name)) {
					authToken = val;
				}
			}

			if (authToken == null)
				throw new APIException("Couldn't find authenticity token");

			details.add(new BasicNameValuePair("utf8", "\u2713"));
			details.add(new BasicNameValuePair("authenticity_token", authToken));
			details.add(new BasicNameValuePair("member[user_ids][]", String.valueOf(userID)));
			details.add(new BasicNameValuePair("member[role_ids][]", String.valueOf(roleID)));
			details.add(new BasicNameValuePair("button", ""));

			response1.close();

			HttpPost httpPost = new HttpPost(addMemberURL.toString());

			httpPost.setEntity(new UrlEncodedFormEntity(details, "UTF-8"));

			CloseableHttpResponse response = httpClient.execute(httpPost);

			System.out.println(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));
			System.out.println("Resp code: " + response.getStatusLine().getStatusCode());

			response.close();

		} catch (APIException e) {
			throw e;
		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}
	}

	public void close() {

		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
