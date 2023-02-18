package net.fryc.recallstaffs.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "recallstaffs")
public class RecallStaffsConfig implements ConfigData {

    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart
    public int recallStaffCraftCost = 10;
    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.RequiresRestart
    public boolean resetStaffCooldownAfterDeath = false;

    @ConfigEntry.Category("other")
    public boolean recallingSummonsLightningBolt = true;

    @ConfigEntry.Category("other")
    public boolean enableTooltipsForRecallStaffs = true;

    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.RequiresRestart
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


}
