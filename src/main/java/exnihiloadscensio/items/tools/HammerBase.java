package exnihiloadscensio.items.tools;


import com.google.common.collect.Sets;

import exnihiloadscensio.registries.HammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HammerBase extends ItemTool implements IHammer  {

	int miningLevel;

	public HammerBase(String name, int maxUses, ToolMaterial material)
	{
		super(3.0f, material, Sets.newHashSet(new Block[]{}));
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		GameRegistry.registerItem(this);
		this.setMaxDamage(maxUses);
		this.miningLevel = material.getHarvestLevel();
		System.out.println("Adding Hammer "+name+" with harvest level "+miningLevel);

	}

	@Override
	public boolean isHammer(ItemStack stack) 
	{
		return true;
	}

	@Override
	public int getMiningLevel(ItemStack stack) 
	{
		return miningLevel;
	}

	public float func_150893_a(ItemStack stack, Block block)
	{
		return HammerRegistry.registered(block) ? this.efficiencyOnProperMaterial : 1.0F;
	}

	@Override
	public boolean canHarvestBlock(Block block)
	{
		if (!HammerRegistry.registered(block))
			return false;

		return block.getHarvestLevel(block.getDefaultState()) <= this.miningLevel;
	}

	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));

	}

}
