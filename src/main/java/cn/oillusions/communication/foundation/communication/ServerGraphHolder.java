package cn.oillusions.communication.foundation.communication;

import cn.oillusions.communication.Communication;
import cn.oillusions.communication.foundation.core.graph.Graph;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = Communication.ID)
public class ServerGraphHolder {
    public static final String FILE_ID = "communication.graph";
    @Nullable public static Graph layer;


    public static Graph get() {
        if (layer == null) {
            Communication.LOGGER.error("物理层还没初始化, 呃啊", new Throwable("stack trace"));
            throw new IllegalStateException("呱, 物理层还没初始化, 呃啊");
        }
        return layer;
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        Communication.LOGGER.info("ServerStartedEvent 触发, 创建物理层");
        layer = Graph.get(event.getServer(), Graph.FACTORY, FILE_ID);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (layer != null) layer.tick();
    }
}
