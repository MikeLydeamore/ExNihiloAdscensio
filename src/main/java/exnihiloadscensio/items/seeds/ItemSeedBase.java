package exnihiloadscensio.items.seeds;

import com.sun.xml.internal.ws.util.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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

public class ItemSeedBase extends Item implements IPlantable {

	private IBlockState plant;
	private IBlockState soil;
	private EnumPlantType type;
	private String name;
	
	public ItemSeedBase(String name, IBlockState plant, IBlockState soil) {
		super();
		this.setRegistryName("itemSeed" + StringUtils.capitalize(name));
		this.setUnlocalizedName("itemSeed" + StringUtils.capitalize(name));
		this.plant = plant;
		this.soil = soil;
		this.name = name;
		type = EnumPlantType.Plains;
		
		GameRegistry.<Item>register(this);
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
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!facing.equals(EnumFacing.UP))
        	return EnumActionResult.PASS;
        	
        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.add(0, 1, 0), facing, stack)) {
            IBlockState soil = world.getBlockState(pos);
            Block soilBlock = soil.getBlock();

            if (soil != null && soilBlock.canSustainPlant(soil, world, pos, EnumFacing.UP, this) 
            		&& world.isAirBlock(pos.add(0, 1, 0)) 
            		&& this.getPlant(world, pos) != null)
            {
                world.setBlockState(pos.add(0, 1, 0), this.getPlant(world, pos));
                --stack.stackSize;
                return EnumActionResult.SUCCESS;
            }
        }
        
        return EnumActionResult.PASS;
    }
	
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("exnihiloadscensio:itemSeed", "type="+name));
	}

}
