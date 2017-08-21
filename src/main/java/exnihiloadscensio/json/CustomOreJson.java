package exnihiloadscensio.json;

import com.google.gson.*;
import exnihiloadscensio.items.ore.Ore;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;

import java.lang.reflect.Type;

public class CustomOreJson implements JsonDeserializer<Ore>, JsonSerializer<Ore>
{
    @Override
    public JsonElement serialize(Ore src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getName());
        obj.add("color", context.serialize(src.getColor(), Color.class));
        obj.add("result", context.serialize(src.getResult(), ItemInfo.class));
        
        return obj;
    }
    
    @Override
    public Ore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonHelper helper = new JsonHelper(json);
        
        String name = helper.getString("name");
        Color color = context.deserialize(json.getAsJsonObject().get("color"), Color.class);
        ItemInfo result = context.deserialize(json.getAsJsonObject().get("result"), ItemInfo.class);
        
        return new Ore(name, color, result);
    }
}
