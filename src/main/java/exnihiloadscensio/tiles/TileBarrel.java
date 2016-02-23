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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
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

	public boolean onBlockActivated(EntityPlayer player, EnumFacing side)
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
						mode = possibleMode;
						PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(mode.getClass().getName(), this.pos), this);
						this.markDirty();
						this.worldObj.markBlockForUpdate(pos);
						return true;
					}
				}
			}
		}
		else
		{

		}

		return false;
	}

	@Override
	public void update()
	{
		if (mode != null)
			System.out.println(mode.getName());
		else
			System.out.println("null");
		if (worldObj.isRemote)
			return;
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
