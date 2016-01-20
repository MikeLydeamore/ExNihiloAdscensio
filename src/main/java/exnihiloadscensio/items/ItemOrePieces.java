package exnihiloadscensio.items;

import java.util.List;

import lombok.Getter;
import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.texturing.TextureDynamic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOrePieces extends Item {
	
	private String name;
	private Color color;
	@Getter
	private TextureDynamic[] texture = new TextureDynamic[3];
	
	private int brokenIndex = 0, crushedIndex = 1, dustIndex = 2;

	public ItemOrePieces(String name_, String color)
	{
		super();
		this.name = name_;
		this.color = new Color(color);
		this.setUnlocalizedName("orePieces"+name);
		this.setRegistryName("orePieces"+name);
		this.hasSubtypes = true;

		GameRegistry.registerItem(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (int i = 0; i < 3; i++)
			subItems.add(new ItemStack(itemIn, 1, i));
	}

	public ItemOrePieces generateTexture()
	{
		ResourceLocation baseTexture = new ResourceLocation("exnihiloadscensio", "textures/items/ItemBrokenBase.png");
		ResourceLocation templateTexture = new ResourceLocation("exnihiloadscensio", "textures/items/ItemBrokenTemplate.png");
		
		if (!ExNihiloAdscensio.proxy.runningOnServer())
		{
			texture[brokenIndex] = new TextureDynamic(name, baseTexture, templateTexture, color);
			//attachTexture(broken, texture_name, baseTexture, templateTexture, color);
		}
		
		return this;
	}

}
