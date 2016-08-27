package exnihiloadscensio.json;

import java.lang.reflect.Type;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import exnihiloadscensio.util.BlockInfo;

public class CustomBlockInfoJson implements JsonDeserializer<BlockInfo>, JsonSerializer<BlockInfo> {
	
	@SuppressWarnings("deprecation")
	@Override
	public JsonElement serialize(BlockInfo src, Type typeOfSrc,
			JsonSerializationContext context) {
		
		JsonObject obj = new JsonObject();
		obj.addProperty("name", GameData.getBlockRegistry().getNameForObject(src.getBlock()).toString());
		obj.addProperty("meta", src.getMeta());
		return obj;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockInfo deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonHelper helper = new JsonHelper(json);

        String name = helper.getString("name");
        int meta = helper.getNullableInteger("meta", 0);

        return new BlockInfo(GameData.getBlockRegistry().containsKey(new ResourceLocation(name)) ? GameData.getBlockRegistry().getObject(new ResourceLocation(name)) : null, meta);
	}

}