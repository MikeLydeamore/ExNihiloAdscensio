package exnihiloadscensio.barrel;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBarrelMode {
	
	public void writeToNBT(NBTTagCompound tag);
	
	public void readFromNBT(NBTTagCompound tag);
	
	public boolean isTriggerItemStack(ItemStack stack);
	
	public boolean isTriggerFluidStack(FluidStack stack);
	
	public String getName();
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTextureForRender();
	
	public float getFilledLevelForRender();

}
