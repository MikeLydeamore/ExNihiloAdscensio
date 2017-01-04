package exnihiloadscensio.items.tools;

import com.google.common.collect.Sets;

import exnihiloadscensio.registries.CrookRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CrookBase extends ItemTool implements ICrook {

	int miningLevel;

	public CrookBase(String name, int maxUses)
	{
		super(ToolMaterial.WOOD, Sets.newHashSet(new Block[]{}));
		
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		GameRegistry.<Item>register(this);
		this.setMaxDamage(maxUses);

	}

	@Override
	public boolean isCrook(ItemStack stack) 
	{
		return true;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return CrookRegistry.registered(state.getBlock()) ? this.efficiencyOnProperMaterial : 1.0F;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));

	}

}
