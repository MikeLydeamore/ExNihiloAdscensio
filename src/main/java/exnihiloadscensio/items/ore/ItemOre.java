package exnihiloadscensio.items.ore;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.registries.OreRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
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

	public static ItemStack getStack(Ore ore) {
		return getStack(ore, 1);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()	{
		for (Ore ore : OreRegistry.ORES.getValues()) {
			String variant = "type=";
			if (ore.getRegistryName().toString().contains("hunk"))
				variant += "hunk";
			else if (ore.getRegistryName().toString().contains("ingot"))
				variant += "ingot";
			else if (ore.getRegistryName().toString().contains("dust"))
				variant += "dust";
			else
				variant += "piece";

			ModelLoader.setCustomModelResourceLocation(this, OreRegistry.ORES.getId(ore), new ModelResourceLocation("exnihiloadscensio:itemOre", variant));
		}
	}

	@SideOnly(Side.CLIENT)
	public void fixModel() {
		for (Ore ore : OreRegistry.ORES.getValues()) {
			String variant = "type=";
			if (ore.getRegistryName().toString().contains("hunk"))
				variant += "hunk";
			else if (ore.getRegistryName().toString().contains("ingot"))
				variant += "ingot";
			else if (ore.getRegistryName().toString().contains("dust"))
				variant += "dust";
			else
				variant += "piece";
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, OreRegistry.ORES.getId(ore), new ModelResourceLocation("exnihiloadscensio:itemOre", variant));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Ore ore = OreRegistry.ORES.getObjectById(stack.getItemDamage());
		String pre = "ore";
		String name = "";
		if (ore == null || ore.getRegistryName() == null)
			return super.getItemStackDisplayName(stack);

		if (ore.getRegistryName().toString().contains("hunk")) {
			pre += "hunk"; 
			name = ore.getRegistryName().toString().replace("hunk", "");
		}
		else if (ore.getRegistryName().toString().contains("ingot")) {
			pre += "ingot";
			name = ore.getRegistryName().toString().replace("ingot", "");
		}
		else if (ore.getRegistryName().toString().contains("dust")) {
			pre += "dust";
			name = ore.getRegistryName().toString().replace("dust", "");
		}
		else {
			pre += "piece";
			name = ore.getRegistryName().toString();
		}
		name = name.substring(name.indexOf(":")+1, name.length());
		return (StringUtils.capitalize(name) + " " +I18n.translateToLocal(pre+".name")).trim();
	}

}
