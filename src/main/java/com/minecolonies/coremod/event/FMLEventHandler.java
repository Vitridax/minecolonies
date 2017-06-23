package com.minecolonies.coremod.event;

import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.coremod.MineColonies;
import com.minecolonies.coremod.blocks.ModBlocks;
import com.minecolonies.coremod.colony.ColonyManager;
import com.minecolonies.coremod.items.ModItems;
import com.minecolonies.coremod.network.messages.ColonyStylesMessage;
import com.minecolonies.coremod.network.messages.ServerUUIDMessage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event handler used to catch various forge events.
 */
public class FMLEventHandler
{
    /**
     * Called when the server ticks.
     * Calls {@link ColonyManager#onServerTick(TickEvent.ServerTickEvent)}.
     *
     * @param event {@link net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent}.
     */
    @SubscribeEvent
    public void onServerTick(final TickEvent.ServerTickEvent event)
    {
        ColonyManager.onServerTick(event);
    }

    /**
     * Called when the client ticks.
     * Calls {@link ColonyManager#onClientTick(TickEvent.ClientTickEvent)}.
     *
     * @param event {@link net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent}.
     */
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event)
    {
        ColonyManager.onClientTick(event);
    }

    /**
     * Called when the world ticks.
     * Calls {@link ColonyManager#onWorldTick(TickEvent.WorldTickEvent)}.
     *
     * @param event {@link net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent}.
     */
    @SubscribeEvent
    public void onWorldTick(final TickEvent.WorldTickEvent event)
    {
        ColonyManager.onWorldTick(event);
    }

    /**
     * Called when a player logs in. If the joining player is a MP-Player, sends
     * all possible styles in a message.
     *
     * @param event {@link net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent}
     */
    @SubscribeEvent
    public void onPlayerLogin(@NotNull final PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            MineColonies.getNetwork().sendTo(new ServerUUIDMessage(), (EntityPlayerMP) event.player);
            MineColonies.getNetwork().sendTo(new ColonyStylesMessage(), (EntityPlayerMP) event.player);
            ColonyManager.syncAllColoniesAchievements();
        }
    }

    /**
     * Called when the config is changed, used to synch between file and game.
     * @param event the on config changed event.
     */
    @SubscribeEvent
    public void onConfigChanged(@NotNull final ConfigChangedEvent.OnConfigChangedEvent event)
    {
        ConfigManager.sync(Constants.MOD_ID, Config.Type.INSTANCE);
    }

    /**
     * Called when registering blocks,
     * we have to register all our modblocks here.
     * @param event the registery event for blocks.
     */
    @SubscribeEvent
    public void registerBlocks(@NotNull final RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                ModBlocks.blockConstructionTape,
                ModBlocks.blockConstructionTapeCorner,
                ModBlocks.blockHutBaker,
                ModBlocks.blockHutBlacksmith,
                ModBlocks.blockHutBuilder,
                ModBlocks.blockHutCitizen,
                ModBlocks.blockHutDeliveryman,
                ModBlocks.blockHutFarmer,
                ModBlocks.blockHutField,
                ModBlocks.blockHutFisherman,
                ModBlocks.blockHutGuardTower,
                ModBlocks.blockHutLumberjack,
                ModBlocks.blockHutMiner,
                ModBlocks.blockHutStonemason,
                ModBlocks.blockHutTownHall,
                ModBlocks.blockHutWareHouse,
                ModBlocks.blockSolidSubstitution,
                ModBlocks.blockSubstitution);
    }

    /**
     * Called when registering items,
     * we have to register all our mod items here.
     * @param event the registery event for items.
     */
    @SubscribeEvent
    public void registerItems(@NotNull final RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                ModItems.buildTool,
                ModItems.caliper,
                ModItems.permTool,
                ModItems.scanTool,
                ModItems.scepterGuard,
                ModItems.supplyCamp,
                ModItems.supplyChest,
                new ItemBlock(ModBlocks.blockConstructionTape),
                new ItemBlock(ModBlocks.blockConstructionTapeCorner));
    }


}
