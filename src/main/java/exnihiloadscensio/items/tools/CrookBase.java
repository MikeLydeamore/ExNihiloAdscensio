package exnihiloadscensio.items.tools;

import com.google.common.collect.Sets;

import exnihiloadscensio.registries.CrookRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CrookBase extends ItemTool implements ICrook {

	int miningLevel;

	public CrookBase(String name, int maxUses)
	{
		super(0.0f, ToolMaterial.WOOD, Sets.newHashSet(new Block[]{}));
		
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		GameRegistry.registerItem(this);
		this.setMaxDamage(maxUses);

	}

	@Override
	public boolean isCrook(ItemStack stack) 
	{
		return true;
	}

	public float func_150893_a(ItemStack stack, Block block)
	{
		return CrookRegistry.registered(block) ? this.efficiencyOnProperMaterial : 1.0F;
	}

	@Override
	public boolean canHarvestBlock(Block block)
	{
		if (!CrookRegistry.registered(block))
			return false;

		return true;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));

	}

}
