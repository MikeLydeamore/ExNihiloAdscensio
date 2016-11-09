package exnihiloadscensio.capabilities;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class ENCapabilities
{
    public static void init()
    {
        CapabilityManager.INSTANCE.register(ICapabilityHeat.class, CapabilityHeatManager.INSTANCE, CapabilityHeatManager.INSTANCE);
    }
}
