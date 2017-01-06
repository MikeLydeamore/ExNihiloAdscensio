package exnihiloadscensio.barrel;

import javax.annotation.Nullable;

import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.MessageFluidUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelLiquidBlacklistRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry.TriggerType;
import exnihiloadscensio.tiles.TileBarrel;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class BarrelFluidHandler extends FluidTank {
	
	private TileBarrel barrel;
	
	public BarrelFluidHandler(TileBarrel barrel) {
		this(Fluid.BUCKET_VOLUME);
		this.barrel = barrel;
	}
	
    public BarrelFluidHandler(int capacity) {
        super(capacity);
    }

    public BarrelFluidHandler(@Nullable FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
    }

    public BarrelFluidHandler(Fluid fluid, int amount, int capacity)  {
        super(fluid, amount, capacity);
    }
    
    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        if(fluid == null || fluid.getFluid() == null || BarrelLiquidBlacklistRegistry.isBlacklisted(barrel.getTier(), fluid.getFluid().getName()))
            return false;
        
    	for (IBarrelMode mode : BarrelModeRegistry.getModes(TriggerType.FLUID))	{
    		if (mode.isTriggerFluidStack(fluid))
    			return true;
    	}
        return false;
    }
    
    @Override
    public boolean canFill() {
    	return barrel.getMode() == null ? true : barrel.getMode().canFillWithFluid(barrel);
    }
    
    @Override
    public int fill(FluidStack resource, boolean doFill) {
    	if (barrel.getMode() != null && !barrel.getMode().canFillWithFluid(barrel))
    		return 0;
    	
    	int amount = super.fill(resource, doFill);
    	if (amount > 0) {
    		PacketHandler.sendToAllAround(new MessageFluidUpdate(fluid, barrel.getPos()), barrel);
        	if (this.fluid != null && this.barrel.getMode() == null) {
        		this.barrel.setMode("fluid");
        		PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(barrel.getMode().getName(), barrel.getPos()), barrel);
        	}
    	}
    	return amount;
    }
    
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
    	FluidStack stack = super.drain(resource, doDrain);
    	if (stack != null && stack.amount > 0) {
    		PacketHandler.sendToAllAround(new MessageFluidUpdate(fluid, barrel.getPos()), barrel);
    	}
    	if (fluid == null && barrel.getMode() != null && barrel.getMode().getName().equals("fluid")) {
    		barrel.setMode("null");
    		PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
    	}
    	return stack;
    }
    
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
    	FluidStack stack = super.drain(maxDrain, doDrain);
    	if (stack != null && stack.amount > 0) {
    		PacketHandler.sendToAllAround(new MessageFluidUpdate(fluid, barrel.getPos()), barrel);
    	}
    	if (fluid == null && barrel.getMode() != null && barrel.getMode().getName().equals("fluid")) {
    		barrel.setMode("null");
    		PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
    	}
    	return stack;
    }
    
    @Override
    protected void onContentsChanged() {
    	
    }
    
    public void setBarrel(TileBarrel barrel) {
    	this.barrel = barrel;
    }

}
