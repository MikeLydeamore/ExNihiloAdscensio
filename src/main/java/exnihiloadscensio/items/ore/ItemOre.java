package exnihiloadscensio.items.ore;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import exnihiloadscensio.ExNihiloAdscensio;
import lombok.Getter;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemOre extends Item {

	@Getter
	private boolean registerIngot;
	
	@Getter
	private Ore ore;
	
	public ItemOre(Ore ore) {
		super();

		this.ore = ore;
		registerIngot = ore.getResult() == null;
		setUnlocalizedName(ExNihiloAdscensio.MODID + ".ore."+ore.getName());
		setRegistryName("itemOre"+StringUtils.capitalize(ore.getName()));
		setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		setHasSubtypes(true);
		GameRegistry.<Item>register(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(new ItemStack(this, 1, 0)); //Piece
		subItems.add(new ItemStack(this, 1, 1)); //Chunk
		subItems.add(new ItemStack(this, 1, 2)); //Dust
		if (registerIngot)
			subItems.add(new ItemStack(this, 1, 3)); //Ingot
	}

	@SideOnly(Side.CLIENT)
	public void initModel()	{

		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihiloadscensio:itemOre", "type=piece"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation("exnihiloadscensio:itemOre", "type=hunk"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation("exnihiloadscensio:itemOre", "type=dust"));
		if (registerIngot)
			ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation("exnihiloadscensio:itemOre", "type=ingot"));
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = ore.getName();
		String pre = "";
		switch (stack.getItemDamage()) {
		case 0:
			pre = "orepiece";
			break;
		case 1:
			pre = "orehunk";
			break;
		case 2:
			pre = "oredust";
			break;
		case 3:
			pre = "oreingot";
			break;
		}
		return (StringUtils.capitalize(name) + " " +I18n.translateToLocal(pre+".name")).trim();
	}

}
