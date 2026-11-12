package cn.oillusions.communication.foundation.communication.menu;

import cn.oillusions.communication.AllMenus;
import cn.oillusions.communication.foundation.communication.AttachConnectorHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChannelSelectMenu extends AbstractContainerMenu {
    private final long taskID;
    private final List<ChannelEntry> clientChannels;

    public ChannelSelectMenu(int windowID, Inventory playerInventory, Player player, long taskID) {
        super(AllMenus.CHANNEL_SELECT_MENU.get(), windowID);
        this.taskID = taskID;
        this.clientChannels = List.of();
    }

    public ChannelSelectMenu(int windowID, Inventory playerInventory, RegistryFriendlyByteBuf extraDataBuf) {
        super(AllMenus.CHANNEL_SELECT_MENU.get(), windowID);

        ExtraData extraData = ExtraData.CODEC.decode(extraDataBuf);
        this.taskID = extraData.taskID();
        this.clientChannels = extraData.channelEntries();
    }

    public long taskID() {
        return taskID;
    }

    public List<ChannelEntry> allChannels() {
        return clientChannels;
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return AttachConnectorHandler.contains(taskID);
    }

    public record ExtraData(long taskID, List<ChannelEntry> channelEntries) {
        public static final StreamCodec<RegistryFriendlyByteBuf, ExtraData> CODEC =
            StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, ExtraData::taskID,
                ChannelEntry.LIST_CODEC, ExtraData::channelEntries,
                ExtraData::new
            );
    };

    public record ChannelEntry(long edgeID, String edgeName) {
        public static final StreamCodec<RegistryFriendlyByteBuf, ChannelEntry> CODEC =
            StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, ChannelEntry::edgeID,
                ByteBufCodecs.STRING_UTF8, ChannelEntry::edgeName,
                ChannelEntry::new
            );

        public static final StreamCodec<RegistryFriendlyByteBuf, List<ChannelEntry>> LIST_CODEC =
            ByteBufCodecs.collection(ArrayList::new, ChannelEntry.CODEC);
    }
}
