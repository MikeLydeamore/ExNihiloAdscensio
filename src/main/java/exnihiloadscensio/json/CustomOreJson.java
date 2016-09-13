package exnihiloadscensio.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.items.ore.Ore;
import exnihiloadscensio.items.ore.OreSiftable;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;

public class CustomOreJson implements JsonDeserializer<Ore>, JsonSerializer<Ore> {

	@Override
	public JsonElement serialize(Ore src, Type typeOfSrc, JsonSerializationContext context) {
		
		JsonObject obj = new JsonObject();
		obj.addProperty("name", src.getName());
		obj.add("color", context.serialize(src.getColor(), Color.class));
		obj.add("result", context.serialize(src.getResult(), ItemInfo.class));
		obj.add("drop", context.serialize(src.getDrop(), new HashMap<BlockInfo, ArrayList<OreSiftable>>().getClass()));
		
		return obj;
	}

	@Override
	public Ore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		JsonHelper helper = new JsonHelper(json);
		String name = helper.getString("name");
		Color color = context.deserialize(json.getAsJsonObject().get("color"), Color.class);
		ItemInfo result = context.deserialize(json.getAsJsonObject().get("result"), ItemInfo.class);
		
		Type type = new TypeToken<HashMap<String, ArrayList<OreSiftable>>>(){}.getType();
		HashMap<String, ArrayList<OreSiftable>> dropString = context.deserialize(json.getAsJsonObject().get("drop"), type);
		HashMap<BlockInfo, ArrayList<OreSiftable>> drop = new HashMap<BlockInfo, ArrayList<OreSiftable>>();
		for (String s : dropString.keySet()) {
			drop.put(new BlockInfo(s), dropString.get(s));
		}
		return new Ore(name, color, result, drop);
	}

}
