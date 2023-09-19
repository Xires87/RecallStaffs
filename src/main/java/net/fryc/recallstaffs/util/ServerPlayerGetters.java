package net.fryc.recallstaffs.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.Nullable;

public interface ServerPlayerGetters {

    BlockPos getServerPlayerSpawnPosition();

    int getRecallStaffCooldown();

    void setRecallStaffCooldown(int cooldown);

    @Nullable GlobalPos getPositionBeforeUsingRecoveryAltar();

    void setPositionBeforeUsingRecoveryAltar(GlobalPos pos);
}
