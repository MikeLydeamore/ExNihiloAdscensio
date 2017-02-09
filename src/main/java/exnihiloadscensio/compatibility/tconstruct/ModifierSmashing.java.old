package exnihiloadscensio.compatibility.tconstruct;

import java.util.List;

import exnihiloadscensio.registries.HammerRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class ModifierSmashing extends ModifierTrait
{
    public ModifierSmashing()
    {
        super("exnihilo_smashing", 0xFF0000);
    }
    
    @Override
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return enchantment != Enchantments.SILK_TOUCH;
    }
    
    @Override
    public boolean canApplyTogether(IToolMod modifier)
    {
        return modifier != TinkerRegistry.getModifier("silktouch");
    }
    
    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag)
    {
        super.applyEffect(rootCompound, modifierTag);
        
        ToolNBT data = TagUtil.getToolStats(rootCompound);
        
        data.durability += 500;
        data.attack += 1.0F;
        
        if (data.harvestLevel < HarvestLevels.OBSIDIAN)
        {
            data.harvestLevel = HarvestLevels.OBSIDIAN;
        }
        
        TagUtil.setToolTag(rootCompound, data.get());
    }
    
    @Override
    public void blockHarvestDrops(ItemStack tool, BlockEvent.HarvestDropsEvent event)
    {
        List<ItemStack> rewards = HammerRegistry.getRewardDrops(event.getWorld().rand, event.getState(), ToolHelper.getHarvestLevelStat(tool), event.getFortuneLevel());
        
        if (rewards != null && rewards.size() > 0)
        {
            event.getDrops().clear();
            event.setDropChance(1.0F);
            event.getDrops().addAll(rewards);
        }
    }
}
