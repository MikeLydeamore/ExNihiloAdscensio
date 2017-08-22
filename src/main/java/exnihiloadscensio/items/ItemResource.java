package exnihiloadscensio.items;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.BlockInfestedLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ItemResource extends Item {

	public static final String PORCELAIN_CLAY = "porcelain_clay";
	public static final String SILKWORM = "silkworm";
	public static final String ANCIENT_SPORES = "ancient_spores";
	public static final String GRASS_SEEDS = "grass_seeds";
	public static final String DOLL_BASE = "doll";
	
	private static ArrayList<String> names = new ArrayList<String>();

	public ItemResource() {
		super();

		setUnlocalizedName("itemmaterial");
		setRegistryName("itemmaterial");
		setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		setHasSubtypes(true);
		ForgeRegistries.ITEMS.register(this);

		names.add(0, "removed");
		names.add(1, PORCELAIN_CLAY);
		names.add(2, SILKWORM);
		names.add(3, ANCIENT_SPORES);
		names.add(4, GRASS_SEEDS);
		names.add(5, DOLL_BASE);
	}

	@Override
	@Nonnull
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + names.get(stack.getItemDamage());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(@Nonnull CreativeTabs tabs, @Nonnull NonNullList<ItemStack> list) {
		for (int i = 1; i < names.size(); i++)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	@Nonnull
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.getItemDamage() == names.indexOf(SILKWORM)) {
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() != Blocks.AIR && (state.getBlock() == Blocks.LEAVES || state.getBlock() == Blocks.LEAVES2)) {
			    BlockInfestedLeaves.infestLeafBlock(world, pos);
				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}
		if (stack.getItemDamage() == names.indexOf(ANCIENT_SPORES) || stack.getItemDamage() == names.indexOf(GRASS_SEEDS)) {
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() != Blocks.AIR && state.getBlock() == Blocks.DIRT) {
				IBlockState transformTo = stack.getItemDamage() == names.indexOf(ANCIENT_SPORES) ? Blocks.MYCELIUM.getDefaultState() : Blocks.GRASS.getDefaultState();
				world.setBlockState(pos, transformTo);
				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}
		
		return EnumActionResult.PASS;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()	{
		for (int i = 0 ; i < names.size() ; i ++) {
			String variant = "type="+names.get(i);
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("exnihiloadscensio:itemmaterial", variant));
		}
	}

	public static ItemStack getResourceStack(String name) {
		return getResourceStack(name, 1);
	}
	
	public static ItemStack getResourceStack(String name, int quantity) {
		return new ItemStack(ENItems.resources, quantity, names.indexOf(name));
	}


}
