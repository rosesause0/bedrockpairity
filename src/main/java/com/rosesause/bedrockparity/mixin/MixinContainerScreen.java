package com.rosesause.bedrockparity.mixin;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Set;

@Mixin(ContainerScreen.class)
public abstract class MixinContainerScreen<T extends Container> extends Screen implements IHasContainer<T> {

    /** A list of the players inventory slots */
    @Final
    @Shadow
    protected T container;
    @Final
    @Shadow
    protected PlayerInventory playerInventory;
    /** Holds the slot currently hovered */
    @Nullable
    @Shadow
    protected Slot hoveredSlot;
    @Nullable
    @Shadow
    private Slot returningStackDestSlot;

    /** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
    @Shadow
    protected int guiLeft;
    /** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
    @Shadow
    protected int guiTop;
    /** Used when touchscreen is enabled. */
    @Shadow
    private boolean isRightMouseClick;
    @Shadow
    private int touchUpX;
    @Shadow
    private int touchUpY;
    @Shadow
    private long returningStackTime;
    /** Used when touchscreen is enabled */
    @Shadow
    private ItemStack returningStack = ItemStack.EMPTY;
    @Final
    @Shadow
    protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
    @Shadow
    protected boolean dragSplitting;
    @Shadow
    private int dragSplittingRemnant;

    /** Used when touchscreen is enabled */
    @Shadow
    private ItemStack draggedStack = ItemStack.EMPTY;

    protected MixinContainerScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    /**
     * @author
     */
    @Overwrite
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        ContainerScreen screen = (ContainerScreen) ((Object) this);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawBackground(screen, matrixStack, mouseX, mouseY));
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableDepthTest();
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)i, (float)j, 0.0F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableRescaleNormal();
        this.hoveredSlot = null;
        int k = 240;
        int l = 240;
        RenderSystem.glMultiTexCoord2f(33986, 240.0F, 240.0F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        for(int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1) {
            Slot slot = this.container.inventorySlots.get(i1);
            if (slot.isEnabled()) {
                this.moveItems(matrixStack, slot);
            }

            if (this.isSlotSelected(slot, (double)mouseX, (double)mouseY) && slot.isEnabled()) {
                this.hoveredSlot = slot;
                RenderSystem.disableDepthTest();
                int j1 = slot.xPos;
                int k1 = slot.yPos;
                RenderSystem.colorMask(true, true, true, false);
                int slotColor = this.getSlotColor(i1);
                this.fillGradient(matrixStack, j1, k1, j1 + 16, k1 + 16, slotColor, slotColor);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();
            }
        }

        this.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(screen, matrixStack, mouseX, mouseY));
        PlayerInventory playerinventory = this.minecraft.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? playerinventory.getItemStack() : this.draggedStack;
        if (!itemstack.isEmpty()) {
            int j2 = 8;
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;
            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float)itemstack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);
                if (itemstack.isEmpty()) {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStackBig(itemstack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!this.returningStack.isEmpty()) {
            float f = (float)(Util.milliTime() - this.returningStackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            int l1 = this.touchUpX + (int)((float)l2 * f);
            int i2 = this.touchUpY + (int)((float)i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, (String)null);
        }

        RenderSystem.popMatrix();
        RenderSystem.enableDepthTest();
    }

    @Shadow
    public int getSlotColor(int index) {
        return -1;
    }

    @Shadow
    private boolean isSlotSelected(Slot slotIn, double mouseX, double mouseY) {
        return false;
    }

    @Shadow
    private void drawItemStack(ItemStack stack, int x, int y, String altText) {

    }


    /**
     * Draws an ItemStack.
     *
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStackBig(ItemStack stack, int x, int y, String altText) {

        this.setBlitOffset(200);
        this.itemRenderer.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.font;
        float scale = 1.4f;
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 0f);
        RenderSystem.translatef(-2.5F, -2.75F, 0.0F);
        RenderSystem.scalef(scale, scale, 1);

        enableGUIStandardItemLighting(scale);
        this.itemRenderer.renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        this.setBlitOffset(0);
        this.itemRenderer.zLevel = 0.0F;
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();

        RenderSystem.pushMatrix();
        this.setBlitOffset(200);
        this.itemRenderer.zLevel = 200.0F;
        //Append text
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.setBlitOffset(0);
        this.itemRenderer.zLevel = 0.0F;
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }

    private static void enableGUIStandardItemLighting(float scale) {
        RenderSystem.pushMatrix();
        RenderSystem.rotatef(-30.0F, 0.0F, 1.0F, 0.0F);
        RenderSystem.rotatef(165.0F, 1.0F, 0.0F, 0.0F);
        enableStandardItemLighting(scale);
        RenderSystem.popMatrix();
    }

    private static final Vector3d LIGHT0_POS = (new Vector3d(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();
    private static final Vector3d LIGHT1_POS = (new Vector3d(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();

    private static void enableStandardItemLighting(float scale) {
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        GlStateManager.light(16384, 4611, GlStateManager.getBuffer((float) LIGHT0_POS.x, (float) LIGHT0_POS.y, (float) LIGHT0_POS.z, 0.0f));
        float lightStrength = 0.3F * scale;
        GlStateManager.light(16384, 4609, GlStateManager.getBuffer(lightStrength, lightStrength, lightStrength, 1.0F));
        GlStateManager.light(16384, 4608, GlStateManager.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.light(16384, 4610, GlStateManager.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.light(16385, 4611, GlStateManager.getBuffer((float) LIGHT1_POS.x, (float) LIGHT1_POS.y, (float) LIGHT1_POS.z, 0.0f));
        GlStateManager.light(16385, 4609, GlStateManager.getBuffer(lightStrength, lightStrength, lightStrength, 1.0F));
        GlStateManager.light(16385, 4608, GlStateManager.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.light(16385, 4610, GlStateManager.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GlStateManager.shadeModel(7424);
        float ambientLightStrength = 0.4F;
        GlStateManager.lightModel(2899, GlStateManager.getBuffer(ambientLightStrength, ambientLightStrength, ambientLightStrength, 1.0F));
    }


    @Shadow
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {

    }

    @Shadow
    protected abstract void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y);

    @Shadow
    private void moveItems(MatrixStack matrixStack, Slot slot) {

    }


}
