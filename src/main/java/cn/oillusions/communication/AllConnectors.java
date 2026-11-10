package cn.oillusions.communication;

import cn.oillusions.communication.content.wireless.WirelessConnector;
import cn.oillusions.communication.foundation.core.type.ConnectorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllConnectors {
    private static final DeferredRegister<ConnectorType<?>> REGISTER = DeferredRegister.create(
        AllRegistries.CONNECTORS, Communication.ID);

    public static final DeferredHolder<ConnectorType<?>, ConnectorType<WirelessConnector>> WIRELESS_CONNECTOR = REGISTER.register(
        "wireless", () -> WirelessConnector.TYPE);



    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
