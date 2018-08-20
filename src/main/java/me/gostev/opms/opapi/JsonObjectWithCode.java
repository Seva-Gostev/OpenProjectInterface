package me.gostev.opms.opapi;

import com.google.gson.JsonObject;

public class JsonObjectWithCode {

	private final JsonObject object;
	private final int code;

	public JsonObjectWithCode(JsonObject object, int code) {

		this.object = object;
		this.code = code;
	}

	public JsonObject getObject() {
		return object;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Code: " + code + "\nObject:\n" + object.toString();
	}

}
