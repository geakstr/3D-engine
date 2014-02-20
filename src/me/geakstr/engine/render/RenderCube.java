package me.geakstr.engine.render;

import me.geakstr.engine.core.Shader;
import me.geakstr.engine.core.Transform;
import me.geakstr.engine.core.World;
import me.geakstr.engine.model.Cube.Cube;
import me.geakstr.engine.model.Cube.CubeType;
import me.geakstr.engine.model.ResourceBuffer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;

public class RenderCube {
    public static int indexSize;

    public static void prepare() {
        List<Float> vertices = new ArrayList<Float>();

        // Generate mesh
        indexSize = 0;
        for (int x = 0; x < World.getLength(); x++) {
            for (int y = 0; y < World.getHeight(); y++) {
                for (int z = 0; z < World.getWidth(); z++) {
                    CubeType[] sides = World.isRenderableSides(World.get(x, y, z), x, y, z);
                    if (sides == null) continue;

                    for (int i = 0; i < CubeType.TOP.vertices().length; i += 3) {
                        vertices.add(CubeType.TOP.vertices()[i] + x);
                        vertices.add(CubeType.TOP.vertices()[i + 1] + y);
                        vertices.add(CubeType.TOP.vertices()[i + 2] + z);
                    }
                    indexSize++;
                }
            }
        }
        
        // Fill buffers
        int size = vertices.size();
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(size);
        for (int i = 0; i < size; i++) vertexBuffer.put(vertices.get(i));

        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(size);
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(size);
        FloatBuffer textureBuffer = BufferUtils.createFloatBuffer((int) (size / 1.5));
        for (int i = 0; i < indexSize; i++) {
        	for (int k = 0; k < 6; k++) {
                for (int j = 0; j < 3; j++) {
                    normalBuffer.put(Cube.normals[j]);
                    colorBuffer.put(Cube.diffuse[j]);
                }
            }
        	for (int j = 0; j < CubeType.TOP.texCoords().length; j += 2) {
        		textureBuffer.put(CubeType.TOP.texCoords()[j]);
            	textureBuffer.put(1 - CubeType.TOP.texCoords()[j + 1]);
            }
        }

        indexSize *= 6;
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexSize);
        for (int i = 0; i < indexSize; i++) indexBuffer.put(i);


        // Flip buffers
        indexBuffer.rewind();
        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer.rewind();
        textureBuffer.rewind();


        // Gen buffers
        final int vboIndex = glGenBuffers();
        final int vboVertex = glGenBuffers();
        final int vboNormal = glGenBuffers();
        final int vboColor = glGenBuffers();
        final int vboTexture = glGenBuffers();


        // Assign data to buffers
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndex);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboNormal);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboTexture);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


        // Bind buffers
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glBindTexture(GL_TEXTURE_2D, ResourceBuffer.getTexturesID(World.getCubeId()));
        glBindBuffer(GL_ARRAY_BUFFER, vboTexture);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboNormal);
        glNormalPointer(GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glColorPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndex);
    }

    public static void render(Shader shader) {
        Transform transform = new Transform();
        shader.setUniform("mModelTransform", transform.getTransform());

        glDrawElementsBaseVertex(GL_TRIANGLES, indexSize, GL_UNSIGNED_INT, 0, 0);
    }

    public static void dispose() {
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}