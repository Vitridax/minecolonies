package com.minecolonies.coremod.entity.ai.mobs;

import com.minecolonies.api.configuration.Configurations;
import com.minecolonies.coremod.items.ItemChiefSword;
import com.minecolonies.coremod.sounds.BarbarianSounds;
import com.minecolonies.coremod.util.BarbarianSpawnUtils;
import com.minecolonies.coremod.util.BarbarianUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Abstract Class for barbarians
 */
public abstract class AbstractEntityBarbarian extends EntityMob
{
    private static final Potion SPEED_EFFECT                    = Potion.getPotionById(1);
    private static final int    TIME_TO_COUNTDOWN               = 240;
    private static final int    COUNTDOWN_SECOND_MULTIPLIER     = 4;
    private static final int    SPEED_EFFECT_DISTANCE           = 7;
    private static final int    SPEED_EFFECT_DURATION           = 160;
    private static final int    SPEED_EFFECT_MULTIPLIER         = 2;
    private static final int    BARBARIAN_HORDE_DIFFICULTY_FIVE = 5;

    private int currentCount = 0;

    /**
     * Values used to choose whether or not to play sound
     */
    private static final int OUT_OF_ONE_HUNDRED = 100;
    private static final int ONE                = 1;

    /**
     * Constructor method for our abstract class
     *
     * @param worldIn The world that the Barbarian is in
     */
    AbstractEntityBarbarian(final World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void initEntityAI()
    {
        BarbarianSpawnUtils.setBarbarianAITasks(this);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        BarbarianSpawnUtils.setBarbarianAttributes(this);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable()
    {
        return BarbarianSpawnUtils.getBarbarianLootTables(this);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata)
    {
        BarbarianSpawnUtils.setBarbarianEquipment(this);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    /**
     * We have loot_tables for a reason, this is done to disable equipped items dropping on death.
     *
     * @param wasRecentlyHit  Was the barbarian recently hit?
     * @param lootingModifier Was the barbarian hit with a sword with looting Enchantment?
     */
    @Override
    protected void dropEquipment(final boolean wasRecentlyHit, final int lootingModifier)
    {
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.getHeldItemMainhand().getItem() instanceof ItemChiefSword && Configurations.gameplay.barbarianHordeDifficulty >= BARBARIAN_HORDE_DIFFICULTY_FIVE && currentCount <= 0)
        {
            final Stream<EntityLivingBase> barbarians = BarbarianUtils.getBarbariansCloseToEntity(this, SPEED_EFFECT_DISTANCE);
            barbarians.forEach(entity -> entity.addPotionEffect(new PotionEffect(SPEED_EFFECT, SPEED_EFFECT_DURATION, SPEED_EFFECT_MULTIPLIER)));
            currentCount = COUNTDOWN_SECOND_MULTIPLIER * TIME_TO_COUNTDOWN;
        }
        else
        {
            --currentCount;
        }

        super.onLivingUpdate();
    }

    @Override
    protected SoundEvent getHurtSound(final DamageSource sourceOfDamage)
    {
        return BarbarianSounds.barbarianHurt;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return BarbarianSounds.barbarianDeath;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return BarbarianSounds.barbarianSay;
    }

    @Override
    public void playLivingSound()
    {
        final SoundEvent soundevent = this.getAmbientSound();

        if (soundevent != null && world.rand.nextInt(OUT_OF_ONE_HUNDRED) <= ONE)
        {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }
}
