package exnihiloadscensio.barrel.modes.fluid;

import exnihiloadscensio.barrel.modes.mobspawn.BarrelModeMobSpawn;
import exnihiloadscensio.items.ItemDoll;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.FluidBlockTransformerRegistry;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.ItemInfo;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BarrelItemHandlerFluid extends ItemStackHandler {
	
	@Setter
	private TileBarrel barrel;
	
	public BarrelItemHandlerFluid(TileBarrel barrel) {
		super(1);
		this.barrel = barrel;
	}
    
    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        FluidTank tank = barrel.getTank();
        
        if (tank.getFluid() == null)
            return stack;
        
        if (FluidBlockTransformerRegistry.canBlockBeTransformedWithThisFluid(tank.getFluid().getFluid(), stack) && tank.getFluidAmount() == tank.getCapacity())
        {
            ItemInfo info = FluidBlockTransformerRegistry.getBlockForTransformation(tank.getFluid().getFluid(), stack);
            
            if (info != null)
            {               
                if (!simulate)
                {
                    tank.drain(tank.getCapacity(), true);
                    barrel.setMode("block");
                    PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("block", barrel.getPos()), barrel);
                    
                    barrel.getMode().addItem(info.getItemStack(), barrel);
                }

                ItemStack ret = stack.copy();
                ret.shrink(1);
                
                return ret;
            }
            
        }
        
        if (!stack.isEmpty() && tank.getFluidAmount() == tank.getCapacity() && stack.getItem() instanceof ItemDoll
				&& ((ItemDoll) stack.getItem()).getSpawnFluid(stack) == tank.getFluid().getFluid()) {
			if (!simulate) {
				barrel.getTank().drain(Fluid.BUCKET_VOLUME, true);
				barrel.setMode("mobspawn");				
				((BarrelModeMobSpawn) barrel.getMode()).setDollStack(stack);
				PacketHandler.sendNBTUpdate(barrel);
			}
			ItemStack ret = stack.copy();
			ret.shrink(1);
			
			return ret;
		}
        
        return stack;
    }
    
    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return ItemStack.EMPTY;
    }
}