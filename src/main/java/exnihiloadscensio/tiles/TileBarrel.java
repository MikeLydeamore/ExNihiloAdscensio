package exnihiloadscensio.tiles;

import java.util.ArrayList;

import exnihiloadscensio.barrel.BarrelFluidHandler;
import exnihiloadscensio.barrel.BarrelItemHandler;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.blocks.BlockBarrel;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.MessageCheckLight;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry.TriggerType;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileBarrel extends TileEntity implements ITickable {

	@Getter
	private IBarrelMode mode;
	@Getter
	private BarrelItemHandler itemHandler;
	@Getter
	private BarrelFluidHandler tank;
	@Getter
	private int tier;
	
	public TileBarrel()
	{
	    this(ENBlocks.barrelWood);
	}
	
	public TileBarrel(BlockBarrel block)
	{
	    this.tier = block.getTier();
	    this.blockType = block;
		itemHandler = new BarrelItemHandler(this);
		tank = new BarrelFluidHandler(this);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (mode == null || mode.getName().equals("fluid"))
        {
            ItemStack stack = player.getHeldItemMainhand();
            boolean result = FluidUtil.interactWithFluidHandler(stack, this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side), player);

            if (result)
            {
            	PacketHandler.sendNBTUpdate(this);
                if(getBlockType().getLightValue(state, world, pos) != world.getLight(pos))
                {
                    world.checkLight(pos);
                    PacketHandler.sendToAllAround(new MessageCheckLight(pos), this);
                }
                
                return true;
            }
            
            //Check for more fluid
            IFluidHandler tank = (IFluidHandler) this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            FluidStack bucketStack = FluidUtil.getFluidContained(stack);
            FluidStack tankStack = tank.drain(Integer.MAX_VALUE, false);
            if (bucketStack != null && tankStack != null 
            		&& bucketStack.getFluid() == tankStack.getFluid()
            		&& tank.fill(FluidUtil.getFluidContained(stack), false) != 0) {
            	tank.drain(Fluid.BUCKET_VOLUME, true);
               	FluidUtil.interactWithFluidHandler(stack, tank, player);
               	PacketHandler.sendNBTUpdate(this);
            }
        }
        
        if (mode == null)
        {
            if (player.getHeldItem(EnumHand.MAIN_HAND) != null)
            {
                ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
                ArrayList<IBarrelMode> modes = BarrelModeRegistry.getModes(TriggerType.ITEM);
                if (modes == null)
                    return false;
                for (IBarrelMode possibleMode : modes)
                {
                    if (possibleMode.isTriggerItemStack(stack))
                    {
                        setMode(possibleMode.getName());
                        PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(mode.getName(), this.pos), this);
                        mode.onBlockActivated(world, this, pos, state, player, side, hitX, hitY, hitZ);
                        this.markDirty();
                        this.world.setBlockState(pos, state);
                        
                        if(getBlockType().getLightValue(state, world, pos) != world.getLight(pos))
                        {
                            world.checkLight(pos);
                            PacketHandler.sendToAllAround(new MessageCheckLight(pos), this);
                        }
                        
                        return true;
                    }
                }
            }
        }
        else
        {
            mode.onBlockActivated(world, this, pos, state, player, side, hitX, hitY, hitZ);
            
            if(getBlockType().getLightValue(state, world, pos) != world.getLight(pos))
            {
                world.checkLight(pos);
                PacketHandler.sendToAllAround(new MessageCheckLight(pos), this);
            }
            
            return true;
        }
        
        return true;
    }

	@Override
	public void update()
	{
		if (world.isRemote)
			return;

		if (Config.shouldBarrelsFillWithRain && (mode == null || mode.getName() == "fluid")) {
			BlockPos plusY = new BlockPos(pos.getX(), pos.getY()+1, pos.getZ());
			if(world.isRainingAt(plusY)) {
				FluidStack stack = new FluidStack(FluidRegistry.WATER, 2);
				tank.fill(stack, true);
			}
		}
		if (mode != null)
			mode.update(this);
        
		if(getBlockType().getLightValue(world.getBlockState(pos), world, pos) != world.getLight(pos))
        {
            world.checkLight(pos);
            PacketHandler.sendToAllAround(new MessageCheckLight(pos), this);
        }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tank.writeToNBT(tag);

		if (mode != null)
		{
			NBTTagCompound barrelModeTag = new NBTTagCompound();
			mode.writeToNBT(barrelModeTag);
			barrelModeTag.setString("name", mode.getName());
			tag.setTag("mode", barrelModeTag);
		}

		NBTTagCompound handlerTag = itemHandler.serializeNBT();
		tag.setTag("itemHandler", handlerTag);
		tag.setInteger("barrelTier", tier);
		
		return super.writeToNBT(tag);

	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		tank.readFromNBT(tag);
		if (tag.hasKey("mode"))
		{
			NBTTagCompound barrelModeTag = (NBTTagCompound) tag.getTag("mode");
			this.setMode(barrelModeTag.getString("name"));
			if (mode != null)
				mode.readFromNBT(barrelModeTag);
		}

		if (tag.hasKey("itemHandler"))
		{
			itemHandler.deserializeNBT((NBTTagCompound) tag.getTag("itemHandler"));
		}
		
		if(tag.hasKey("barrelTier"))
		{
		    tier = tag.getInteger("barrelTier");
		}
		super.readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = writeToNBT(new NBTTagCompound());
		return tag;
	}

	public void setMode(String modeName)
	{
		try 
		{
			if (modeName.equals("null"))
				mode = null;
			else
				mode = BarrelModeRegistry.getModeByName(modeName).getClass().newInstance();
			this.markDirty();
		} catch (Exception e)
		{
			e.printStackTrace(); //Naughty
		}
	}

	public void setMode(IBarrelMode mode)
	{
		this.mode = mode;
		this.markDirty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return (T) itemHandler;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;

		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
				capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
				super.hasCapability(capability, facing);
	}

}
