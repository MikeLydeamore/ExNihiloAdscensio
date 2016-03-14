package exnihiloadscensio.tiles;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry.TriggerType;
import exnihiloadscensio.util.BarrelMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

public class TileBarrel extends TileEntity implements ITickable, ISidedInventory {

	private ItemStack processingStack;
	private FluidTank fluidTank;
	@Getter
	private IBarrelMode mode;

	public TileBarrel()
	{
		fluidTank = new FluidTank(1000);
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (mode == null)
		{
			if (player.getHeldItem() != null)
			{
				ItemStack stack = player.getHeldItem();
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
						this.worldObj.markBlockForUpdate(pos);
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
			mode.readFromNBT(barrelModeTag);
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
    {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new S35PacketUpdateTileEntity(this.pos, this.getBlockMetadata(), tag);
    }
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
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
			this.worldObj.markBlockForUpdate(pos);
		} catch (Exception e)
		{
			e.printStackTrace(); //Naughty
		}
	}
	
	public void setMode(IBarrelMode mode)
	{
		this.mode = mode;
	}
	/* *****
	 * ISIDEDINVENTORY
	 * *****/


	@Override
	public int getSizeInventory() 
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) 
	{
		if (index == 0)
			return processingStack;

		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		return null; //Can't extract.
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		if (index == 0)
			processingStack = stack;		
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			EnumFacing direction) 
	{
		return false;
	}

}
