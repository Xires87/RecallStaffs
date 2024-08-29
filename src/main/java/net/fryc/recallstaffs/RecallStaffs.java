package net.fryc.recallstaffs;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fryc.recallstaffs.blocks.ModBlocks;
import net.fryc.recallstaffs.command.ResetStaffCooldownCommand;
import net.fryc.recallstaffs.config.RecallStaffsConfig;
import net.fryc.recallstaffs.craftingmanipulator.Rules;
import net.fryc.recallstaffs.effects.ModEffects;
import net.fryc.recallstaffs.event.CopyRecallStaffCooldown;
import net.fryc.recallstaffs.items.ModItems;
import net.fryc.recallstaffs.network.ModPackets;
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

		Rules.enableCraftingManipulatorRules();

		CommandRegistrationCallback.EVENT.register(ResetStaffCooldownCommand::register);

		ModPackets.registerPayloads();
	}
}
