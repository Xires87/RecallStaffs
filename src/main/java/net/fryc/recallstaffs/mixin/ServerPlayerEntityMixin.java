package net.fryc.recallstaffs.mixin;

import com.mojang.authlib.GameProfile;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerGetters {

    @Shadow
    @Nullable
    private BlockPos spawnPointPosition;

    private int recallStaffCooldown = 0;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }


    //reading recallStaffCooldown from Nbt
    @Inject(method = "Lnet/minecraft/server/network/ServerPlayerEntity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void readRecallStaffCooldownFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains("RecallStaffCooldown")){
            NbtCompound nbtCompound = nbt.getCompound("RecallStaffCooldown");
            recallStaffCooldown = nbtCompound.getInt("RScooldown");
        }
    }

    //writing recallStaffCooldown to Nbt
    @Inject(method = "Lnet/minecraft/server/network/ServerPlayerEntity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void writeRecallStaffCooldownToNbt(NbtCompound nbt, CallbackInfo ci) {
        if(recallStaffCooldown != 0){
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putInt("RScooldown", recallStaffCooldown);
            nbt.put("RecallStaffCooldown", nbtCompound);
        }
    }

    //decrementing recall staff cooldown
    @Inject(method = "Lnet/minecraft/server/network/ServerPlayerEntity;tick()V", at = @At("TAIL"))
    private void decrementRecallStaffCooldown(CallbackInfo ci) {
        if(recallStaffCooldown > 0) recallStaffCooldown--;
    }

    @Override
    public BlockPos getServerPlayerSpawnPosition() {
        return this.spawnPointPosition;
    }

    @Override
    public int getRecallStaffCooldown() {
        return recallStaffCooldown;
    }

    @Override
    public void setRecallStaffCooldown(int cooldown) {
        recallStaffCooldown = cooldown;
    }
}
