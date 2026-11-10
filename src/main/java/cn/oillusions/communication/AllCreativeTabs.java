package cn.oillusions.communication;

import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Communication.ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_TAB = REGISTER.register(
        "base",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.create_communication.base"))
            .withTabsBefore(AllCreativeModeTabs.BASE_CREATIVE_TAB.getId())
            .icon(() -> new ItemStack(AllBlocks.REDSTONE_ADAPTER_BLOCK.asStack().getItem()))
            .displayItems((params, output) -> {
                output.accept(AllItems.BASIC_PORT_ITEM.get());
                output.accept(AllItems.REDSTONE_ADAPTER.get());
                output.accept(AllItems.WIRELESS_CONNECTOR_ITEM.get());
            })
            .build()
    );

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
