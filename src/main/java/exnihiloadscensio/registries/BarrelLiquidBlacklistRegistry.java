package exnihiloadscensio.registries;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BarrelLiquidBlacklistRegistry
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<Integer, List<String>> blacklist = new HashMap<>();
    
    public static boolean isBlacklisted(int level, String fluid)
    {
        return level < 0 || blacklist.getOrDefault(level, Collections.EMPTY_LIST).contains(fluid);
    }
    
    public static void register(int level, String fluid)
    {
        List<String> list = blacklist.get(level);
        
        if(list == null)
        {
            list = new ArrayList<>();
            blacklist.put(level, list);
        }
        
        list.add(fluid);
    }
    
    public static void registerDefaults()
    {
        register(0, "lava");
        register(0, "fire_water");
        register(0, "rocket_fuel");
    }
    
    public static void loadJson(File file)
    {
        blacklist.clear();
        
        if(file.exists())
        {
            try
            {
                String json = new String(Files.readAllBytes(file.toPath()));
                
                Map<Integer, List<String>> loaded = gson.fromJson(json, new TypeToken<Map<Integer, List<String>>>(){}.getType());
                blacklist.putAll(Maps.transformValues(loaded, list -> new ArrayList<>(list)));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            registerDefaults();
            saveJson(file);
        }
    }
    
    public static void saveJson(File file)
    {
        try
        {
            String json = gson.toJson(blacklist, new TypeToken<Map<Integer, List<String>>>(){}.getType());
            
            Files.write(file.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
