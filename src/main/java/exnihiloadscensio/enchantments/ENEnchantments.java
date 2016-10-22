package exnihiloadscensio.enchantments;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class ENEnchantments
{
    public static void init()
    {
        override(new ResourceLocation("minecraft:efficiency"), new EnchantmentEfficiency());
        override(new ResourceLocation("minecraft:fortune"), new EnchantmentFortune());
    }
    
    private static Method registryAddRaw;
    private static Field fieldModifiers;
    
    static
    {
        try
        {
            registryAddRaw = FMLControlledNamespacedRegistry.class.getDeclaredMethod("addObjectRaw", int.class, ResourceLocation.class, IForgeRegistryEntry.class);
            registryAddRaw.setAccessible(true);
            
            fieldModifiers = Field.class.getDeclaredField("modifiers");
            fieldModifiers.setAccessible(true);
        }
        catch (NoSuchMethodException | SecurityException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }
    
    // Overriding Enchantments requires some hackery since Forge protects against it. But it should work. I think.
    private static void override(ResourceLocation key, Enchantment enchantmentNew)
    {
        Enchantment original = Enchantment.REGISTRY.getObject(key);
        int idOriginal = Enchantment.REGISTRY.getIDForObject(original);
        
        if(Enchantment.REGISTRY instanceof FMLControlledNamespacedRegistry)
        {
            try
            {
                registryAddRaw.invoke(Enchantment.REGISTRY, idOriginal, key, enchantmentNew);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Enchantment.REGISTRY.register(idOriginal, key, enchantmentNew);
        }
        
        try
        {
            Field[] enchantments = Enchantments.class.getFields();
            
            for(Field field : enchantments)
            {
                if(Enchantment.class.isAssignableFrom(field.getType()))
                {
                    if(original.equals(field.get(null)))
                    {
                        field.setAccessible(true);
                        fieldModifiers.set(field, field.getModifiers() & ~Modifier.FINAL);
                        field.set(null, enchantmentNew);
                    }
                }
            }
        }
        catch (IllegalAccessException | IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }
}
