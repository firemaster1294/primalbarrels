/************************
 borrowed from iron chests
 ********************* */
package com.im1294.primalbarrels.network;

import com.im1294.primalbarrels.PrimalBarrels;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;


public class PacketHandler {
  private static final String PROTOCOL_VERSION = "1.0";

  private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(PrimalBarrels.MODID, "main_channel"))
          .clientAcceptedVersions(PROTOCOL_VERSION::equals)
          .serverAcceptedVersions(PROTOCOL_VERSION::equals)
          .networkProtocolVersion(() -> PROTOCOL_VERSION)
          .simpleChannel();

  public static void register() {
    //HANDLER.registerMessage(0, PacketTopStackSyncChest.class, PacketTopStackSyncChest::encode, PacketTopStackSyncChest::decode, PacketTopStackSyncChest.Handler::handle);
  }

  public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
    HANDLER.send(target, message);
  }
}
