package exnihiloadscensio.blocks;

import java.util.ArrayList;
import java.util.Map;

import exnihiloadscensio.config.Config;
import exnihiloadscensio.items.ItemMesh;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.tiles.TileSieve;
import exnihiloadscensio.util.Util;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class BlockSieve extends BlockBase implements ITileEntityProvider, IProbeInfoAccessor {

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
		
		// I think this should work. Let's just go with it.
		if(player instanceof FakePlayer && !Config.fakePlayersCanSieve)
		    return false;
		
		TileSieve te = (TileSieve) world.getTileEntity(pos);
		if (te != null) {
			if (heldItem != null && heldItem.getItem() instanceof ItemMesh) {
				//Adding a mesh.
				ItemStack meshStack = heldItem.copy(); meshStack.stackSize = 1;
				MeshType type = MeshType.getMeshTypeByID(heldItem.getItemDamage());
				boolean done = te.setMesh(meshStack, false);
				
				if (done) {
				    if(!player.isCreative())
				        heldItem.stackSize--;
				    
					world.setBlockState(pos, state.withProperty(MESH, type));
					PacketHandler.sendNBTUpdate(te);			
					return true;
				}
			}
			if (player.getHeldItemMainhand() == null && te.getMeshStack() != null && player.isSneaking() && te.setMesh(null, true)) {
				//Removing a mesh.
				Util.dropItemInWorld(te, player, te.getMeshStack(), 0.02f);
				te.setMesh(null);
				PacketHandler.sendNBTUpdate(te);
				world.setBlockState(pos, state.withProperty(MESH, MeshType.NONE));
				
				return true;
			}
			
			if (te.addBlock(heldItem)) {
			    if(!player.isCreative())
                    heldItem.stackSize--;
                
				for (int xOffset = -1*Config.sieveSimilarRadius ; xOffset <= 1*Config.sieveSimilarRadius ; xOffset++) {
					for (int zOffset = -1*Config.sieveSimilarRadius ; zOffset <= 1*Config.sieveSimilarRadius ; zOffset++) {
						TileEntity entity = world.getTileEntity(pos.add(xOffset, 0, zOffset));
						if (entity != null && entity instanceof TileSieve) {
							TileSieve sieve = (TileSieve) entity;
							if (heldItem.stackSize > 0 && te.isSieveSimilarToInput(sieve)) 
                                if (sieve.addBlock(heldItem) && !player.isCreative())
                                    heldItem.stackSize--;
						}
					}
				}
				return true;
			}
			
			ArrayList<BlockPos> toSift = new ArrayList<BlockPos>();
			for (int xOffset = -1*Config.sieveSimilarRadius ; xOffset <= Config.sieveSimilarRadius ; xOffset++) {
				for (int zOffset = -1*Config.sieveSimilarRadius ; zOffset <= 1*Config.sieveSimilarRadius ; zOffset++) {
					TileEntity entity = world.getTileEntity(pos.add(xOffset, 0, zOffset));
					if (entity != null && entity instanceof TileSieve) {
						TileSieve sieve = (TileSieve) entity;
						if (te.isSieveSimilar(sieve))
							toSift.add(pos.add(xOffset, 0, zOffset));
					}
				}
			}
			for (BlockPos posIter : toSift) {
				if (posIter != null) {
					TileSieve sieve = (TileSieve) world.getTileEntity(posIter);
					sieve.doSieving(player);
				}
			}
			return true;
		}

		return true;
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
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null) {
			TileSieve sieve = (TileSieve) te;
			if (sieve.getMeshStack() != null)
				Util.dropItemInWorld(sieve, null, sieve.getMeshStack(), 0.02f);
		}
		
		super.breakBlock(world, pos, state);
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

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
			IBlockState blockState, IProbeHitData data) {
		
		TileSieve sieve = (TileSieve) world.getTileEntity(data.getPos());
		if (sieve == null)
			return;
		
		if (sieve.getMeshStack() == null) {
			probeInfo.text("Mesh: None");
			return;
		}
		probeInfo.text("Mesh: " + I18n.format(sieve.getMeshStack().getUnlocalizedName() + ".name"));
		
		if (mode == ProbeMode.EXTENDED) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(sieve.getMeshStack());
			for (Enchantment enchantment : enchantments.keySet()) {
				probeInfo.text(TextFormatting.BLUE + enchantment.getTranslatedName(enchantments.get(enchantment)));
			}
		}
		
	}

}
