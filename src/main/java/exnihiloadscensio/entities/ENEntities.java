package exnihiloadscensio.entities;

import exnihiloadscensio.ExNihiloAdscensio;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ENEntities
{
    public static void init()
    {
        EntityRegistry.registerModEntity(ProjectileStone.class, "Thrown Stone", 0, ExNihiloAdscensio.instance, 64, 10, true);
    }
}
