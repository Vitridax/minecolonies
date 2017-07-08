package com.minecolonies.coremod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockDecorative extends Block {
    public BlockDecorative(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.Crossed));
    }


    public static final PropertyEnum<com.minecolonies.coremod.blocks.EnumType> TYPE = PropertyEnum.create("type", BlockDecorative.EnumType.class);

    public enum EnumType implements com.minecolonies.coremod.blocks.EnumType {
        Crossed(0, "crossed"),
        Plain(1, "plain");

        private int ID;
        private String name;

        private EnumType(int ID, String name) {
            this.ID = ID;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getID() {
            return ID;
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        protected BlockState createBlockState() {
            return new BlockState(this, new IProperty[]{TYPE});
        }

        @Override
        public IBlockState getStateFromMeta(int meta) {
            return getDefaultState().withProperty(TYPE, meta == 0 ? EnumType.WHITE : EnumType.BLACK);
        }

        @Override
        public int getMetaFromState(IBlockState state) {
            com.minecolonies.coremod.blocks.EnumType type = state.getValue(TYPE);
            return type.getID();
        }

        @Override
        public int damageDropped(IBlockState state) {
            return getMetaFromState(state);
        }

        @Override
        public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
            list.add(new ItemStack(itemIn, 1, 0)); //Meta 0
            list.add(new ItemStack(itemIn, 1, 1)); //Meta 1
        }
    }
}


