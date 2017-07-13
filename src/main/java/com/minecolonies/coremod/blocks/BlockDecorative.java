package com.minecolonies.coremod.blocks;

import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.coremod.creativetab.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockDecorative extends Block {

    private static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

    /**
     * This blocks name.
     */
    private static final String BLOCK_NAME = "blockDecorative";

    BlockDecorative() {
        super(Material.WOOD);
        setRegistryName(BLOCK_NAME);
        setUnlocalizedName(String.format("%s.%s", Constants.MOD_ID.toLowerCase(), BLOCK_NAME));
        setCreativeTab(ModCreativeTabs.MINECOLONIES);
        GameRegistry.register(this);
        GameRegistry.register((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.CROSSED));
    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return (this.getUnlocalizedName() + "." + EnumType.CROSSED.getUnlocalizedName() + ".name");
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, meta == 0 ? EnumType.CROSSED : EnumType.PLAIN);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        EnumType type = (EnumType) state.getValue(TYPE);
        return type.getMetadata();
    }

    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, IBlockState state, final EntityLivingBase placer, final ItemStack stack)
    {
        state = state.withProperty(TYPE, EnumType.byMetadata(stack.getItemDamage()));

        worldIn.setBlockState(pos, state, 2);

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, EnumType.CROSSED.getMetadata()));
        list.add(new ItemStack(this, 1, EnumType.PLAIN.getMetadata()));
    }

    @NotNull
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        EnumType type = state.getValue(TYPE);

        drops.add(new ItemStack(this, 1, type.getMetadata()));

        return drops;
    }

    @NotNull
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        EnumType type = state.getValue(TYPE);

        return new ItemStack(this, 1, type.getMetadata());
    }

    public enum EnumType implements IStringSerializable {
        CROSSED(0, "crossed"),
        PLAIN(1, "plain");

        /** Array of the Block's BlockStates */
        private static final BlockDecorative.EnumType[] META_LOOKUP = new BlockDecorative.EnumType[values().length];
        /** The BlockState's metadata. */
        private final int meta;
        /** The EnumType's name. */
        private final String name;
        private final String unlocalizedName;

        EnumType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = name;
        }

        /**
         * Returns the EnumType's metadata value.
         */
        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        /**
         * Returns an EnumType for the BlockState from a metadata value.
         */
        public static BlockDecorative.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            for (BlockDecorative.EnumType blockdecorative$enumtype : values())
            {
                META_LOOKUP[blockdecorative$enumtype.getMetadata()] = blockdecorative$enumtype;
            }
        }
    }
}


