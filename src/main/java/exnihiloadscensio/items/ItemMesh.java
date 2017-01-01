package exnihiloadscensio.items;

import java.util.List;

import exnihiloadscensio.blocks.BlockSieve.MeshType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMesh extends Item {
	
	public ItemMesh() {
		super();
		this.setHasSubtypes(true);
		this.setUnlocalizedName("itemMesh");
		this.setRegistryName("itemMesh");
		this.setMaxStackSize(1);
		GameRegistry.<Item>register(this);
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack)
	{
	    switch(stack.getMetadata())
	    {
	        case 1:
	            return 15;
	        case 2:
	            return 7;
	        case 3:
	            return 14;
	        case 4:
	            return 10;
	        default:
	            return 0;
	    }
	}
	
	@Override
	public boolean isItemTool(ItemStack stack)
	{
	    return true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
	    return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
    {
        for (int i = 1 ; i < MeshType.values().length ; i++) { //0 is the "none" case.
        	subItems.add(new ItemStack(item, 1, i));
        }
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation("exnihiloadscensio:itemMeshString"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation("exnihiloadscensio:itemMeshFlint"));
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation("exnihiloadscensio:itemMeshIron"));
		ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation("exnihiloadscensio:itemMeshDiamond"));
	}

}
