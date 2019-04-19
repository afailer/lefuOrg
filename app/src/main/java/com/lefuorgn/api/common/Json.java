package com.lefuorgn.api.common;

import com.google.gson.Gson;

/**
 * GSON单例对象
 */
public final class Json { 
	private static Json json;
	private Gson gson;
	
	private Json() {
		gson = new Gson();
	}
	
	public static Gson getGson() {
		if(json == null) {
			synchronized (Json.class) {
				if(json == null) {
					json = new Json();
				}
			}
		}
		return json.gson;
	}
}
