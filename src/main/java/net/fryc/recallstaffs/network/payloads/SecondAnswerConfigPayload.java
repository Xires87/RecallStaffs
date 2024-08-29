package net.fryc.recallstaffs.network.payloads;

import net.fryc.recallstaffs.network.ModPackets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SecondAnswerConfigPayload(int woodCost, int copperCost, int ironCost, int goldCost, int diamondCost, int netheriteCost) implements CustomPayload {

    public static final Id<SecondAnswerConfigPayload> ID = new Id<>(ModPackets.SECOND_ANSWER_CONFIG_ID);
    public static final PacketCodec<RegistryByteBuf, SecondAnswerConfigPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, SecondAnswerConfigPayload::woodCost,
            PacketCodecs.INTEGER, SecondAnswerConfigPayload::copperCost,
            PacketCodecs.INTEGER, SecondAnswerConfigPayload::ironCost,
            PacketCodecs.INTEGER, SecondAnswerConfigPayload::goldCost,
            PacketCodecs.INTEGER, SecondAnswerConfigPayload::diamondCost,
            PacketCodecs.INTEGER, SecondAnswerConfigPayload::netheriteCost,
            SecondAnswerConfigPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}