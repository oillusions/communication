package cn.oillusions.communication;


import cn.oillusions.communication.content.redstone.RedstoneSignalStrengthPayload;
import cn.oillusions.communication.foundation.core.type.PayloadType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllPayloads {
    private static final DeferredRegister<PayloadType<?>> REGISTER = DeferredRegister.create(
        AllRegistries.PAYLOADS, Communication.ID);

    public static final DeferredHolder<PayloadType<?>, PayloadType<RedstoneSignalStrengthPayload>> REDSTONE_SIGNAL_PAYLOAD =
        REGISTER.register("redstone_signal", () -> RedstoneSignalStrengthPayload.TYPE);


    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
