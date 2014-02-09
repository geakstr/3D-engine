package me.geakstr.engine.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import me.geakstr.engine.core.Game;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	public int id;
	public int width;
	public int height;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public static Texture loadTexture(String name) {
		name = Game.RES_DIR + "/textures/" + name;
		// Load the image
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(name));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to load Texture: " + name);
			Game.end();
		}

		int[] pixels = new int[bimg.getWidth() * bimg.getHeight()];
		bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0, bimg.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(bimg.getWidth() * bimg.getHeight() * 4);

		// Iterate through all the pixels and add them to the ByteBuffer
		for (int y = 0; y < bimg.getHeight(); y++) {
			for (int x = 0; x < bimg.getWidth(); x++) {
				// Select the pixel
				int pixel = pixels[y * bimg.getWidth() + x];
				// Add the RED component
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				// Add the GREEN component
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				// Add the BLUE component
				buffer.put((byte) (pixel & 0xFF));
				// Add the ALPHA component
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		buffer.flip();

		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(), bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		return new Texture(textureID, bimg.getWidth(), bimg.getHeight());
	}

}
