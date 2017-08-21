package exnihiloadscensio.items.tools;


import com.google.common.collect.Sets;
import exnihiloadscensio.registries.HammerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class HammerBase extends ItemTool implements IHammer  {

	int miningLevel;

	public HammerBase(String name, int maxUses, ToolMaterial material)
	{
		super(material, Sets.newHashSet(new Block[]{}));
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		ForgeRegistries.ITEMS.register(this);
		this.setMaxDamage(maxUses);
		this.miningLevel = material.getHarvestLevel();
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

	public float getStrVsBlock(@Nonnull ItemStack stack, IBlockState state)
	{
		return HammerRegistry.registered(state.getBlock()) ? this.efficiencyOnProperMaterial : 1.0F;
	}
	
	@Override
	 public boolean canHarvestBlock(IBlockState state)
    {
        return HammerRegistry.registered(state.getBlock());
    }


	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		if (getRegistryName() != null)
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));

	}

}
