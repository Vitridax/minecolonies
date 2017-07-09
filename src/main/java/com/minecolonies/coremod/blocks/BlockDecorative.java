package com.minecolonies.coremod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.List;

public class BlockDecorative extends Block {

    private static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

    BlockDecorative(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.CROSSED));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, meta == 0 ? EnumType.CROSSED : EnumType.PLAIN);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumType type = state.getValue(TYPE);
        return type.getID();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0)); //Meta 0
        list.add(new ItemStack(itemIn, 1, 1)); //Meta 1
    }

    public enum EnumType implements IStringSerializable {
        CROSSED(0, "crossed"),
        PLAIN(1, "plain");

        private int ID;
        private String name;

        EnumType(int ID, String name) {
            this.ID = ID;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }


        public int getID() {
            return ID;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}


