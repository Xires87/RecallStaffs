package net.fryc.recallstaffs.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.server.network.ServerPlayerEntity;

public class CopyRecallStaffCooldown implements ServerPlayerEvents.CopyFrom{
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        //copies recall staff cooldown when player dies
        ((ServerPlayerGetters) newPlayer).setRecallStaffCooldown(((ServerPlayerGetters) oldPlayer).getRecallStaffCooldown());
    }
}
