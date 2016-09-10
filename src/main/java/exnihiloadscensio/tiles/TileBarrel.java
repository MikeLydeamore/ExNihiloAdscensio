package exnihiloadscensio.tiles;

import java.util.ArrayList;

import lombok.Getter;
import exnihiloadscensio.barrel.BarrelFluidHandler;
import exnihiloadscensio.barrel.BarrelItemHandler;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry.TriggerType;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileBarrel extends TileEntity implements ITickable {

	@Getter
	private IBarrelMode mode;

	private BarrelItemHandler itemHandler;
	@Getter
	private BarrelFluidHandler tank;

	public TileBarrel()
	{
		itemHandler = new BarrelItemHandler(this);
		tank = new BarrelFluidHandler(this);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (mode == null || mode.getName().equals("fluid")) {
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
				IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				FluidStack drainStack = handler.drain(Fluid.BUCKET_VOLUME, false);
				int amount = this.getTank().fill(drainStack, false);
				
				if (amount > 0) {
					handler.drain(Fluid.BUCKET_VOLUME, true);
					this.getTank().fill(drainStack, true);
					this.setMode("fluid");
					PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("fluid", pos), this);
					return true;
				}
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
						this.worldObj.setBlockState(pos, state);
						return true;
					}
				}
			}
		}
		else
		{
			return mode.onBlockActivated(world, this, pos, state, player, side, hitX, hitY, hitZ);
		}

		return true;
	}

	@Override
	public void update()
	{
		if (worldObj.isRemote)
			return;

		if (Config.shouldBarrelsFillWithRain && (mode == null || mode.getName() == "fluid")) {
			BlockPos plusY = new BlockPos(pos.getX(), pos.getY()+1, pos.getZ());
			if(worldObj.isRainingAt(plusY)) {
				FluidStack stack = new FluidStack(FluidRegistry.WATER, 2);
				tank.fill(stack, true);
			}
		}
		if (mode != null)
			mode.update(this);
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
