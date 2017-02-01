package exnihiloadscensio.json;

import com.google.gson.*;
import exnihiloadscensio.util.LogUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;

public class CustomItemStackJson implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonHelper helper = new JsonHelper(json);
        
        String name = helper.getString("name");
        int amount = helper.getNullableInteger("amount", 1);
        int meta = helper.getNullableInteger("meta", 0);
        
        Item item = Item.getByNameOrId(name);
        
        if(item == null)
        {
            LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString());
            LogUtil.error("This may result in crashing or other undefined behavior");

            item = Items.AIR;
        }
        
        return new ItemStack(item, amount, meta);
    }
    
    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("name", src.getItem().getRegistryName() == null ? "" : src.getItem().getRegistryName().toString());
        jsonObject.addProperty("amount", src.getCount());
        jsonObject.addProperty("meta", src.getItemDamage());
        
        return jsonObject;
    }
}
