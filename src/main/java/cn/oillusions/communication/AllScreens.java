package cn.oillusions.communication;

import cn.oillusions.communication.content.client.gui.ChannelCreateScreen;
import cn.oillusions.communication.content.client.gui.ChannelSelectScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Communication.ID)
public class AllScreens {

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(
            AllMenus.CHANNEL_SELECT_MENU.get(),
            ChannelSelectScreen::new
        );
        event.register(
            AllMenus.CHANNEL_CREATE_MENU.get(),
            ChannelCreateScreen::new
        );
    }
}
