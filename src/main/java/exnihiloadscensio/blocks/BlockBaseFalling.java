package exnihiloadscensio.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBaseFalling extends BlockFalling
{
    public BlockBaseFalling(SoundType sound, String name)
    {
        super(Material.SAND);
        setUnlocalizedName(name);
        setRegistryName(name);
        setSoundType(sound);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(name));
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "normal"));
    }
}
