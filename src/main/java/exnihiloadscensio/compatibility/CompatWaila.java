package exnihiloadscensio.compatibility;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.BlockBarrel;
import exnihiloadscensio.blocks.BlockCrucible;
import exnihiloadscensio.blocks.BlockInfestedLeaves;
import exnihiloadscensio.blocks.BlockSieve;
import exnihiloadscensio.registries.CrucibleRegistry;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.tiles.TileSieve;
import mcp.mobius.waila.api.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

@WailaPlugin(ExNihiloAdscensio.MODID)
public class CompatWaila implements IWailaPlugin, IWailaDataProvider {

	@Override
	public void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(this, BlockBarrel.class);
		registrar.registerBodyProvider(this, BlockSieve.class);
		registrar.registerBodyProvider(this, BlockInfestedLeaves.class);
		registrar.registerBodyProvider(this, BlockCrucible.class);
	}

	@Override
	@Nonnull
	public ItemStack getWailaStack(IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return ItemStack.EMPTY;
	}

	@Override
	@Nonnull
	public List<String> getWailaHead(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	@Nonnull
	public List<String> getWailaBody(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		if (accessor.getBlock() instanceof BlockBarrel) {
			TileBarrel barrel = (TileBarrel) accessor.getTileEntity();
			
			if (barrel != null && barrel.getMode() != null) {
				currenttip = barrel.getMode().getWailaTooltip(barrel, currenttip);
			}
		}
		
		if (accessor.getBlock() instanceof BlockSieve) {
			TileSieve sieve = (TileSieve) accessor.getTileEntity();
			
			if (sieve != null && sieve.getMeshStack() != null) {
				currenttip.add("Mesh: " + I18n.format(sieve.getMeshStack().getUnlocalizedName() + ".name"));
			}
			else {
			    currenttip.add("Mesh: None");
			}
		}
		
		if(accessor.getBlock() instanceof BlockInfestedLeaves)
		{
		    TileInfestedLeaves tile = (TileInfestedLeaves) accessor.getTileEntity();
		    
		    if (tile != null) {
				if(tile.getProgress() >= 1.0F)
				{
					currenttip.add("Progress: Done");
				}
				else
				{
					currenttip.add("Progress: " + Math.round(100 * tile.getProgress()) + "%");
				}
			}
		}
		
		if(accessor.getBlock() instanceof BlockCrucible)
		{
		    TileCrucible tile = (TileCrucible) accessor.getTileEntity();
		    
		    if (tile != null) {
				ItemStack solid = tile.getCurrentItem() == null ? null : tile.getCurrentItem().getItemStack();
				FluidStack liquid = tile.getTank().getFluid();

				String solidName = solid == null ? "None" : solid.getDisplayName();
				String liquidName = liquid == null ? "None" : liquid.getLocalizedName();

				int solidAmount = Math.max(0, tile.getSolidAmount());

				ItemStack toMelt = tile.getItemHandler().getStackInSlot(0);

				if(!toMelt.isEmpty())
				{
					solidAmount += CrucibleRegistry.getMeltable(toMelt).getAmount() * toMelt.getCount();
				}

				currenttip.add(String.format("Solid (%s): %d", solidName, solidAmount));
				currenttip.add(String.format("Liquid (%s): %d", liquidName, tile.getTank().getFluidAmount()));
				currenttip.add("Rate: " + tile.getHeatRate() + "x");
			}
		}
		
		return currenttip;
	}

	@Override
	@Nonnull
	public List<String> getWailaTail(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	@Nonnull
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te,
			NBTTagCompound tag, World world, BlockPos pos) {
		return tag;
	}

}
