package net.fryc.recallstaffs.util;

import net.minecraft.util.math.BlockPos;

public interface ServerPlayerGetters {

    BlockPos getServerPlayerSpawnPosition();

    int getRecallStaffCooldown();

    void setRecallStaffCooldown(int cooldown);
}
