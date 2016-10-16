package exnihiloadscensio.entities;

import exnihiloadscensio.items.ENItems;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileStone extends EntityThrowable
{
    private ItemStack stack;
    
    public ProjectileStone(World worldIn)
    {
        super(worldIn);
    }
    
    public ProjectileStone(World worldIn, EntityLivingBase thrower)
    {
        super(worldIn, thrower);
    }
    
    public ProjectileStone(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }
    
    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), (int) (Math.random() * (4.0F / 3.0F)));
        }
        else if (!worldObj.isRemote)
        {
            setDead();
            
            if(stack != null)
            {
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, stack));
            }
        }
        
        for (int j = 0; j < 8; ++j)
        {
            worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX, posY, posZ, 0.0D, 0.0D, 0.0D, new int[] { Block.getStateId(Blocks.STONE.getDefaultState()) });
        }
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        
        NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        
        tag.setTag("pebbleStack", stackTag);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);
        
        if(tag.hasKey("pebbleStack"))
        {
            stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) tag.getTag("pebbleStack"));
        }
        else
        {
            stack = new ItemStack(ENItems.pebbles);
        }
    }
    
    public synchronized ItemStack getStack()
    {
        return stack;
    }
    
    public synchronized void setStack(ItemStack stack)
    {
        this.stack = stack;
    }
}
