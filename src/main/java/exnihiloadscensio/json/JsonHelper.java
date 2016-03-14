package exnihiloadscensio.json;

import com.google.gson.JsonElement;


public class JsonHelper {

	JsonElement json;

	public JsonHelper(JsonElement json) {
		this.json = json;
	}

	public boolean getBoolean(String object) {
		return json.getAsJsonObject().get(object).getAsBoolean();
	}

	public boolean getNullableBoolean(String object, boolean def) {
		boolean ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsBoolean();

		return ret;
	}

	public boolean getNullableBoolean(String object) {
		return getNullableBoolean(object, false);
	}

	public int getInteger(String object) {
		return json.getAsJsonObject().get(object).getAsInt();
	}

	public int getNullableInteger(String object, int def) {
		int ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsInt();

		return ret;
	}

	public double getDouble(String object) {
		return json.getAsJsonObject().get(object).getAsDouble();
	}

	public double getNullableDouble(String object, double def) {
		double ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsDouble();

		return ret;
	}

	public String getString(String object) {
		return json.getAsJsonObject().get(object).getAsString();
	}

	public String getNullableString(String object, String def) {
		String ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsString();

		return ret;
	}

}
