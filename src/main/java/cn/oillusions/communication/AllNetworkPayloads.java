package cn.oillusions.communication;

import cn.oillusions.communication.foundation.communication.network.payload.CloseChannelSelectPayload;
import cn.oillusions.communication.foundation.communication.network.payload.OpenChannelCreateMenuPayload;
import cn.oillusions.communication.foundation.communication.network.payload.ReturnChannelCreateResultPayload;
import cn.oillusions.communication.foundation.communication.network.payload.ReturnChannelSelectResultPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class AllNetworkPayloads {
    @SubscribeEvent
    public static void onRegisterNetworkPayload(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
            OpenChannelCreateMenuPayload.TYPE,
            OpenChannelCreateMenuPayload.CODEC,
            OpenChannelCreateMenuPayload::handle
        );

        registrar.playToServer(
            ReturnChannelCreateResultPayload.TYPE,
            ReturnChannelCreateResultPayload.CODEC,
            ReturnChannelCreateResultPayload::handle
        );

        registrar.playToServer(
            ReturnChannelSelectResultPayload.TYPE,
            ReturnChannelSelectResultPayload.CODEC,
            ReturnChannelSelectResultPayload::handle
        );

        registrar.playToServer(
            CloseChannelSelectPayload.TYPE,
            CloseChannelSelectPayload.CODEC,
            CloseChannelSelectPayload::handle
        );
    }
}
