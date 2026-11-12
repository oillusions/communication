package cn.oillusions.communication.foundation.communication.network.payload;

import cn.oillusions.communication.Communication;
import cn.oillusions.communication.foundation.communication.menu.ChannelCreateMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenChannelCreateMenuPayload(long selectTaskID) implements CustomPacketPayload, MenuProvider {

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenChannelCreateMenuPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, OpenChannelCreateMenuPayload::selectTaskID,
            OpenChannelCreateMenuPayload::new
        );

    public static final Type<OpenChannelCreateMenuPayload> TYPE =
        new Type<>(Communication.modLoc("open_channel_create_menu"));

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenChannelCreateMenuPayload payload, IPayloadContext context) {
        context.player().openMenu(
            payload,
            buf -> ChannelCreateMenu.ExtraData.CODEC.encode(
                buf,
                new ChannelCreateMenu.ExtraData(payload.selectTaskID()))
        );
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return Component.literal("创建频道");
    }

    @Override
    @NotNull
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new ChannelCreateMenu(i, inventory);
    }
}
