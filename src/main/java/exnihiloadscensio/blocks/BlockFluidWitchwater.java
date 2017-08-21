package exnihiloadscensio.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockFluidWitchwater extends BlockFluidClassic {
	
	public BlockFluidWitchwater() {
		super(ENBlocks.fluidWitchwater, Material.WATER);
		
		this.setRegistryName("witchwater");
		this.setUnlocalizedName("witchwater");
		ForgeRegistries.BLOCKS.register(this);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (world.isRemote)
			return;

		if (entity.isDead)
			return;

		if (entity instanceof EntitySkeleton) {

			EntitySkeleton skeleton = (EntitySkeleton) entity;
			skeleton.setDead();

			EntityWitherSkeleton witherSkeleton = new EntityWitherSkeleton(world);
			witherSkeleton.setLocationAndAngles(skeleton.posX, skeleton.posY, skeleton.posZ, skeleton.rotationYaw, skeleton.rotationPitch);
			witherSkeleton.renderYawOffset = skeleton.renderYawOffset;
			witherSkeleton.setHealth(witherSkeleton.getMaxHealth());

			world.spawnEntity(witherSkeleton);
		}

		if (entity instanceof EntityCreeper) {
			EntityCreeper creeper = (EntityCreeper) entity;
			if (!creeper.getPowered()) {
				creeper.onStruckByLightning(null);
				creeper.setHealth(creeper.getMaxHealth());

				return;
			}
		}

		if (entity instanceof EntitySpider && !(entity instanceof EntityCaveSpider)) {
			EntitySpider spider = (EntitySpider) entity;
			spider.setDead();

			EntityCaveSpider caveSpider = new EntityCaveSpider(world);
			caveSpider.setLocationAndAngles(spider.posX, spider.posY, spider.posZ, spider.rotationYaw, spider.rotationPitch);
			caveSpider.renderYawOffset = spider.renderYawOffset;
			caveSpider.setHealth(caveSpider.getMaxHealth());

			world.spawnEntity(caveSpider);

			return;
		}

		if (entity instanceof EntitySquid) {
			EntitySquid squid = (EntitySquid) entity;
			squid.setDead();

			EntityGhast ghast = new EntityGhast(world);
			ghast.setLocationAndAngles(squid.posX, squid.posY, squid.posZ, squid.rotationYaw, squid.rotationPitch);
			ghast.renderYawOffset = squid.renderYawOffset;
			ghast.setHealth(ghast.getMaxHealth());

			world.spawnEntity(ghast);

			return;
		}

		if (entity instanceof EntityAnimal) {
			entity.onStruckByLightning(null);

			return;
		}

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 210, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 210, 2));
			player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 210, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 210, 0));
		}
	}

}
