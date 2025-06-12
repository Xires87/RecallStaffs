package net.fryc.recallstaffs.items.custom;

import net.fryc.recallstaffs.RecallStaffs;
import net.fryc.recallstaffs.config.ConfigHelper;
import net.fryc.recallstaffs.items.ModItems;
import net.fryc.recallstaffs.util.ServerPlayerGetters;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.Optional;

public class StaffItem extends Item {

    public StaffItem(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(RecallStaffs.config.enableTooltipsForRecallStaffs){
            Pair<Integer, Integer> pair = ConfigHelper.getClientRecallStaffCostAndCooldown(stack);
            tooltip.add(Text.literal(Text.translatable("text.recallstaffs.usage_cost").getString() + ": " + pair.getA() + " " + Text.translatable("text.recallstaffs.levels").getString()).formatted(Formatting.BLUE));
            tooltip.add(Text.literal(Text.translatable("text.recallstaffs.cooldown").getString() + ": " + pair.getB() + Text.translatable("text.recallstaffs.seconds").getString()).formatted(Formatting.GRAY));
        }
        if(isCalibrated(stack)){
            tooltip.add(Text.translatable("text.recallstaffs.revert_calibration"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 140;
    }

    public Text getName(ItemStack stack) {
        String calibrated = isCalibrated(stack) ? "_calibrated" : "";
        return Text.translatable(this.getTranslationKey(stack) + calibrated);
    }

    public ActionResult useOnBlock(ItemUsageContext context){
        if(!context.getWorld().isClient()){
            if(this.canBeCalibrated()){
                if(isCalibrated(context.getStack())){
                    if(context.getWorld().getBlockState(context.getBlockPos()).getBlock().equals(Blocks.GRINDSTONE)){
                        context.getStack().remove(DataComponentTypes.LODESTONE_TRACKER);
                        context.getWorld().playSound(null, context.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS);

                        return ActionResult.success(true);
                    }
                }
                else if(context.getWorld().getBlockState(context.getBlockPos()).getBlock().equals(Blocks.LODESTONE)){
                    context.getStack().set(DataComponentTypes.LODESTONE_TRACKER, new LodestoneTrackerComponent(
                            Optional.of(new GlobalPos(context.getWorld().getRegistryKey(), context.getBlockPos())),
                            true
                    ));
                    context.getWorld().playSound(null, context.getBlockPos(), SoundEvents.ITEM_LODESTONE_COMPASS_LOCK, SoundCategory.BLOCKS);

                    return ActionResult.success(true);
                }
            }
        }

        return ActionResult.PASS;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(user instanceof ServerPlayerEntity splayer){
            int cooldown = ((ServerPlayerGetters) splayer).getRecallStaffCooldown();
            if(cooldown < 1){
                user.setCurrentHand(hand);
                /*
                int time = setNauseaTime(this);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200 + time, 0));
                 */
                return TypedActionResult.pass(itemStack);
            }
            else{
                cooldown /= 20;
                this.cancelTeleportation(Text.literal(
                        Text.translatable("text.recallstaffs.time_before_next_recall").getString() +
                                ": " +
                                cooldown +
                                Text.translatable("text.recallstaffs.seconds").getString()
                ), splayer, itemStack);

                return TypedActionResult.fail(itemStack);
            }
        }
        return TypedActionResult.fail(itemStack);
    }


    /*
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(remainingUseTicks > 0 && user.hasStatusEffect(StatusEffects.NAUSEA)){
            if(user.getActiveStatusEffects().get(StatusEffects.NAUSEA).getDuration() < 800){
                user.removeStatusEffect(StatusEffects.NAUSEA);
            }
        }
    }

     */

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        if (!world.isClient()) {
            AbstractHorseEntity horse = null;
            int additionalCost = 0;
            if (user.hasVehicle()) {
                if(user.getVehicle() instanceof AbstractHorseEntity horseEntity){
                    horse = horseEntity;
                    additionalCost = RecallStaffs.config.recallingWithHorseAdditionalCost;
                }
                user.stopRiding();
            }

            if (user instanceof ServerPlayerEntity player) {
                if(player.getServer() != null){
                    Pair<Integer, Integer> pair = ConfigHelper.getRecallStaffCostAndCooldown(stack, world);
                    int recallCost = pair.getA();
                    int recallCooldown = pair.getB();
                    boolean shouldCheckLevel = isCalibrated(stack) ? RecallStaffs.config.checkPlayersLevelBeforeCalibratedRecall : RecallStaffs.config.checkPlayersLevelBeforeRecall;
                    if(!(player.experienceLevel < recallCost && shouldCheckLevel)){
                        BlockPos spawnPos = null;
                        RegistryKey<World> dimension = null;
                        if(isCalibrated(stack)){
                            Optional<GlobalPos> optional = stack.get(DataComponentTypes.LODESTONE_TRACKER).target();
                            if(optional.isPresent()){
                                spawnPos = optional.get().pos();
                                dimension = optional.get().dimension();
                            }
                            if(spawnPos == null || !isLodeStone(player.getServer().getWorld(dimension), spawnPos)) {
                                this.cancelTeleportation(Text.translatable("text.recallstaffs.calibrated_teleport_error").formatted(Formatting.RED), player, stack);

                                return itemStack;
                            }
                        }

                        if(spawnPos == null) {
                            spawnPos = ((ServerPlayerGetters) player).getServerPlayerSpawnPosition() == null ?
                                    player.getWorld().getSpawnPos() : ((ServerPlayerGetters) player).getServerPlayerSpawnPosition();
                            dimension = player.getSpawnPointDimension();
                        }

                        this.teleport(
                                world, player, player.getServer().getWorld(dimension),
                                spawnPos, stack, recallCost, recallCooldown, horse, additionalCost
                        );
                    }
                    else {
                        this.cancelTeleportation(Text.translatable("text.recallstaffs.too_low_level").formatted(Formatting.RED), player, stack);
                    }
                }
            }
        }

        return itemStack;
    }

    public boolean canBeCalibrated(){
        return this != ModItems.WOODEN_RECALL_STAFF;
    }

    public static boolean isCalibrated(ItemStack stack){
        return stack.contains(DataComponentTypes.LODESTONE_TRACKER);
    }

    public boolean hasGlint(ItemStack stack){
        return isCalibrated(stack) || super.hasGlint(stack);
    }

    private void teleport(
            World currentWorld, ServerPlayerEntity player, ServerWorld destinationWorld,
            BlockPos destinationPos, ItemStack usedItem, int recallCost,
            int recallCooldown, @Nullable AbstractHorseEntity horse, int additionalCost
    ){
        player.setInvulnerable(true);

        int prevX = (int) player.getX();
        int prevY = (int) player.getY();
        int prevZ = (int) player.getZ();
        Vec3d previousPos = player.getPos();

        int destY = isCalibrated(usedItem) ? destinationPos.getY() + 1 : destinationPos.getY();

        if(horse != null){
            horse.teleport(destinationWorld, destinationPos.getX(), destY, destinationPos.getZ(), null, horse.getYaw(), horse.getPitch());
        }
        player.teleport(destinationWorld, destinationPos.getX(), destY, destinationPos.getZ(), player.getYaw(), player.getPitch());
        currentWorld.emitGameEvent(GameEvent.TELEPORT, previousPos, GameEvent.Emitter.of(player));

        player.getItemCooldownManager().set(usedItem.getItem(), 100);
        player.setExperienceLevel(changePlayerLevel(player.experienceLevel, recallCost, additionalCost));

        if(RecallStaffs.config.recallingSummonsLightningBolt){
            EntityType.LIGHTNING_BOLT.spawn((ServerWorld) currentWorld, new BlockPos(prevX, prevY, prevZ), SpawnReason.TRIGGERED);
        }
        ((ServerPlayerGetters) player).setRecallStaffCooldown(recallCooldown * 20);

        player.setInvulnerable(false);
    }

    private void cancelTeleportation(Text message, ServerPlayerEntity player, ItemStack stack){
        player.getItemCooldownManager().set(stack.getItem(), 40);
        player.sendMessage(message, true);
    }

    private static boolean isLodeStone(ServerWorld world, BlockPos pos){
        if(world == null){
            return false;
        }

        return world.getBlockState(pos).getBlock().equals(Blocks.LODESTONE);
    }

    private static int changePlayerLevel(int playerLevel, int cost, int additionalCost){
        playerLevel -= cost + additionalCost;

        if(playerLevel < 0) playerLevel = 0;
        return playerLevel;
    }

    private static int setNauseaTime(Item item){
        int time = 600;
        if(item.getDefaultStack().isOf(ModItems.COPPER_RECALL_STAFF)) time -= 200;
        else if(item.getDefaultStack().isOf(ModItems.IRON_RECALL_STAFF)) time -= 300;
        else if(item.getDefaultStack().isOf(ModItems.GOLDEN_RECALL_STAFF)) time -= 400;
        else if(item.getDefaultStack().isOf(ModItems.DIAMOND_RECALL_STAFF)) time -= 500;
        else if(item.getDefaultStack().isOf(ModItems.NETHERITE_RECALL_STAFF)) time = 0;
        return time;
    }

}
