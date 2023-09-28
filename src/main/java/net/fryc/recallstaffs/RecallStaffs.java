package net.fryc.recallstaffs;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fryc.craftingmanipulator.rules.PlayerLevelRBR;
import net.fryc.craftingmanipulator.rules.oncraft.ExperienceOCR;
import net.fryc.recallstaffs.blocks.ModBlocks;
import net.fryc.recallstaffs.command.ResetStaffCooldownCommand;
import net.fryc.recallstaffs.config.RecallStaffsConfig;
import net.fryc.recallstaffs.effects.ModEffects;
import net.fryc.recallstaffs.event.CopyRecallStaffCooldown;
import net.fryc.recallstaffs.items.ModItems;
import net.fryc.recallstaffs.tags.ModItemTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecallStaffs implements ModInitializer {
	public static final String MOD_ID = "recallstaffs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static RecallStaffsConfig config;

	@Override
	public void onInitialize() {
		AutoConfig.register(RecallStaffsConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(RecallStaffsConfig.class).getConfig();

		ModItems.registerModItems();

		ModBlocks.registerModBlocks();
		ModEffects.registerEffects();

		if(!config.resetStaffCooldownAfterDeath){
			ServerPlayerEvents.COPY_FROM.register(new CopyRecallStaffCooldown());
		}

		if(config.recallStaffCraftCost > 0){
			String tooltip = config.recallStaffCraftCost + " level";
			if(!config.enableCraftingRequirementsTooltipForRecallStaffs) tooltip = "";
			PlayerLevelRBR RECALL_STAFF_CRAFT_COST = new PlayerLevelRBR(tooltip, ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL, config.recallStaffCraftCost);
			ExperienceOCR REMOVE_LEVEL = new ExperienceOCR("", ModItemTags.RECALL_STAFFS_REQUIRE_LEVEL, -config.recallStaffCraftCost, false);
		}

		CommandRegistrationCallback.EVENT.register(ResetStaffCooldownCommand::register);
	}
}
