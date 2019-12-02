package me.jraynor.engine.registry.assets;

import me.jraynor.bootstrap.Window;
import me.jraynor.engine.registry.utils.Font;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class FontAsset extends Asset {
    private Font font;
    private ByteBuffer fontBuffer;

    public FontAsset(File file, String pack, String name) {
        super(file, pack, name);
    }

    @Override
    public void parseAsset(File file) throws IOException {
        this.font = Font.valueOf(getName().trim().toUpperCase());
        this.fontBuffer = ioResourceToByteBuffer(file.getAbsolutePath(), 512 * 1024);
    }

    @Override
    public void load() {
        switch (font) {
            case BOLD:
                Window.INSTANCE.setBoldFont(fontBuffer);
                break;
            case LIGHT:
                Window.INSTANCE.setLightFont(fontBuffer);
                break;
            case ITALIC:
                Window.INSTANCE.setItalic(fontBuffer);
                break;
            case REGULAR:
                Window.INSTANCE.setRegularFont(fontBuffer);
                break;
            case SEMIBOLD:
                Window.INSTANCE.setSemiBold(fontBuffer);
                break;
            case EXTRABOLD:
                Window.INSTANCE.setExtraBold(fontBuffer);
                break;
        }
        super.load();
    }

    private ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) {
                    ;
                }
            }
        } else {
            try (InputStream source = Window.class.getClassLoader().getResourceAsStream(resource);
                 ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    private ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
