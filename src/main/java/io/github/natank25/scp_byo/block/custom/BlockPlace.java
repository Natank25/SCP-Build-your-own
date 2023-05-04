package io.github.natank25.scp_byo.block.custom;

import net.minecraft.util.StringIdentifiable;

public enum BlockPlace implements StringIdentifiable {
    UPPER,
    LOWER,
    BETWEEN;

    public String toString() {
        return this.asString();
    }

    @Override
    public String asString() {
        if(this == UPPER){
            return "upper";
        }else if(this == LOWER){
            return "lower";
        }else if(this == BETWEEN){
            return "between";
        }
        return null;
    }

}
