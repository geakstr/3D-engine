package me.geakstr.engine.render;

import me.geakstr.engine.core.World;
import me.geakstr.engine.model.Cube.Cube;
import me.geakstr.engine.model.ResourceBuffer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class RenderCube {
    public static int indexSize;

    public static void buffering() {
        List<Float> vertices = new ArrayList<Float>();
        List<Float> normals = new ArrayList<Float>();
        List<Float> texCoords = new ArrayList<Float>();

        // Generate mesh
        indexSize = 0;
        for (int x = 0; x < World.getLength(); x++) {
            for (int y = 0; y < World.getHeight(); y++) {
                for (int z = 0; z < World.getWidth(); z++) {
                    Cube.Side[] sides = World.isRenderableSides(World.get(x, y, z), x, y, z);
                    if (sides == null) continue;

                    for (Cube.Side side : sides) {
	                    for (int i = 0; i <side.vertices().length; i += 3) {
	                        vertices.add(side.vertices()[i] + x);
	                        vertices.add(side.vertices()[i + 1] + y);
	                        vertices.add(side.vertices()[i + 2] + z);
	                    }
	                    for (int k = 0; k < 6; k++) {
                            for (int i = 0; i < side.normals().length; i++) {
	                            normals.add(side.normals()[i]);
	                        }
	                    }
	                    for (int i = 0; i < side.texCoords().length; i += 2) {
	                    	texCoords.add(side.texCoords()[i]);
	                    	texCoords.add(1 - side.texCoords()[i + 1]);
	                    }
	                    indexSize++;
                    }
                }
            }
        }
        indexSize *= 6;

        // Fill buffers
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexSize);
        for (int i = 0; i < indexSize; i++) indexBuffer.put(i);

        int size = vertices.size();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(size);
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(size);
        for (int i = 0; i < size; i++) {
        	vertexBuffer.put(vertices.get(i));
        	normalBuffer.put(normals.get(i));
        }

        size /= 1.5;
        FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(size);
        for (int i = 0; i < size; i++) textureBuffer.put(texCoords.get(i));

        // Flip buffers
        indexBuffer.rewind();
        vertexBuffer.rewind();
        normalBuffer.rewind();
        textureBuffer.rewind();


        // Gen ?BO
        final int ibo = glGenBuffers();
        final int vbo = glGenBuffers();
        final int nbo = glGenBuffers();
        final int tbo = glGenBuffers();


        // Assign data to buffers
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


        // Bind buffers
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindTexture(GL_TEXTURE_2D, ResourceBuffer.getTexturesID(World.getCubeId()));
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glNormalPointer(GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    }

    public static void render() {
        glDrawElements(GL_TRIANGLES, indexSize, GL_UNSIGNED_INT, 0);
    }

    public static void dispose() {
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }
}