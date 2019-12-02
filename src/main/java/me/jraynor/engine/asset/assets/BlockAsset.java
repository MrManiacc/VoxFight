package me.jraynor.engine.asset.assets;

import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import lombok.Getter;
import me.jraynor.engine.asset.AssetMapper;
import me.jraynor.engine.asset.assets.data.BlockData;
import me.jraynor.engine.asset.urn.Urn;
import me.jraynor.engine.block.blocks.Block;
import me.jraynor.engine.registry.CoreRegistry;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static me.jraynor.engine.asset.AssetMapper.AssetType.BLOCK;

public class BlockAsset extends Asset<BlockData> {
    private Logger logger = LogManager.getLogger();
    @Getter
    private Block block;

    public BlockAsset(Urn urn) {
        super(urn);
    }

    @Override
    protected void doReload(Optional<BlockData> data) {
        AssetMapper mapper = CoreRegistry.get(AssetMapper.class);
        Path path = mapper.getAssetPath(BLOCK, urn);
//        try {
//            createBlock(new Yaml().load(Files.readString(path)));
//        } catch (IOException e) {
//            logger.error("Couldn't parse the BlockAsset at {}", path.toString());
//        }
    }

    private void createBlock(Map<String, Object> yamlMap) {

    }
}
