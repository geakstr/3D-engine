package me.geakstr.engine.render;

import me.geakstr.engine.core.Shader;
import me.geakstr.engine.core.Transform;
import me.geakstr.engine.core.World;
import me.geakstr.engine.math.Vector2f;
import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.model.Cube;
import me.geakstr.engine.model.Face;
import me.geakstr.engine.model.Material;
import me.geakstr.engine.model.Point;
import me.geakstr.engine.model.ResourceBuffer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;

public class RenderCube {
    public static int indexSize;

    public static void prepare() {
        boolean isTextured = ResourceBuffer.getTextured(World.getCubeId());
        List<Float> vertexArray = new ArrayList<Float>();
        List<Float> normalArray = new ArrayList<Float>();
        List<Float> colorArray = new ArrayList<Float>();
        List<Float> textureArray = new ArrayList<Float>();
        indexSize = 0;
        
        for (int x = 0; x < World.getLength(); x++) {
            for (int y = 0; y < World.getHeight(); y++) {
                for (int z = 0; z < World.getWidth(); z++) {
                	
                	Cube.Type[] sides = World.isRenderableSides(World.get(x, y, z), x, y, z);
                	if (sides == null) continue;
                	
                	//for (Face face : ResourceBuffer.getFaces(World.getCubeId())) {
                	//for (Cube.Type side : sides) {
	                	for (int i = 1; i < 8; i += 6) {
	                		Face face = ResourceBuffer.getFaces(World.getCubeId()).get(i);
	                        Material material = face.getMaterial();
	
	                        Vector3f v1 = ResourceBuffer.getVertices(World.getCubeId()).get((int) face.getVertex().x - 1);
	                        vertexArray.add(v1.x + x);
	                        vertexArray.add(v1.y + y);
	                        vertexArray.add(v1.z + z);
	
	                        Vector3f v2 = ResourceBuffer.getVertices(World.getCubeId()).get((int) face.getVertex().y - 1);
	                        vertexArray.add(v2.x + x);
	                        vertexArray.add(v2.y + y);
	                        vertexArray.add(v2.z + z);
	
	                        Vector3f v3 = ResourceBuffer.getVertices(World.getCubeId()).get((int) face.getVertex().z - 1);
	                        vertexArray.add(v3.x + x);
	                        vertexArray.add(v3.y + y);
	                        vertexArray.add(v3.z + z);
	
	                        Vector3f n1 = ResourceBuffer.getNormals(World.getCubeId()).get((int) face.getNormal().x - 1);
	                        normalArray.add(n1.x);
	                        normalArray.add(n1.y);
	                        normalArray.add(n1.z);
	
	                        Vector3f n2 = ResourceBuffer.getNormals(World.getCubeId()).get((int) face.getNormal().y - 1);
	                        normalArray.add(n2.x);
	                        normalArray.add(n2.y);
	                        normalArray.add(n2.z);
	
	                        Vector3f n3 = ResourceBuffer.getNormals(World.getCubeId()).get((int) face.getNormal().z - 1);
	                        normalArray.add(n3.x);
	                        normalArray.add(n3.y);
	                        normalArray.add(n3.z);
	
	                        colorArray.add(material.getDiffuse().x);
	                        colorArray.add(material.getDiffuse().y);
	                        colorArray.add(material.getDiffuse().z);
	                        colorArray.add(material.getDiffuse().x);
	                        colorArray.add(material.getDiffuse().y);
	                        colorArray.add(material.getDiffuse().z);
	                        colorArray.add(material.getDiffuse().x);
	                        colorArray.add(material.getDiffuse().y);
	                        colorArray.add(material.getDiffuse().z);
	
	                        if (isTextured) {
	                            Vector2f t1 = ResourceBuffer.getTexCoords(World.getCubeId()).get((int) face.getTexCoord().x - 1);
	                            textureArray.add(t1.x);
	                            textureArray.add(1 - t1.y);
	
	                            Vector2f t2 = ResourceBuffer.getTexCoords(World.getCubeId()).get((int) face.getTexCoord().y - 1);
	                            textureArray.add(t2.x);
	                            textureArray.add(1 - t2.y);
	
	                            Vector2f t3 = ResourceBuffer.getTexCoords(World.getCubeId()).get((int) face.getTexCoord().z - 1);
	                            textureArray.add(t3.x);
	                            textureArray.add(1 - t3.y);
	                        }
	                        indexSize += 3;
	                    }
                	//}
                }
            }
        }

        int size = vertexArray.size();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9 * size);
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9 * size);
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(9 * size);
        FloatBuffer textureBuffer = null;
        if (isTextured) textureBuffer = BufferUtils.createFloatBuffer(6 * size);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexSize);

        for (int i = 0; i < indexSize; i++) {
            indexBuffer.put(i);
        }

        for (int i = 0; i < size; i++) {
            vertexBuffer.put(vertexArray.get(i));
            normalBuffer.put(normalArray.get(i));
            colorBuffer.put(colorArray.get(i));
        }

        if (isTextured) {
            for (Float pos : textureArray) {
                textureBuffer.put(pos);
            }
        }

        indexBuffer.rewind();
        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer.rewind();
        if (isTextured) textureBuffer.rewind();

        int vboIndexID = glGenBuffers();
        int vboVertexID = glGenBuffers();
        int vboNormalID = glGenBuffers();
        int vboColorID = glGenBuffers();
        int vboTextureID = isTextured ? glGenBuffers() : -1;


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboNormalID);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        if (isTextured) {
            glBindBuffer(GL_ARRAY_BUFFER, vboTextureID);
            glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }


        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        if (isTextured) {
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glBindTexture(GL_TEXTURE_2D, ResourceBuffer.getTexturesID(World.getCubeId()));
            glBindBuffer(GL_ARRAY_BUFFER, vboTextureID);
            glTexCoordPointer(2, GL_FLOAT, 0, 0);
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboNormalID);
        glNormalPointer(GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glColorPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexID);
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
