package exnihiloadscensio.tiles;

import exnihiloadscensio.capabilities.CapabilityHeatManager;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.CrucibleRegistry;
import exnihiloadscensio.registries.HeatRegistry;
import exnihiloadscensio.registries.types.Meltable;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.LogUtil;
import exnihiloadscensio.util.Util;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class TileCrucible extends TileEntity implements ITickable {
	
    @Getter
	private FluidTank tank;
	@Getter
	private int solidAmount;
	@Getter
	private ItemInfo currentItem;
	
	private int ticksSinceLast = 0;
	@Getter
	private CrucibleItemHandler itemHandler;
	
	private static final int MAX_ITEMS = 4;
	
	public TileCrucible() {
		tank = new FluidTank(4*Fluid.BUCKET_VOLUME);
		tank.setCanFill(false);
		itemHandler = new CrucibleItemHandler(); itemHandler.setTe(this);
	}
    
    @Override
    public void update()
    {
        if (getWorld().isRemote)
            return;
        
        ticksSinceLast++;
        
        if (ticksSinceLast >= 10)
        {
            ticksSinceLast = 0;

            int heatRate = getHeatRate();
            
            if (heatRate <= 0)
                return;
            
            if(solidAmount <= 0)
            {
                if(!itemHandler.getStackInSlot(0).isEmpty())
                {
                    currentItem = new ItemInfo(itemHandler.getStackInSlot(0));
                    itemHandler.getStackInSlot(0).shrink(1);
                    
                    if(itemHandler.getStackInSlot(0).isEmpty())
                    {
                        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    }
                    
                    solidAmount = CrucibleRegistry.getMeltable(currentItem).getAmount();
                }
                else
                {
                    if(currentItem != null)
                    {
                        currentItem = null;
    
                        PacketHandler.sendNBTUpdate(this);
                    }
                    
                    return;
                }
            }
            
            if(!itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).isItemEqual(currentItem.getItemStack()))
            {
                // For meltables with a really small "amount"
                while(heatRate > solidAmount && !itemHandler.getStackInSlot(0).isEmpty())
                {
                    solidAmount += CrucibleRegistry.getMeltable(currentItem).getAmount();
                    itemHandler.getStackInSlot(0).shrink(1);
                    
                    if (itemHandler.getStackInSlot(0).isEmpty())
                    {
                        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    }
                }
            }
            
            // Never take more than we have left
            if(heatRate > solidAmount)
            {
                heatRate = solidAmount;
            }
            
            if(heatRate > 0 && currentItem != null && CrucibleRegistry.canBeMelted(currentItem))
            {
                FluidStack toFill = new FluidStack(FluidRegistry.getFluid(CrucibleRegistry.getMeltable(currentItem).getFluid()), heatRate);
                int filled = tank.fillInternal(toFill, true);
                solidAmount -= filled;
                
                if(filled > 0)
                {
                    PacketHandler.sendNBTUpdate(this);
                }
            }
        }
    }
    
    public int getHeatRate()
    {
        BlockPos posBelow = pos.add(0, -1, 0);
        IBlockState stateBelow = getWorld().getBlockState(posBelow);
        
        if (stateBelow == Blocks.AIR.getDefaultState())
        {
            return 0;
        }
        
        int heat = HeatRegistry.getHeatAmount(new BlockInfo(stateBelow));
        
        if(heat != 0)
        {
            return heat;
        }
        
        TileEntity tile = getWorld().getTileEntity(posBelow);
        
        if(tile != null && tile.hasCapability(CapabilityHeatManager.HEAT_CAPABILITY, EnumFacing.UP))
        {
            return tile.getCapability(CapabilityHeatManager.HEAT_CAPABILITY, EnumFacing.UP).getHeatRate();
        }
        
        return 0;
    }
	
	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture() {
		int noItems = itemHandler.getStackInSlot(0).isEmpty() ? 0 :
			itemHandler.getStackInSlot(0).getCount();
		if (noItems == 0 && currentItem == null && tank.getFluidAmount() == 0) //Empty!
			return null;
		
		if (noItems == 0 && currentItem == null) //Nothing being melted.
		{
			if (tank.getFluid() == null) {
				return Util.getTextureFromBlockState(Blocks.AIR.getDefaultState());
			}

			return Util.getTextureFromBlockState(tank.getFluid().getFluid().getBlock().getDefaultState());
		}
		
		double solidProportion = ((double) noItems) / MAX_ITEMS;
		
		if (currentItem != null)
		{
			Meltable meltable = CrucibleRegistry.getMeltable(currentItem);
			
			if(meltable != null)
			{
			    solidProportion += ((double) solidAmount) / (MAX_ITEMS * meltable.getAmount());
			}
			else
			{
			    LogUtil.throwing(new NullPointerException("Meltable is null! Item is " + currentItem.getItem().getUnlocalizedName()));
			}
		}
		
		double fluidProportion = ((double) tank.getFluidAmount()) / tank.getCapacity();
		if (fluidProportion > solidProportion) {
			if (tank.getFluid() == null || tank.getFluid().getFluid() == null || tank.getFluid().getFluid().getBlock() == null)
				return null;
			
			return Util.getTextureFromBlockState(tank.getFluid().getFluid().getBlock().getDefaultState());
		}
		else {
			if (currentItem != null) {
				IBlockState block = Block.getBlockFromItem(currentItem.getItem())
						.getStateFromMeta(currentItem.getMeta());
				return Util.getTextureFromBlockState(block);
			}
			IBlockState block = Block.getBlockFromItem(itemHandler.getStackInSlot(0).getItem())
					.getStateFromMeta(itemHandler.getStackInSlot(0).getItemDamage());
			return Util.getTextureFromBlockState(block);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public float getFilledAmount() {
		int noItems = itemHandler.getStackInSlot(0).isEmpty() ? 0 : itemHandler.getStackInSlot(0).getCount();
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
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.isEmpty()) {
			return false;
		}

		FluidActionResult result = FluidUtil.interactWithFluidHandler(stack, tank, player);

		if (result.isSuccess()) {
			if (!player.isCreative()) {
				player.setHeldItem(hand, result.getResult());
			}

			PacketHandler.sendNBTUpdate(this);

			return true;
		}
		
		//Adding a meltable.
		ItemStack addStack = stack.copy(); addStack.setCount(1);
		ItemStack insertStack = itemHandler.insertItem(0, addStack, true);
		if (!ItemStack.areItemStacksEqual(addStack, insertStack)) {
			itemHandler.insertItem(0, addStack, false);
			stack.shrink(1);
			PacketHandler.sendNBTUpdate(this);
			return true;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
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
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
				capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
				super.hasCapability(capability, facing);
    }
    
    @Override @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        if (currentItem != null)
        {
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
		if (tag.hasKey("currentItem"))
		{
			currentItem = ItemInfo.readFromNBT(tag.getCompoundTag("currentItem"));
		}
		else
		{
		    currentItem = null;
		}
		
		solidAmount = tag.getInteger("solidAmount");
		
		if (tag.hasKey("itemHandler"))
		{
			itemHandler.deserializeNBT((NBTTagCompound) tag.getTag("itemHandler"));
		}
		
		if (tag.hasKey("tank"))
		{
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

	@Override @SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		readFromNBT(tag);
	}

	@Override @Nonnull
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

}
