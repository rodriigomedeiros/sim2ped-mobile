package br.uern.ges.med.sim2ped.utils;

import com.google.gson.JsonObject;

public class JSONMessageObjectGCM {
	public String type;
	public JsonObject object;
    public String action;
	
	public JsonObject getObject() {
		return object;
	}
	
	public String getType() {
		return type;
	}

    public String getAction(){
        return action;
    }

}
