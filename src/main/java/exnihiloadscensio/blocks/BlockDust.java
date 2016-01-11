package exnihiloadscensio.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDust extends Block {
	
	public BlockDust()
	{
		super(Material.sand);
		setUnlocalizedName("blockDust");
		setRegistryName("blockDust");
		GameRegistry.registerBlock(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
