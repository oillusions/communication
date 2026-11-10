package cn.oillusions.communication;


import cn.oillusions.communication.content.basic.BasicSlot;
import cn.oillusions.communication.foundation.core.type.SlotType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllSlots {
    private static final DeferredRegister<SlotType<?>> REGISTER = DeferredRegister.create(
        AllRegistries.SLOTS, Communication.ID);

    public static final DeferredHolder<SlotType<?>, SlotType<BasicSlot>> BASIC_PORT = REGISTER.register(
        "basic", () -> BasicSlot.TYPE);



    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
