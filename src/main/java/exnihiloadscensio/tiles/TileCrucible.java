package exnihiloadscensio.tiles;

import lombok.Getter;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.CrucibleRegistry;
import exnihiloadscensio.registries.HeatRegistry;
import exnihiloadscensio.registries.types.Meltable;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileCrucible extends TileEntity implements ITickable {
	
	private FluidTank tank;
	private int solidAmount;
	@Getter
	private ItemInfo currentItem;
	
	private int ticksSinceLast = 0;
	
	private CrucibleItemHandler itemHandler;
	
	private static final int MAX_ITEMS = 4;
	
	public TileCrucible() {
		tank = new FluidTank(4*Fluid.BUCKET_VOLUME);
		itemHandler = new CrucibleItemHandler();
	}

	@Override
	public void update() {
		if (worldObj.isRemote)
			return;
		
		ticksSinceLast++;
		if (ticksSinceLast >= 10) {
			ticksSinceLast = 0;
			if (getHeatRate() <= 0)
				return;
			
			Fluid fluidToFill;
			if (solidAmount > 0) {
				Meltable meltable = CrucibleRegistry.getMeltable(currentItem);
				fluidToFill = FluidRegistry.getFluid(meltable.getFluid());
			}
			else {
				ItemStack toMelt = itemHandler.getStackInSlot(0);
				if (toMelt == null) {
					return; //If we get to here, there is nothing left to melt.
				}
				Meltable meltable = CrucibleRegistry.getMeltable(toMelt);
				solidAmount = meltable.getAmount();
				currentItem = new ItemInfo(toMelt);
				
				toMelt.stackSize--;
				if (toMelt.stackSize == 0)
					itemHandler.setStackInSlot(0, null);
				else
					itemHandler.setStackInSlot(0, toMelt);
				
				fluidToFill = FluidRegistry.getFluid(meltable.getFluid());
			}
			//Check if the tank can take the fluid.
			FluidStack fStack = new FluidStack(fluidToFill, getHeatRate());
			int fillAmount = tank.fill(fStack, false);
			if (fillAmount > 0) {
				tank.fill(fStack, true);
				solidAmount -= getHeatRate(); //Moving items into the "meltable" slot is handled earlier.
				PacketHandler.sendNBTUpdate(this);
			}
		}
		
	}
	
	private int getHeatRate() {
		BlockPos posBelow = new BlockPos(pos.getX(), pos.getY()-1, pos.getZ());
		IBlockState blockBelow = worldObj.getBlockState(posBelow);
		
		if (blockBelow == null)
			return 0;
		
		return HeatRegistry.getHeatAmount(new ItemInfo(blockBelow));
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture() {
		int noItems = itemHandler.getStackInSlot(0) == null ? 0 : 
			itemHandler.getStackInSlot(0).stackSize;
		if (noItems == 0 && currentItem == null && tank.getFluidAmount() == 0) //Empty!
			return null;
		
		if (noItems == 0 && currentItem == null) //Nothing being melted.
			return Util.getTextureFromBlockState(tank.getFluid().getFluid().getBlock().getDefaultState());
		
		double solidProportion = ((double) noItems) / MAX_ITEMS;
		if (currentItem != null) {
			Meltable meltable = CrucibleRegistry.getMeltable(currentItem);
			solidProportion += ((double) solidAmount) / (MAX_ITEMS * meltable.getAmount());
		}
		
		double fluidProportion = ((double) tank.getFluidAmount()) / tank.getCapacity();
		if (fluidProportion > solidProportion)
			return Util.getTextureFromBlockState(tank.getFluid().getFluid().getBlock().getDefaultState());
		else {
			IBlockState block = Block.getBlockFromItem(currentItem.getItem())
					.getStateFromMeta(currentItem.getMeta());
			return Util.getTextureFromBlockState(block);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public float getFilledAmount() {
		int noItems = itemHandler.getStackInSlot(0) == null ? 0 : itemHandler.getStackInSlot(0).stackSize;
		if (noItems == 0 && currentItem == null && tank.getFluidAmount() == 0) //Empty!
			return 0;
		
		float fluidProportion = ((float) tank.getFluidAmount()) / tank.getCapacity();
		if (noItems == 0 && currentItem == null) //Nothing being melted.
			return fluidProportion;
		
		float solidProportion = ((float) noItems) / MAX_ITEMS;
		if (currentItem != null) {
			Meltable meltable = CrucibleRegistry.getMeltable(currentItem);
			solidProportion += ((double) solidAmount) / (MAX_ITEMS * meltable.getAmount() );
		}

		return solidProportion > fluidProportion ? solidProportion : fluidProportion;
	}
	
	public boolean onBlockActivated(ItemStack stack, EntityPlayer player) {
		if (stack == null)
			return false;
		
		//Bucketing out the fluid.
		if (stack != null && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
			IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			FluidStack fluid = tank.drain(Fluid.BUCKET_VOLUME, false);
			int amount = handler.fill(fluid, true);
			if (amount != 0) {
				tank.drain(Fluid.BUCKET_VOLUME, true);
				PacketHandler.sendNBTUpdate(this);
			}
			return true;
		}
		
		//Adding a meltable.
		ItemStack addStack = stack.copy(); addStack.stackSize = 1;
		ItemStack insertStack = itemHandler.insertItem(0, addStack, true);
		if (!ItemStack.areItemStacksEqual(addStack, insertStack)) {
			itemHandler.insertItem(0, addStack, false);
			stack.stackSize--;
			PacketHandler.sendNBTUpdate(this);
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			itemHandler.setTe(this);
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
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		if (currentItem != null) {
			NBTTagCompound currentItemTag = currentItem.writeToNBT(new NBTTagCompound());
			tag.setTag("currentItem", currentItemTag);
		}
		tag.setInteger("solidAmount", solidAmount);
		NBTTagCompound itemHandlerTag = itemHandler.serializeNBT();
		tag.setTag("itemHandler", itemHandlerTag);
		
		NBTTagCompound tankTag = tank.writeToNBT(new NBTTagCompound());
		tag.setTag("tank", tankTag);
		
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("currentItem")) {
			currentItem = ItemInfo.readFromNBT(tag.getCompoundTag("currentItem"));
		}
		solidAmount = tag.getInteger("solidAmount");
		if (tag.hasKey("itemHandler"))	{
			itemHandler.deserializeNBT((NBTTagCompound) tag.getTag("itemHandler"));
		}
		if (tag.hasKey("tank")) {
			tank.readFromNBT((NBTTagCompound) tag.getTag("tank"));
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

}
