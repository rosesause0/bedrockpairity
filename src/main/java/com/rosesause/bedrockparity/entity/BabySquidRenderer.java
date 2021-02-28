package com.rosesause.bedrockparity.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SquidModel;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class BabySquidRenderer extends MobRenderer<BabySquidEntity, SquidModel<BabySquidEntity>> {
    private static final ResourceLocation SQUID_TEXTURES = new ResourceLocation("textures/entity/squid.png");

    public BabySquidRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SquidModel<>(), 0.7F);
    }

    @Override
    public void render(BabySquidEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        //if(entityModel.isChild)
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getEntityTexture(BabySquidEntity entity) {
        return SQUID_TEXTURES;
    }

    protected void applyRotations(BabySquidEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, entityLiving.prevSquidPitch, entityLiving.squidPitch);
        float f1 = MathHelper.lerp(partialTicks, entityLiving.prevSquidYaw, entityLiving.squidYaw);
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
        matrixStackIn.translate(0.0D, -1.2F, 0.0D);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(BabySquidEntity livingBase, float partialTicks) {
        return MathHelper.lerp(partialTicks, livingBase.lastTentacleAngle, livingBase.tentacleAngle);
    }
}
