package exnihiloadscensio.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import exnihiloadscensio.util.BlockInfo;
import net.minecraft.block.Block;

public class CustomBlockInfoJson implements JsonDeserializer<BlockInfo>, JsonSerializer<BlockInfo>
{
    @Override
    public JsonElement serialize(BlockInfo src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject obj = new JsonObject();
        
        obj.addProperty("name", src.getBlock().getRegistryName().toString());
        obj.addProperty("meta", src.getMeta());
        
        return obj;
    }
    
    @Override
    public BlockInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        
        JsonHelper helper = new JsonHelper(json);
        
        String name = helper.getString("name");
        int meta = helper.getNullableInteger("meta", 0);
        
        return new BlockInfo(Block.getBlockFromName(name), meta);
    }
}
