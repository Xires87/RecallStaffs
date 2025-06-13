package net.fryc.recallstaffs.tags;

import net.fryc.recallstaffs.RecallStaffs;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModBlockTags {

    public static final TagKey<Block> CALIBRATES_RECALL_STAFF = ModBlockTags.register("calibrates_recall_staff");
    public static final TagKey<Block> REVERTS_RECALL_STAFF_CALIBRATION = ModBlockTags.register("reverts_recall_staff_calibration");

    private ModBlockTags() {
    }

    private static TagKey<Block> register(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(RecallStaffs.MOD_ID, id));
    }
}
