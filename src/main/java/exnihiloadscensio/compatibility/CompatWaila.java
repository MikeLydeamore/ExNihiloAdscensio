package exnihiloadscensio.compatibility;

import java.util.List;

import exnihiloadscensio.blocks.BlockBarrel;
import exnihiloadscensio.blocks.BlockCrucible;
import exnihiloadscensio.blocks.BlockInfestedLeaves;
import exnihiloadscensio.blocks.BlockSieve;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.tiles.TileSieve;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CompatWaila implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		if (accessor.getBlock() instanceof BlockBarrel) {
			TileBarrel barrel = (TileBarrel) accessor.getTileEntity();
			
			if (barrel.getMode() != null) {
				currenttip = barrel.getMode().getWailaTooltip(barrel, currenttip);
			}
		}
		
		if (accessor.getBlock() instanceof BlockSieve) {
			TileSieve sieve = (TileSieve) accessor.getTileEntity();
			
			if (sieve.getMeshStack() != null) {
				currenttip.add("Mesh: " + I18n.format(sieve.getMeshStack().getUnlocalizedName() + ".name"));
			}
			else {
			    currenttip.add("Mesh: None");
			}
		}
		
		if(accessor.getBlock() instanceof BlockInfestedLeaves)
		{
		    TileInfestedLeaves tile = (TileInfestedLeaves) accessor.getTileEntity();
		    
		    if(tile.getProgress() >= 1.0F)
		    {
		        currenttip.add("Progress: Done");
		    }
		    else
		    {
		        currenttip.add("Progress: " + Math.round(100 * tile.getProgress()) + "%");
		    }
		}
		
		if(accessor.getBlock() instanceof BlockCrucible)
		{
		    TileCrucible tile = (TileCrucible) accessor.getTileEntity();
		    
		    currenttip.add("Solid:  " + tile.getSolidAmount() + "mb");
		    currenttip.add("Liquid: " + tile.getTank().getFluidAmount() + "mb");
		    currenttip.add("Rate:   " + tile.getHeatRate() + "x");
		}
		
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te,
			NBTTagCompound tag, World world, BlockPos pos) {
		return tag;
	}
	
	public static void callbackRegister(IWailaRegistrar registrar) {
		CompatWaila instance = new CompatWaila();
		registrar.registerBodyProvider(instance, BlockBarrel.class);
		registrar.registerBodyProvider(instance, BlockSieve.class);
		registrar.registerBodyProvider(instance, BlockInfestedLeaves.class);
		registrar.registerBodyProvider(instance, BlockCrucible.class);
	}

}
