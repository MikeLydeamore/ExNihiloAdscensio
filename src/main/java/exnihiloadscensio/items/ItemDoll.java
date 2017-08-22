package exnihiloadscensio.items;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.ENBlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ItemDoll extends Item {
	
	public static final String BLAZE = "blaze";
	public static final String ENDERMAN = "enderman";
	
	private static ArrayList<String> names = new ArrayList<String>();
	
	public ItemDoll() {
		
		super();
		setUnlocalizedName("itemdoll");
		setRegistryName("itemdoll");
		
		setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		setHasSubtypes(true);
		ForgeRegistries.ITEMS.register(this);
		
		names.add(BLAZE);
		names.add(ENDERMAN);
	}
	
	public Fluid getSpawnFluid(ItemStack stack) {
		return stack.getItemDamage() == 0 ? FluidRegistry.LAVA : (Fluid) ENBlocks.fluidWitchwater;
	}
	
	/**
	 * Spawns the mob in the world at position
	 * @param stack The Doll Stack
	 * @param pos Blockpos
	 * @return true if spawn is successful
	 */
	public boolean spawnMob(ItemStack stack, World world, BlockPos pos) {
		if (stack.getItemDamage() == 0) {
			EntityBlaze blaze = new EntityBlaze(world);
			blaze.setPosition(pos.getX(), pos.getY()+1, pos.getZ());

			return world.spawnEntity(blaze);
		}
		else {
			EntityEnderman enderman = new EntityEnderman(world);
			enderman.setPosition(pos.getX(), pos.getY()+2, pos.getZ());
			
			return world.spawnEntity(enderman);
		}
	}
	
	@Override
	@Nonnull
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + names.get(stack.getItemDamage());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
		for (int i = 0; i < names.size(); i++)
			list.add(new ItemStack(this, 1, i));
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()	{
		for (int i = 0 ; i < names.size() ; i ++) {
			String variant = "type="+names.get(i);
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("exnihiloadscensio:itemdoll", variant));
		}
	}

}
