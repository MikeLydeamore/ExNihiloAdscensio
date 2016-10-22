package exnihiloadscensio.enchantments;

import exnihiloadscensio.items.ENItems;
import net.minecraft.enchantment.EnchantmentDigging;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentEfficiency extends EnchantmentDigging
{
    protected EnchantmentEfficiency()
    {
        super(Rarity.COMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem().equals(ENItems.mesh) ? true : super.canApplyAtEnchantingTable(stack);
    }
}
