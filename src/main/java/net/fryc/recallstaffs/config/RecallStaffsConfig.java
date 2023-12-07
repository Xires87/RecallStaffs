package net.fryc.recallstaffs.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "recallstaffs")
public class RecallStaffsConfig implements ConfigData {

    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    @Comment("Players level required to craft recall staffs (wooden and netherite staffs don't require level to craft)")
    public int recallStaffCraftCost = 10;
    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.RequiresRestart
    public boolean resetStaffCooldownAfterDeath = false;

    @ConfigEntry.Category("other")
    public boolean recallingSummonsLightningBolt = true;

    @ConfigEntry.Category("other")
    @Comment("This option is client sided")
    public boolean enableTooltipsForRecallStaffs = true;

    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("This option is client sided")
    public boolean enableCraftingRequirementsTooltipForRecallStaffs = true;


    @ConfigEntry.Category("cooldown")
    public int woodenRecallStaffCooldown = 1400;

    @ConfigEntry.Category("cooldown")
    public int copperRecallStaffCooldown = 900;

    @ConfigEntry.Category("cooldown")
    public int ironRecallStaffCooldown = 720;

    @ConfigEntry.Category("cooldown")
    public int goldenRecallStaffCooldown = 590;

    @ConfigEntry.Category("cooldown")
    public int diamondRecallStaffCooldown = 410;

    @ConfigEntry.Category("cooldown")
    public int netheriteRecallStaffCooldown = 230;



    @ConfigEntry.Category("recallcost")
    @ConfigEntry.Gui.Tooltip
    @Comment("When true, players won't be able to recall with level lower than staff's usage cost")
    public boolean checkPlayersLevelBeforeRecall = false;
    @ConfigEntry.Category("recallcost")
    public int woodenRecallCost = 10;

    @ConfigEntry.Category("recallcost")
    public int copperRecallCost = 2;

    @ConfigEntry.Category("recallcost")
    public int ironRecallCost = 2;

    @ConfigEntry.Category("recallcost")
    public int goldenRecallCost = 1;

    @ConfigEntry.Category("recallcost")
    public int diamondRecallCost = 1;

    @ConfigEntry.Category("recallcost")
    public int netheriteRecallCost = 0;

    @ConfigEntry.Category("recallcost")
    public int recallingWithHorseAdditionalCost = 1;

    @ConfigEntry.Category("recoveryaltar")
    @ConfigEntry.Gui.Tooltip
    @Comment("20 = 1s. If this value is lower than 40, player won't get Call of Being and won't teleport back to recovery altar")
    public int invulnerabilityTimeAfterUsingRecoveryAltar = 600;

    @ConfigEntry.Category("recoveryaltar")
    @Comment("Number of levels required for last charges (without using Echo Shards)")
    public int recoveryAltarChargingLevelCost = 10;

    @ConfigEntry.Category("recoveryaltar")
    @ConfigEntry.Gui.Tooltip
    @Comment("When set to 0, recovery altar will be charged only with echo shards; when set to 6 - only with player levels")
    public int recoveryAltarLevelChargesCount = 2;

}
