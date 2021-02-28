package com.rosesause.bedrockparity.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BabyWaterMobEntity extends AgeableEntity {

    protected EntityType<?> adult;

    protected BabyWaterMobEntity(EntityType<? extends BabyWaterMobEntity> type, EntityType<?> adultType, World p_i48565_2_) {
        super(type, p_i48565_2_);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        adult = adultType;
    }

    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableEntity.AgeableData(true);
        }

        AgeableEntity.AgeableData ageableentity$ageabledata = (AgeableEntity.AgeableData)spawnDataIn;
        if (ageableentity$ageabledata.canBabySpawn() && ageableentity$ageabledata.getIndexInGroup() > 0) {
            this.setGrowingAge(-24000);
        }
        else
        {

        }

        this.getAttribute(Attributes.FOLLOW_RANGE).applyPersistentModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
        if (this.rand.nextFloat() < 0.05F) {
            this.setLeftHanded(true);
        } else {
            this.setLeftHanded(false);
        }
        ageableentity$ageabledata.incrementIndexInGroup();
        return spawnDataIn;
    }

    @Override
    protected void onGrowingAdult() {
        Entity adultEntity = adult.create(this.world);
        world.addEntity(adultEntity);
        remove();
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.WATER;
    }

    public boolean isNotColliding(IWorldReader worldIn) {
        return worldIn.checkNoEntityCollision(this);
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval() {
        return 120;
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(PlayerEntity player) {
        return 1 + this.world.rand.nextInt(3);
    }

    protected void updateAir(int p_209207_1_) {
        if (this.isAlive() && !this.isInWaterOrBubbleColumn()) {
            this.setAir(p_209207_1_ - 1);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }

    }

    /**
     * Gets called every tick from main Entity class
     */
    public void baseTick() {
        int i = this.getAir();
        super.baseTick();
        this.updateAir(i);
    }

    public boolean isPushedByWater() {
        return false;
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }
}
