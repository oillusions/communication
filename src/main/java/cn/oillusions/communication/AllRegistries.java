package cn.oillusions.communication;

import cn.oillusions.communication.foundation.core.graph.Medium;
import cn.oillusions.communication.foundation.core.type.ConnectorType;
import cn.oillusions.communication.foundation.core.type.PayloadType;
import cn.oillusions.communication.foundation.core.type.SlotType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Communication.ID)
public class AllRegistries {
    public static final Registry<SlotType<?>> SLOTS = create(AllTypes.SLOT_TYPE_KEY);
    public static final Registry<ConnectorType<?>> CONNECTORS = create(AllTypes.CONNECTOR_TYPE_KEY);
    public static final Registry<PayloadType<?>> PAYLOADS = create(AllTypes.PAYLOAD_TYPE_KEY);
    public static final Registry<Medium> MEDIUMS = create(AllTypes.MEDIUM_TYPE_KEY);


    private static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
        return new RegistryBuilder<>(key)
            .sync(false)
            .create();
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(SLOTS);
        event.register(CONNECTORS);
        event.register(PAYLOADS);
        event.register(MEDIUMS);
    }

    private AllRegistries() {};
}
