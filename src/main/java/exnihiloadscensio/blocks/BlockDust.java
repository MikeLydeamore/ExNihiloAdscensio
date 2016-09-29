package exnihiloadscensio.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDust extends BlockFalling {
	
	public BlockDust()
	{
		super(Material.SAND);
		this.setHardness(1.2f);		
		this.setUnlocalizedName("blockDust");
		this.setRegistryName("blockDust");
		this.setSoundType(SoundType.SAND);
		GameRegistry.<Block>register(this);
		GameRegistry.<Item>register(new ItemBlock(this).setRegistryName("blockDust"));
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
