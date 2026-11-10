package cn.oillusions.communication;

import cn.oillusions.communication.content.wireless.WirelessMedium;
import cn.oillusions.communication.foundation.core.graph.Medium;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllMediums {
    private static final DeferredRegister<Medium> REGISTER = DeferredRegister.create(
        AllRegistries.MEDIUMS, Communication.ID);

    public static final DeferredHolder<Medium, WirelessMedium> WIRELESS_MEDIUM = REGISTER.register(
        "wireless", WirelessMedium::new);


    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
