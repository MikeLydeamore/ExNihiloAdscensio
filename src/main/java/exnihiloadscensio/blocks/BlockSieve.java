package exnihiloadscensio.blocks;

import java.util.List;

import exnihiloadscensio.items.ItemMesh;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.tiles.TileSieve;
import exnihiloadscensio.util.Util;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSieve extends BlockBase implements ITileEntityProvider {

	public enum MeshType implements IStringSerializable {
		NONE(0, "none"), STRING(1, "string"), FLINT(2, "flint"), IRON(3, "iron"), DIAMOND(4, "diamond");

		private int id;
		private String name;

		private MeshType(int id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		public int getID() {
			return id;
		}

		@Override
		public String toString() {
			return getName();
		}

		public static MeshType getMeshTypeByID(int meta) {
			switch (meta) {
			case 1:
				return STRING;
			case 2:
				return FLINT;
			case 3:
				return IRON;
			case 4:
				return DIAMOND;
			}

			return NONE;
		}
	}

	public static final PropertyEnum<MeshType> MESH = PropertyEnum.create("mesh", MeshType.class);

	public BlockSieve() {
		super(Material.WOOD, "blockSieve");
		this.setDefaultState(this.blockState.getBaseState().withProperty(MESH, MeshType.NONE));
		this.setHardness(2.0f);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;

		TileSieve te = (TileSieve) world.getTileEntity(pos);
		if (te != null) {
			if (heldItem != null && heldItem.getItem() instanceof ItemMesh) {
				//Adding a mesh.
				ItemStack meshStack = heldItem.copy(); meshStack.stackSize = 1;
				MeshType type = MeshType.getMeshTypeByID(heldItem.getItemDamage());
				boolean done = te.setMesh(meshStack, false);
				
				if (done) {
					heldItem.stackSize--;
					world.setBlockState(pos, state.withProperty(MESH, type));
					PacketHandler.sendNBTUpdate(te);			
					return true;
				}
			}
			if (heldItem == null && te.getMeshStack() != null && player.isSneaking() && te.setMesh(null, true)) {
				//Removing a mesh.
				Util.dropItemInWorld(te, player, te.getMeshStack(), 0.02f);
				te.setMesh(null);
				world.setBlockState(pos, state.withProperty(MESH, MeshType.NONE));
				
				return true;
			}
			
			if (te.addBlock(heldItem)) {
				heldItem.stackSize--;
				return true;
			}
			
			te.doSieving(player);
			return true;
		}

		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileSieve te = (TileSieve) world.getTileEntity(pos);
		if(te != null)
		{
			if(te.getMeshStack() != null)
			{
				EntityItem ei = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), te.getMeshStack());
				world.spawnEntityInWorld(ei);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {MESH});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		MeshType type;
		switch (meta) {
		case 0:
			type = MeshType.NONE;
			break;
		case 1:
			type = MeshType.STRING;
			break;
		case 2:
			type = MeshType.FLINT;
			break;
		case 3:
			type = MeshType.IRON;
			break;
		case 4:
			type = MeshType.DIAMOND;
			break;
		default:
			type = MeshType.STRING;
			break;
		}
		return getDefaultState().withProperty(MESH, type);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		MeshType type = (MeshType) state.getValue(MESH);
		return type.getID();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack>  ret = super.getDrops(world, pos, state, fortune); 
		
		TileSieve te = (TileSieve) world.getTileEntity(pos);
		if (te != null) {
			if (te.getMeshStack() != null) {
				ret.add(te.getMeshStack().copy());
			}
		}
		
		return ret;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSieve();
	}

	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

}
