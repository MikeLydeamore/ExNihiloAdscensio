package exnihiloadscensio.tiles;

import java.util.ArrayList;

import lombok.Getter;
import exnihiloadscensio.barrel.BarrelItemHandler;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry.TriggerType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileBarrel extends TileEntity implements ITickable {

	private ItemStack processingStack;
	private FluidTank fluidTank;
	@Getter
	private IBarrelMode mode;
	
	private BarrelItemHandler itemHandler;

	public TileBarrel()
	{
		fluidTank = new FluidTank(1000);
		itemHandler = new BarrelItemHandler(this);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
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
						setMode(possibleMode.getClass().getName());
						PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(mode.getClass().getName(), this.pos), this);
						mode.onBlockActivated(world, this, pos, state, player, side, hitX, hitY, hitZ);
						this.markDirty();
						this.worldObj.notifyBlockUpdate(pos, state, state, 3);
						return true;
					}
				}
			}
		}
		else
		{
			return mode.onBlockActivated(world, this, pos, state, player, side, hitX, hitY, hitZ);
		}

		return false;
	}

	@Override
	public void update()
	{
		if (worldObj.isRemote)
			return;
		if (mode != null)
			mode.update(this);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		NBTTagCompound fluidTag = new NBTTagCompound();
		fluidTank.writeToNBT(fluidTag);
		tag.setTag("tank", fluidTag);

		if (mode != null)
		{
			NBTTagCompound barrelModeTag = new NBTTagCompound();
			mode.writeToNBT(barrelModeTag);
			barrelModeTag.setString("name", mode.getClass().getName());
			tag.setTag("mode", barrelModeTag);
		}
		
		NBTTagCompound handlerTag = itemHandler.serializeNBT();
		tag.setTag("itemHandler", handlerTag);

	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		if (tag.hasKey("tank"))
			fluidTank.readFromNBT((NBTTagCompound) tag.getTag("tank"));
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
	}
	
	@Override
	public Packet getDescriptionPacket()
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

	public void setMode(String modeName)
	{
		try 
		{
			mode = (IBarrelMode) Class.forName(modeName).newInstance();
			this.markDirty();
			IBlockState state = this.worldObj.getBlockState(pos);
			this.worldObj.notifyBlockUpdate(pos, state, state, 3);
		} catch (Exception e)
		{
			e.printStackTrace(); //Naughty
		}
	}
	
	public void setMode(IBarrelMode mode)
	{
		this.mode = mode;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (this.mode == null)
				return (T) itemHandler;
			else
				return (T) this.mode.getHandler(this);
		}

		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

}
