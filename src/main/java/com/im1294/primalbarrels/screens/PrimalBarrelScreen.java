package com.im1294.primalbarrels.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.im1294.primalbarrels.PrimalBarrels;
import com.im1294.primalbarrels.container.PrimalBarrelContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class PrimalBarrelScreen extends AbstractContainerScreen<PrimalBarrelContainer> {

  private final ResourceLocation texture;

  private final boolean isTall;

  private final boolean isWide;

  public PrimalBarrelScreen(PrimalBarrelContainer barrelContainer, Inventory playerInventory, Component component,
                           ResourceLocation texture) {
    super(barrelContainer, playerInventory, component);
    this.imageWidth = 14 + 18 * Math.max(barrelContainer.width, 9);
    this.imageHeight = 114 + 18 * barrelContainer.height;
    this.texture = texture;
    this.inventoryLabelY = this.imageHeight - 94;
    isTall = imageHeight > 256;
    isWide = imageWidth > 256;
  }

  @Override
  public void render(PoseStack stack,int x, int y, float p_render_3_) {
    this.renderBackground(stack);
    super.render(stack,x, y, p_render_3_);
    this.renderTooltip(stack,x,y);
  }

  /**
   * Draws the background layer of this container (behind the items).
   *
   * @param partialTicks
   * @param mouseX
   * @param mouseY
   */
  @Override
  protected void renderBg(PoseStack stack,float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShaderTexture(0,texture);
    int i = (this.width - this.imageWidth) / 2;
    int j = (this.height - this.imageHeight) / 2;
    if (!isTall) {
      this.blit(stack,i, j, 0, 0, this.imageWidth, this.imageHeight);
    } else if (!isWide) {
      blit(stack,i, j, getBlitOffset(),0, 0, this.imageWidth, this.imageHeight,256,512);
    } else {
      blit(stack,i, j, getBlitOffset(), 0, 0,this.imageWidth, this.imageHeight,512,512);
    }
  }

  private static final ResourceLocation PRIMAL = new ResourceLocation(PrimalBarrels.MODID,"textures/gui/container/primal.png");
  private static final ResourceLocation ADVANCED_PRIMAL = new ResourceLocation(PrimalBarrels.MODID,"textures/gui/container/advanced_primal.png");
  private static final ResourceLocation DEVELOPED_PRIMAL = new ResourceLocation(PrimalBarrels.MODID,"textures/gui/container/developed_primal.png");

  public static PrimalBarrelScreen primal(PrimalBarrelContainer barrelContainer, Inventory playerInventory, Component component) {
    return new PrimalBarrelScreen(barrelContainer,playerInventory,component,PRIMAL);
  }
  public static PrimalBarrelScreen advanced_primal(PrimalBarrelContainer barrelContainer, Inventory playerInventory, Component component) {
    return new PrimalBarrelScreen(barrelContainer,playerInventory,component,ADVANCED_PRIMAL);
  }
  public static PrimalBarrelScreen developed_primal(PrimalBarrelContainer barrelContainer, Inventory playerInventory, Component component) {
    return new PrimalBarrelScreen(barrelContainer,playerInventory,component,DEVELOPED_PRIMAL);
  }
}
