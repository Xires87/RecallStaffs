package net.fryc.recallstaffs.mixin;

import net.fryc.recallstaffs.items.custom.StaffItem;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
abstract class AnimalEntityMixin {

    @Inject(method = "interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void teleportCatOrDogHome(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ret) {
        AnimalEntity dys = ((AnimalEntity) (Object)this);
        if(dys instanceof TameableEntity tameable){
            if(tameable.isOwner(player) && tameable.isSitting()){
                if(player.getStackInHand(hand).getItem() instanceof StaffItem){
                    if(!player.getWorld().isClient()){
                        if(player instanceof ServerPlayerEntity sPlayer){
                            Vec3d vec3d = tameable.getPos();
                            BlockPos spawnPos = ((ServerPlayerGetters) sPlayer).getServerPlayerSpawnPosition();
                            if(spawnPos == null) spawnPos = sPlayer.getWorld().getSpawnPos();
                            if(sPlayer.getServer() != null){
                                tameable.teleport(sPlayer.getServer().getWorld(sPlayer.getSpawnPointDimension()), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(),null, tameable.getYaw(), tameable.getPitch());
                                sPlayer.getWorld().emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(tameable));
                            }
                        }
                    }
                    ret.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}
