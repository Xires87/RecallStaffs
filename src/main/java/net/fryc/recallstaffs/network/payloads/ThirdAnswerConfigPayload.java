package net.fryc.recallstaffs.network.payloads;

import net.fryc.recallstaffs.network.ModPackets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ThirdAnswerConfigPayload(int recallStaffCraftCost) implements CustomPayload {

    public static final Id<ThirdAnswerConfigPayload> ID = new Id<>(ModPackets.THIRD_ANSWER_CONFIG_ID);
    public static final PacketCodec<RegistryByteBuf, ThirdAnswerConfigPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, ThirdAnswerConfigPayload::recallStaffCraftCost,
            ThirdAnswerConfigPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}