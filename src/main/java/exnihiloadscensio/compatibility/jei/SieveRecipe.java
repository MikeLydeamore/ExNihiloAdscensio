package exnihiloadscensio.compatibility.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SieveRecipe implements IRecipeWrapper {

	private List<ItemStack> inputs = new ArrayList<ItemStack>();
	private List<ItemStack> outputs = new ArrayList<ItemStack>();
	
	private ArrayList<Siftable> drops;
	
	public SieveRecipe(IBlockState block, MeshType mesh) {
		drops = SieveRegistry.getDrops(new BlockInfo(block));
		inputs.add(new ItemStack(ENItems.mesh, 1, mesh.getID()));
		inputs.add(new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
		
		if (drops != null && drops.size() > 0) {
			for (Siftable siftable : drops) {
				if (siftable.getMeshLevel() != mesh.getID())
					continue;
				ItemInfo drop = siftable.getDrop();
				boolean found = false;
				for (ItemStack stack : outputs) {
					if (stack.getItem() == drop.getItem() && stack.getItemDamage() == drop.getMeta()) {
						stack.stackSize++;
						found=true;
					}
				}
				if (!found) {
					outputs.add(drop.getItemStack());
				}
				
			}
		}
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, outputs);
		
	}

	@Override
	public List getInputs() {
		return inputs;
	}

	@Override
	public List getOutputs() {
		return outputs;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return Collections.emptyList();
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
	}

	@Override
	public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}
}
