package cn.oillusions.communication;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Communication.ID)
public class Communication {
    public static final String ID = "communication";
    public static final Component NAME = Component.translatable("communication.name");
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);

    public static final String SableID = "sable";

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public Communication(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        AllMediums.register(modEventBus);
        AllPayloads.register(modEventBus);
        AllSlots.register(modEventBus);
        AllConnectors.register(modEventBus);
        AllMenus.register(modEventBus);
        AllBlocks.register(modEventBus);
        AllItems.register(modEventBus);
        AllCreativeTabs.register(modEventBus);
        AllBlockEntities.register(modEventBus);
    }

    public static boolean hasMod(String modID) {
        return ModList.get().isLoaded(modID);
    }
}
