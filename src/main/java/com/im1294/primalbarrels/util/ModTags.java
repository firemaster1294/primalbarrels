package com.im1294.primalbarrels.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;

public class ModTags {

  public static class Blocks {
    public static final TagKey<Block> PRIMAL_BARRELS = tag("barrels/primal");
    public static final TagKey<Block> WOODEN_BARRELS = tag("barrels/wooden");

    private static TagKey<Block> tag(String name) {
      return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("forge",name));
    }
  }
}
