package exnihiloadscensio.items.seeds;

import exnihiloadscensio.ExNihiloAdscensio;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

public class ItemSeedBase extends Item implements IPlantable {

	private IBlockState plant;
	private EnumPlantType type;
	private String name;
	
	public ItemSeedBase(String name, IBlockState plant) {
		super();
		this.setRegistryName("itemSeed" + StringUtils.capitalize(name));
		this.setUnlocalizedName("itemSeed" + StringUtils.capitalize(name));
		this.plant = plant;
		this.name = name;
		type = EnumPlantType.Plains;
		
		GameRegistry.<Item>register(this);
	}
	
	@Override
	public CreativeTabs getCreativeTab()
	{
	    return ExNihiloAdscensio.tabExNihilo;
	}
	
	public ItemSeedBase setPlantType(EnumPlantType type) {
		this.type = type;
		return this;
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return type;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return plant;
	}
	
	@Override @Nonnull
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!facing.equals(EnumFacing.UP))
        	return EnumActionResult.PASS;

        ItemStack stack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.add(0, 1, 0), facing, stack)) {
            IBlockState soil = world.getBlockState(pos);

            if (soil.getBlock().canSustainPlant(soil, world, pos, EnumFacing.UP, this)
            		&& world.isAirBlock(pos.add(0, 1, 0)) 
            		&& this.getPlant(world, pos) != null)
            {
                world.setBlockState(pos.add(0, 1, 0), this.getPlant(world, pos));
                stack.shrink(1);

                return EnumActionResult.SUCCESS;
            }
        }
        
        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihiloadscensio:itemSeed", "type="+name));
	}

}
