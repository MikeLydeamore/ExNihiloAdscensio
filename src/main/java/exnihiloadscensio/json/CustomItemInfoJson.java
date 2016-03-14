package exnihiloadscensio.json;

import java.lang.reflect.Type;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import exnihiloadscensio.util.ItemInfo;

public class CustomItemInfoJson implements JsonDeserializer<ItemInfo>, JsonSerializer<ItemInfo> {

	@Override
	public JsonElement serialize(ItemInfo src, Type typeOfSrc,
			JsonSerializationContext context) {
		
		JsonObject obj = new JsonObject();
		obj.addProperty("name", GameData.getItemRegistry().getNameForObject(src.getItem()).toString());
		obj.addProperty("meta", src.getMeta());
		return obj;
	}

	@Override
	public ItemInfo deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonHelper helper = new JsonHelper(json);

        String name = helper.getString("name");
        int meta = helper.getNullableInteger("meta", 0);

        return new ItemInfo(GameData.getItemRegistry().containsKey(new ResourceLocation(name)) ? GameData.getItemRegistry().getObject(new ResourceLocation(name)) : null, meta);
	}

}
