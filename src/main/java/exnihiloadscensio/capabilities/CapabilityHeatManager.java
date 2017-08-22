package exnihiloadscensio.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.concurrent.Callable;

public class CapabilityHeatManager implements Capability.IStorage<ICapabilityHeat>, Callable<ICapabilityHeat>
{
    public static final CapabilityHeatManager INSTANCE = new CapabilityHeatManager();
    @CapabilityInject(ICapabilityHeat.class)
    public static Capability<ICapabilityHeat> HEAT_CAPABILITY;
    
    
    private CapabilityHeatManager()
    {
        
    }
    
    @Override
    public NBTBase writeNBT(Capability<ICapabilityHeat> capability, ICapabilityHeat instance, EnumFacing side)
    {
        return new NBTTagInt(instance.getHeatRate());
    }
    
    @Override
    public void readNBT(Capability<ICapabilityHeat> capability, ICapabilityHeat instance, EnumFacing side, NBTBase nbt)
    {
        instance.setHeatRate(((NBTTagInt) nbt).getInt());
    }

    @Override
    public ICapabilityHeat call() throws Exception
    {
        return new CapabilityHeat();
    }
}
