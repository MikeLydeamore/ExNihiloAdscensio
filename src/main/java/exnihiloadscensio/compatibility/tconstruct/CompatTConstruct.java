package exnihiloadscensio.compatibility.tconstruct;

import exnihiloadscensio.items.ENItems;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class CompatTConstruct
{
    public static void postInit()
    {
        Modifier smashingModifier = new ModifierSmashing();
        
        TinkerRegistry.registerModifier(smashingModifier);
        smashingModifier.addItem(ENItems.hammerDiamond);
    }
}
