package exnihiloadscensio.json;

import com.google.gson.*;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.LogUtil;
import net.minecraft.block.Block;

import java.lang.reflect.Type;

public class CustomBlockInfoJson implements JsonDeserializer<BlockInfo>, JsonSerializer<BlockInfo>
{
    @Override
    public JsonElement serialize(BlockInfo src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject obj = new JsonObject();
        
        obj.addProperty("name", src.getBlock().getRegistryName() == null ? "" : src.getBlock().getRegistryName().toString());
        obj.addProperty("meta", src.getMeta());
        
        return obj;
    }
    
    @Override
    public BlockInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonHelper helper = new JsonHelper(json);
        
        String name = helper.getString("name");
        int meta = helper.getNullableInteger("meta", 0);

        Block block = Block.getBlockFromName(name);
        
        if(block == null)
        {
            LogUtil.error("Error parsing JSON: Invalid Block: " + json.toString());
            LogUtil.error("This may result in crashing or other undefined behavior");
        }
        
        return new BlockInfo(block, meta);
    }
}
