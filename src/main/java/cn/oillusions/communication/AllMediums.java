package cn.oillusions.communication;

import cn.oillusions.communication.content.wireless.WirelessMedium;
import cn.oillusions.communication.foundation.core.graph.Medium;
import cn.oillusions.communication.foundation.core.type.MediumType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllMediums {
    private static final DeferredRegister<MediumType<?>> REGISTER = DeferredRegister.create(
        AllRegistries.MEDIUMS, Communication.ID);

    public static final DeferredHolder<MediumType<?>, MediumType<WirelessMedium>> WIRELESS_MEDIUM = REGISTER.register(
        "wireless", () -> WirelessMedium.TYPE);


    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
