package me.gostev.opms.opapi;

import org.apache.http.impl.client.BasicCookieStore;

public class AuthenticationInfo {

	private BasicCookieStore cookieStore = new BasicCookieStore();

	BasicCookieStore getCookieStore() {
		return cookieStore;
	}
}
