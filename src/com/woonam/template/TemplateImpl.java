package com.woonam.template;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.woonam.connect.AgentConnect;

public abstract class TemplateImpl {

	public abstract JsonObject run(JsonObject obj_data, String bgPath, String workPath);
}
