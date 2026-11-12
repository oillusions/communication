package cn.oillusions.communication.util;

import cn.oillusions.communication.AllBlockEntities;
import cn.oillusions.communication.content.cct.NetworkCardPeripheral;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber
public class AllCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            PeripheralCapability.get(),
            AllBlockEntities.CC_TWEAKED_ADAPTER_BLOCK_ENTITY.get(),
            (blockEntity, side) -> new NetworkCardPeripheral(blockEntity)
        );
    }
}
