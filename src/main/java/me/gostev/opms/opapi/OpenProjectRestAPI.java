package me.gostev.opms.opapi;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OpenProjectRestAPI {

	private CloseableHttpClient client;
	private HttpClientContext context;

	private URL baseURL;

	public OpenProjectRestAPI(String domainURL, String apikey) throws MalformedURLException {

		this.baseURL = new URL(domainURL + "/api/v3/");

		int port = baseURL.getPort() == -1 ? ("https".equals(baseURL.getProtocol()) ? 443 : 80) : baseURL.getPort();

		HttpHost targetHost = new HttpHost(baseURL.getHost(), port, baseURL.getProtocol());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("apikey", apikey));

		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());

		// Add AuthCache to the execution context
		context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);

		client = HttpClientBuilder.create().build();

	}

	public long inviteUser(NewUserData data) throws APIException {

		JsonObject obj = new JsonObject();
		obj.addProperty("email", data.getEmail());
		obj.addProperty("firstName", data.getFirstName());
		obj.addProperty("status", "invited");

		JsonObjectWithCode response = sendJson(obj, "users");
		System.out.println(response);

		if (response.getCode() != 201) {
			JsonElement elem = response.getObject().get("message");

			throw new APIException("Server returned error code " + response.getCode()
					+ (elem != null ? ("\nMessage: " + response.getObject().get("message").getAsString()) : ""));
		}
		return response.getObject().get("id").getAsLong();
	}

	public long createUser(NewUserData data) throws APIException {

		JsonObject obj = new JsonObject();
		obj.addProperty("login", data.getLogin());
		obj.addProperty("email", data.getEmail());
		obj.addProperty("firstName", data.getFirstName());
		obj.addProperty("lastName", data.getLastName());
		obj.addProperty("admin", data.getAdmin());
		obj.addProperty("language", data.getLanguage());
		obj.addProperty("status", "active");
		obj.addProperty("password", data.getPassword());

		JsonObjectWithCode response = sendJson(obj, "users");
		System.out.println(response);

		if (response.getCode() != 201) {
			JsonElement elem = response.getObject().get("message");

			throw new APIException("Server returned error code " + response.getCode()
					+ (elem != null ? ("\nMessage: " + response.getObject().get("message").getAsString()) : ""));
		}

		return response.getObject().get("id").getAsLong();
	}

	private JsonObjectWithCode sendJson(JsonObject obj, String URLSuffix) throws APIException {

		HttpEntity entity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

		try {
			URL inviteUserURL = null;
			inviteUserURL = new URL(baseURL, URLSuffix);

			System.out.println(inviteUserURL);

			HttpPost post = new HttpPost(inviteUserURL.toString());
			post.setEntity(entity);

			CloseableHttpResponse response = client.execute(post, context);

			String body = IOUtils
					.toString(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));

			JsonObject returnObj = new JsonParser().parse(body).getAsJsonObject();

			int respCode = response.getStatusLine().getStatusCode();
			response.close();

			return new JsonObjectWithCode(returnObj, respCode);

		} catch (Exception ex) {
			throw new APIException("sendJson failed: " + ex.getMessage(), ex);
		}

	}
}
