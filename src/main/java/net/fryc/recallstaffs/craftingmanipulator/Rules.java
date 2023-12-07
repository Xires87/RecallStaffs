package net.fryc.recallstaffs.craftingmanipulator;

import net.fryc.craftingmanipulator.rules.oncraft.ExperienceOCR;
import net.fryc.craftingmanipulator.rules.recipeblocking.PlayerLevelRBR;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.tags.ModItemTags;

public class Rules {

    public static PlayerLevelRBR RECALL_STAFF_CRAFT_COST = new PlayerLevelRBR(ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL, RecallStaffs.config.recallStaffCraftCost);
    public static ExperienceOCR REMOVE_LEVEL = new ExperienceOCR(ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL, -RecallStaffs.config.recallStaffCraftCost, false);

    public static void enableCraftingManipulatorRules(){
        RECALL_STAFF_CRAFT_COST.isEnabled = RecallStaffs.config.recallStaffCraftCost > 0;
        REMOVE_LEVEL.isEnabled = RecallStaffs.config.recallStaffCraftCost > 0;
    }
}
