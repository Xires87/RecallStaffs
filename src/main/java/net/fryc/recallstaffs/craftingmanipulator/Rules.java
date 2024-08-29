package net.fryc.recallstaffs.craftingmanipulator;

import net.fryc.craftingmanipulator.registry.CMRegistries;
import net.fryc.craftingmanipulator.rules.oncraft.ExperienceOCR;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.tags.ModItemTags;

public class Rules {

    public static PlayerLevelWithTooltipRBR RECALL_STAFF_CRAFT_COST = (PlayerLevelWithTooltipRBR) CMRegistries.registerCraftingRule("recallstaffs_crafting_require_level", new PlayerLevelWithTooltipRBR(
            ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL, RecallStaffs.config.recallStaffCraftCost
    ));

    public static ExperienceOCR REMOVE_LEVEL = (ExperienceOCR) CMRegistries.registerCraftingRule("recallstaffs_crafting_consumes_level", new ExperienceOCR(
            ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL, -RecallStaffs.config.recallStaffCraftCost, false
    ));


    public static void enableCraftingManipulatorRules(){
        RECALL_STAFF_CRAFT_COST.enabled = RecallStaffs.config.recallStaffCraftCost > 0;
        REMOVE_LEVEL.enabled = RecallStaffs.config.recallStaffCraftCost > 0;
    }
}
