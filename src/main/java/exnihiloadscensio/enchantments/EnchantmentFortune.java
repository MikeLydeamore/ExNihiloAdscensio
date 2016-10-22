package exnihiloadscensio.enchantments;

import exnihiloadscensio.items.ENItems;
import net.minecraft.enchantment.EnchantmentLootBonus;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentFortune extends EnchantmentLootBonus
{
    protected EnchantmentFortune()
    {
        super(Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
    }
    
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem().equals(ENItems.mesh) ? true : super.canApplyAtEnchantingTable(stack);
    }
}
