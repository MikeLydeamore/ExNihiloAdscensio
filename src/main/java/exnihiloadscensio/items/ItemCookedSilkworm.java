package exnihiloadscensio.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCookedSilkworm extends ItemFood {

	public ItemCookedSilkworm() {
		super(2, 0.6f, false);
		this.setUnlocalizedName("silkwormcooked");
		this.setRegistryName("silkwormcooked");
		ForgeRegistries.ITEMS.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihiloadscensio:itemcookedsilkworm", "type=silkwormcooked"));
	}

}
