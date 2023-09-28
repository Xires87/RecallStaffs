package net.fryc.recallstaffs.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.blocks.custom.RecoveryAltarBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ModBlocks {

    public static final Block RECOVERY_ALTAR = registerBlock("recovery_altar",
            new RecoveryAltarBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_TILES).nonOpaque()));

    private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return false;
    }

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
