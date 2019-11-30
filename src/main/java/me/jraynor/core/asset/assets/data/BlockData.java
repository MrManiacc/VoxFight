package me.jraynor.core.asset.assets.data;

import lombok.Getter;
import lombok.Setter;

public class BlockData implements AssetData {
    @Getter
    @Setter
    private String displayName;
    @Getter
    @Setter
    private short id;
    @Getter
    @Setter
    private float[] uvs;
    @Getter
    @Setter
    private float[] vertices;


}
