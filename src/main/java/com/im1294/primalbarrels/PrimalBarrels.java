/************************
Huge thanks to the Metal Barrels mod,
which provided the backbone to this mod
 ********************* */
package com.im1294.primalbarrels;

import com.google.common.collect.Sets;
import com.im1294.primalbarrels.block.*;
import com.im1294.primalbarrels.container.*;
import com.im1294.primalbarrels.network.PacketHandler;
import com.im1294.primalbarrels.screens.*;
import com.im1294.primalbarrels.tile.*;
import com.im1294.primalbarrels.util.PrimalBarrelBlockEntityType;
import com.im1294.primalbarrels.util.ModTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PrimalBarrels.MODID)
public class PrimalBarrels {

  public static final String MODID = "primalbarrels";

  public static final Logger logger = LogManager.getLogger();

  public static final CreativeModeTab tab = new CreativeModeTab(MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ObjectHolders.PRIMAL_BARREL);
    }
  };

  public PrimalBarrels() {
    // Register doClientStuff method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    // Register commonSetup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
  }

  private void commonSetup(final FMLCommonSetupEvent event){
    PacketHandler.register();
  }

  private void doClientStuff(final FMLClientSetupEvent event) {
    // do something that can only be done on the client
    MenuScreens.register(ObjectHolders.PRIMAL_CONTAINER, PrimalBarrelScreen::primal);
    MenuScreens.register(ObjectHolders.ADVANCED_PRIMAL_CONTAINER, PrimalBarrelScreen::advanced_primal);
    MenuScreens.register(ObjectHolders.DEVELOPED_PRIMAL_CONTAINER, PrimalBarrelScreen::developed_primal);
  }

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    private static final Set<Block> MOD_BLOCKS = new HashSet<>();

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
      Block.Properties wood = Block.Properties.of(Material.WOOD).strength(2.5f).sound(SoundType.WOOD);
      registerBlock(new PrimalBarrelBlock(wood,PrimalBarrelBlockEntityType.primal()),"primal_barrel",blockRegistryEvent.getRegistry());
      registerBlock(new PrimalBarrelBlock(wood,PrimalBarrelBlockEntityType.advanced_primal()),"advanced_primal_barrel",blockRegistryEvent.getRegistry());
      registerBlock(new PrimalBarrelBlock(wood,PrimalBarrelBlockEntityType.developed_primal()),"developed_primal_barrel",blockRegistryEvent.getRegistry());
    }
    private static void registerBlock(Block block, String name, IForgeRegistry<Block> registry) {
      registry.register(block.setRegistryName(name));
      MOD_BLOCKS.add(block);
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {

      IForgeRegistry<Item> registry = itemRegistryEvent.getRegistry();
      for (Block block : MOD_BLOCKS) {
        Item.Properties properties = new Item.Properties().tab(tab);
        if (block == ObjectHolders.DEVELOPED_PRIMAL_BARREL) {
          properties.fireResistant();
        }
        Item item = new BlockItem(block, properties);
        registerItem(item, block.getRegistryName().toString(), registry);
      }
    }

    private static void registerItem(Item item, String name, IForgeRegistry<Item> registry) {
      registry.register(item.setRegistryName(name));
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {

      event.getRegistry().register(new MenuType<>(PrimalBarrelContainer::primal).setRegistryName("primal_container"));
      event.getRegistry().register(new MenuType<>(PrimalBarrelContainer::advanced_primal).setRegistryName("advanced_primal_container"));
      event.getRegistry().register(new MenuType<>(PrimalBarrelContainer::developed_primal).setRegistryName("developed_primal_container"));

    }

    @SubscribeEvent
    public static void registerTiles(RegistryEvent.Register<BlockEntityType<?>> event) {

        event.getRegistry().register(new PrimalBarrelBlockEntityType<>(PrimalBarrelBlockEntityType.primal(),
              Sets.newHashSet(ObjectHolders.PRIMAL_BARREL),
              null, 9, 1, PrimalBarrelContainer::primalS)
              .setRegistryName("primal_tile"));

        event.getRegistry().register(new PrimalBarrelBlockEntityType<>(PrimalBarrelBlockEntityType.advanced_primal(),
              Sets.newHashSet(ObjectHolders.ADVANCED_PRIMAL_BARREL),
              null, 9, 2, PrimalBarrelContainer::advanced_primalS)
              .setRegistryName("advanced_primal_tile"));
        
        event.getRegistry().register(new PrimalBarrelBlockEntityType<>(PrimalBarrelBlockEntityType.developed_primal(),
              Sets.newHashSet(ObjectHolders.DEVELOPED_PRIMAL_BARREL),
              null, 9, 2, PrimalBarrelContainer::developed_primalS)
              .setRegistryName("developed_primal_tile"));
    }
  }

  @ObjectHolder(PrimalBarrels.MODID)
  public static class ObjectHolders {

    public static final Block PRIMAL_BARREL = null;
    public static final MenuType<PrimalBarrelContainer> PRIMAL_CONTAINER = null;
    public static final BlockEntityType<PrimalBarrelBlockEntity> PRIMAL_TILE = null;

    public static final Block ADVANCED_PRIMAL_BARREL = null;
    public static final MenuType<PrimalBarrelContainer> ADVANCED_PRIMAL_CONTAINER = null;
    public static final BlockEntityType<PrimalBarrelBlockEntity> ADVANCED_PRIMAL_TILE = null;

    public static final Block DEVELOPED_PRIMAL_BARREL = null;
    public static final MenuType<PrimalBarrelContainer> DEVELOPED_PRIMAL_CONTAINER = null;
    public static final BlockEntityType<PrimalBarrelBlockEntity> DEVELOPED_PRIMAL_TILE = null;
  }
}
