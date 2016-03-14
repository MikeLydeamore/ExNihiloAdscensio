package exnihiloadscensio.config;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ItemTypeAdapter extends TypeAdapter<Item> {

	@Override
	public void write(JsonWriter out, Item value) throws IOException 
	{
		String[] str = Item.itemRegistry.getNameForObject(value).toString().split(":");
		out.beginObject();
		out.name("modid").value(str[0]);
		out.name("item").value(str[1]);
		out.endObject();		
	}

	@Override
	public Item read(JsonReader in) throws IOException 
	{
		String[] str = new String[2];
		in.beginObject();
		while (in.hasNext())
		{
			String next = in.nextName();
			if (next.equals("modid"))
				str[0] = in.nextString();
			else if (next.equals("item"))
				str[1] = in.nextString();
		}
		in.endObject();
		
		return Item.itemRegistry.getObject(new ResourceLocation(str[0]+":"+str[1]));
	}

}
