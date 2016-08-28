package exnihiloadscensio.items.ore;

import java.util.List;
import java.util.Locale;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.registries.OreRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOre extends Item {

	public ItemOre() {
		super();

		setUnlocalizedName(ExNihiloAdscensio.MODID + ".ore");
		setRegistryName("itemOre");
		setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		setHasSubtypes(true);
		GameRegistry.<Item>register(this);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		Ore ore = getOre(stack);
		return ore == null ? super.getUnlocalizedName(stack) : super.getUnlocalizedName(stack) + "." + ore.getRegistryName().getResourcePath().toLowerCase(Locale.ENGLISH);
	}

	public Ore getOre(ItemStack stack) {
		return OreRegistry.ORES.getObjectById(stack.getItemDamage());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (Ore ore : OreRegistry.ORES.getValues())
			subItems.add(getStack(ore));
	}

	public static ItemStack getStack(Ore ore, int amount) {
		return ExNihiloAdscensio.proxy.getFoodRegistryWrapper().getStack(ore.getRegistryName(), amount);
	}

	public static ItemStack getStack(Ore food) {
		return getStack(food, 1);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()	{
		for (Ore ore : OreRegistry.ORES.getValues()) {
			String variant = "type=";
			if (ore.getRegistryName().toString().contains("hunk"))
				variant += "hunk";
			else if (ore.getRegistryName().toString().contains("ingot"))
				variant += "ingot";
			else
				variant += "piece";
			
			ModelLoader.setCustomModelResourceLocation(this, OreRegistry.ORES.getId(ore), new ModelResourceLocation("exnihiloadscensio:itemOre", variant));
		}
	}

}
