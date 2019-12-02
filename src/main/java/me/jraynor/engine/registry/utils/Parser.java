package me.jraynor.engine.registry.utils;

import me.jraynor.engine.registry.assets.Asset;
import me.jraynor.engine.registry.assets.BlockAsset;
import me.jraynor.engine.registry.assets.FontAsset;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.jraynor.engine.registry.utils.Pack.BLOCK;
import static me.jraynor.engine.registry.utils.Pack.FONT;

public class Parser {
    private static final String root = "src/main/resources/";
    private static final Map<Pack, List<Asset>> loadedPack = new HashMap<>();

    public static void parse(String packName) {
        loadedPack.put(FONT, walk(root + packName, Pack.FONT));
        loadedPack.put(Pack.BLOCK, walk(root + packName, Pack.BLOCK));
        load();
    }


    private static List<Asset> walk(String path, Pack pack) {
        try (Stream<Path> walk = Files.walk(Paths.get(path + pack.getPath()))) {

            List<Path> result = walk
                    .filter(Path -> isValidAsset(pack, Path))
                    .collect(Collectors.toList());
            List<Asset> assets = new ArrayList<>();

            result.forEach(Path -> {
                try {
                    parseAsset(assets, pack, Path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return assets;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void load(){
        loadedPack.get(FONT).forEach(Asset::load);
        loadedPack.get(BLOCK).forEach(Asset::load);

    }

    private static boolean isValidAsset(Pack pack, Path path) {
        String extension = FilenameUtils.getExtension(path.toFile().getAbsolutePath());
        switch (pack) {
            case UI:
                return extension.equalsIgnoreCase("ui");
            case FONT:
                return extension.equalsIgnoreCase("ttf");
            case SHADER:
                return extension.equalsIgnoreCase("glsl");
            case BLOCK:
                return extension.equalsIgnoreCase("blk");
            case TEXTURE:
                if (extension.equalsIgnoreCase("png") ||
                        extension.equalsIgnoreCase("jpg") ||
                        extension.equalsIgnoreCase("jpeg"))
                    return true;
                return false;
            case MISC:
                return true;
        }
        return false;
    }

    private static void parseAsset(List<Asset> assets, Pack pack, Path path) throws IOException {
        File file = path.toFile();
        Asset asset = null;
        if (pack == FONT)
            asset = new FontAsset(file, pack.getName(),
                    FilenameUtils.removeExtension(file.getName()));
        else if (pack == BLOCK)
            asset = new BlockAsset(file, pack.getName(),
                    FilenameUtils.removeExtension(file.getName()));

        if (asset != null) {
            asset.parseAsset(file);
            assets.add(asset);
        }
    }
}
