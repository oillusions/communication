package cn.oillusions.communication;

import cn.oillusions.communication.content.menu.ChannelCreateMenu;
import cn.oillusions.communication.content.menu.ChannelSelectMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllMenus {
    private static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, Communication.ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ChannelSelectMenu>> CHANNEL_SELECT_MENU = REGISTER.register(
        "channel_select", () -> IMenuTypeExtension.create(ChannelSelectMenu::new)
    );

    public static final DeferredHolder<MenuType<?>, MenuType<ChannelCreateMenu>> CHANNEL_CREATE_MENU = REGISTER.register(
        "channel_create", () -> IMenuTypeExtension.create(ChannelCreateMenu::new)
    );


    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
