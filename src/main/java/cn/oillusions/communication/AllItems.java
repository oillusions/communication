package cn.oillusions.communication;

import cn.oillusions.communication.content.basic.item.BasicSlotItem;
import cn.oillusions.communication.content.wireless.item.WirelessConnectorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllItems {
    private static final DeferredRegister<Item> REGISTER = DeferredRegister.createItems(Communication.ID);

    public static final DeferredHolder<Item, BlockItem> REDSTONE_ADAPTER = REGISTER.register(
        "redstone_adapter", () -> new BlockItem(AllBlocks.REDSTONE_ADAPTER_BLOCK.get(), new Item.Properties())
    );

    public static final DeferredHolder<Item, WirelessConnectorItem> WIRELESS_CONNECTOR_ITEM = REGISTER.register(
        "wireless_connector", () -> new WirelessConnectorItem(new Item.Properties())
    );

    public static final DeferredHolder<Item, BlockItem> CC_TWEAKED_ADAPTER = REGISTER.register(
        "cc_tweaked_adapter", () -> new BlockItem(AllBlocks.CC_TWEAKED_ADAPTER_BLOCK.get(), new Item.Properties())
    );

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
