package exnihiloadscensio.tiles;

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
	private BarrelMode mode;
	
	public TileBarrel()
	{
		fluidTank = new FluidTank(1000);
	}
	
	@Override
	public void update()
	{
		if (worldObj.isRemote)
			return;
	}
	
	public void writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound fluidTag = new NBTTagCompound();
		fluidTank.writeToNBT(fluidTag);
		tag.setTag("tank", fluidTag);
		
		tag.setInteger("mode", mode.getNumber())
	}
	
	public void readFromNBT(NBTTagCompound tag)
	{
		if (tag.hasKey("tank"))
			fluidTank.readFromNBT((NBTTagCompound) tag.getTag("tank"));
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
