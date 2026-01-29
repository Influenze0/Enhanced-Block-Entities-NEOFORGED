package foundationgames.enhancedblockentities.util.hacks;

import com.mojang.blaze3d.platform.NativeImage;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;

public enum TextureHacks {
    ;

    public static Optional<byte[]> cropImage(@Nullable InputStream image, float u0, float v0, float u1, float v1) throws IOException {
        byte[] r = new byte[0];
        if (image != null) {
            try (NativeImage src = NativeImage.read(NativeImage.Format.RGBA, image)) {

                int w = src.getWidth();
                int h = src.getHeight();
                int x = (int) Math.floor(u0 * w);
                int y = (int) Math.floor(v0 * h);
                int sw = (int) Math.floor((u1 - u0) * w);
                int sh = (int) Math.floor((v1 - v0) * h);

                try (NativeImage prod = new NativeImage(NativeImage.Format.RGBA, sw, sh, false)) {
                    for (int u = 0; u < sw; u++) {
                        for (int v = 0; v < sh; v++) {
                            prod.setPixel(u, v, src.getPixel(x + u, y + v));
                        }
                    }

                    r = imageToByteArray(prod);
                }
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
        return Optional.of(r);
    }

    private static byte[] imageToByteArray(NativeImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (WritableByteChannel channel = Channels.newChannel(baos)) {
            if (!image.writeToChannel(channel)) {
                throw new IOException("Failed to write image to byte array");
            }
        }

        return baos.toByteArray();
    }
}