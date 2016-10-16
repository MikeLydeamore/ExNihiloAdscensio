package exnihiloadscensio.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block {
	
	public BlockBase(Material mat, String name)
	{
		super(mat);
		setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.<Block>register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(name));
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "normal"));
	}

}
