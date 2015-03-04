package br.uern.ges.med.sim2ped.utils;

import com.google.gson.JsonObject;

public class JSONMessageObject {
	public String type;
	public JsonObject object;
	
	public JsonObject getObject() {
		return object;
	}
	
	public String getType() {
		return type;
	}
	
	public void setObject(JsonObject object) {
		this.object = object;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
