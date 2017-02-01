package exnihiloadscensio.json;

import com.google.gson.*;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.LogUtil;
import net.minecraft.item.Item;

import java.lang.reflect.Type;

public class CustomItemInfoJson implements JsonDeserializer<ItemInfo>, JsonSerializer<ItemInfo>
{
    @Override
    public JsonElement serialize(ItemInfo src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject obj = new JsonObject();
        
        obj.addProperty("name", src.getItem().getRegistryName() == null ? "" : src.getItem().getRegistryName().toString());
        obj.addProperty("meta", src.getMeta());
        
        return obj;
    }
    
    @Override
    public ItemInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonHelper helper = new JsonHelper(json);
        
        String name = helper.getString("name");
        int meta = helper.getNullableInteger("meta", 0);

        Item item = Item.getByNameOrId(name);
        
        if(item == null)
        {
            LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString());
            LogUtil.error("This may result in crashing or other undefined behavior");
        }
        
        return new ItemInfo(item, meta);
    }
}
