package cn.oillusions.communication.foundation.communication.network.payload;

import cn.oillusions.communication.Communication;
import cn.oillusions.communication.foundation.communication.AttachConnectorHandler;
import cn.oillusions.communication.foundation.communication.ServerGraphHolder;
import cn.oillusions.communication.foundation.core.graph.Graph;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ReturnChannelCreateResultPayload(long selectTaskID, String edgeName) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, ReturnChannelCreateResultPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, ReturnChannelCreateResultPayload::selectTaskID,
            ByteBufCodecs.STRING_UTF8, ReturnChannelCreateResultPayload::edgeName,
            ReturnChannelCreateResultPayload::new
        );

    public static final Type<ReturnChannelCreateResultPayload> TYPE =
        new Type<>(Communication.modLoc("return_channel_create_result"));

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ReturnChannelCreateResultPayload payload, IPayloadContext context) {
        AttachConnectorHandler.returnChannelCreateResult(payload.selectTaskID(), payload.edgeName());
    }
}
