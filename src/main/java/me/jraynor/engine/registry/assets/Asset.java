package me.jraynor.engine.registry.assets;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class Asset {
    @Getter
    private File file;
    @Getter
    protected String name;
    @Getter
    private String pack;
    @Getter
    private boolean loaded;

    public Asset(File file, String pack, String name) {
        this.file = file;
        this.name = name;
        this.pack = pack;
        try {
            parseAsset(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        loaded = true;
    }

    public abstract void parseAsset(File file) throws IOException;

    public String parseFile() throws IOException {
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    public String getName() {
        return FilenameUtils.removeExtension(file.getName());
    }
}
