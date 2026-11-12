package cn.oillusions.communication.foundation.communication.menu;

import cn.oillusions.communication.AllMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ChannelCreateMenu extends AbstractContainerMenu {
    private final long selectTaskID;
    public ChannelCreateMenu(int windowID, Inventory playerInventory) {
        super(AllMenus.CHANNEL_CREATE_MENU.get(), windowID);

        this.selectTaskID = -1;
    }

    public ChannelCreateMenu(int windowID, Inventory playerInventory, RegistryFriendlyByteBuf extraDataBuf) {
        super(AllMenus.CHANNEL_CREATE_MENU.get(), windowID);

        this.selectTaskID = ExtraData.CODEC.decode(extraDataBuf).SelectTaskID();
    }

    public long selectTaskID() {
        return selectTaskID;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public record ExtraData(long SelectTaskID) {
        public static final StreamCodec<RegistryFriendlyByteBuf, ExtraData> CODEC =
            StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, ExtraData::SelectTaskID,
                ExtraData::new
            );
    }
}
