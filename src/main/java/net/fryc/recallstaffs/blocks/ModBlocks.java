package net.fryc.recallstaffs.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.blocks.custom.RecoveryAltarBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block RECOVERY_ALTAR = registerBlock("recovery_altar",
            new RecoveryAltarBlock(AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).instrument(Instrument.BASS).strength(3f , 6f).sounds(BlockSoundGroup.GILDED_BLACKSTONE)));


    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(RecallStaffs.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(RecallStaffs.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }
    public static void registerModBlocks() {
    }
}
