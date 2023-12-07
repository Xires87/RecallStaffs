package net.fryc.recallstaffs.craftingmanipulator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fryc.craftingmanipulator.conditions.PressedKey;
import net.fryc.craftingmanipulator.rules.tooltips.TooltipRules;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.config.ConfigHelper;
import net.fryc.recallstaffs.tags.ModItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class ClientRules {

    public static TooltipRules tooltip = new TooltipRules(ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL, Text.translatable("text.craftingmanipulator.crafting_requirements").formatted(Formatting.YELLOW), PressedKey.SHIFT, Text.literal(ConfigHelper.recallStaffCraftCost + " level").formatted(Formatting.RED));

    public static void enableCraftingManipulatorClientRules(){
        tooltip.isEnabled = RecallStaffs.config.enableCraftingRequirementsTooltipForRecallStaffs;
    }

    public static void setRuleStatus(){
        if(RecallStaffs.config.enableCraftingRequirementsTooltipForRecallStaffs){
            tooltip.isEnabled = ConfigHelper.recallStaffCraftCost > 0;
        }
    }
}
