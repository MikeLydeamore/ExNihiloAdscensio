package exnihiloadscensio.items;

import com.google.common.collect.Lists;
import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.entities.ProjectileStone;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPebble extends Item
{
    private static List<String> names = Lists.newArrayList("stone", "granite", "diorite", "andesite");
    
    public ItemPebble()
    {
        setUnlocalizedName("itemPebble");
        setRegistryName("itemPebble");
        setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        setHasSubtypes(true);
        GameRegistry.register(this);
    }

    @Override @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + names.get(stack.getItemDamage());
    }
    
    @Override @SideOnly(Side.CLIENT)
    public void getSubItems(@Nullable Item item, CreativeTabs tabs, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < names.size(); i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        stack.shrink(1);
        world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        
        if (!world.isRemote)
        {
            ItemStack thrown = stack.copy();
            thrown.setCount(1);
            
            ProjectileStone projectile = new ProjectileStone(world, player);
            projectile.setStack(thrown);
            projectile.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 0.5F);
            world.spawnEntity(projectile);
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel()
    {
        for (int i = 0; i < names.size(); i++)
        {
            String variant = "type=" + names.get(i);
            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("exnihiloadscensio:itemPebble", variant));
        }
    }
    
    public static ItemStack getPebbleStack(String name)
    {
        return new ItemStack(ENItems.pebbles, 1, names.indexOf(name));
    }
}
