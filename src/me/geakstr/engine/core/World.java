package me.geakstr.engine.core;

import me.geakstr.engine.math.Vector2f;
import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.model.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

public class World {
    // world[length][height][width]; world[x][y][z]
    private static int[][][] world;

    private static int length, width, height;

    private static int cubeId;

    private World() {}

    public static void init(int _length, int _height, int _width, int _cubeId) {
        length = _length;
        height = _height;
        width = _width;
        cubeId = _cubeId;
        world = new int[length][height][width];
    }

    public static void gen() {
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                for (int y = 0; y < height; y++) {
                    setCube(x, y, z);
                }
            }
        }
    }

//    public void render(Frustum frustum, Shader shader) {
//        RenderEngine.start(cubeId);
//
//        for (int x = 0; x < length; x++) {
//            for (int z = 0; z < width; z++) {
//                for (int y = 0; y < height; y++) {
//                    int id = get(x, y, z);
//                    if (isRenderableCube(id, x, y, z, frustum)) {
//                        RenderEngine.render(x, y, z, shader, new Cube.Type[]{ Cube.Type.TOP });
//                    }
//                }
//            }
//        }
//        RenderEngine.end(cubeId);
//    }

    private static int size;

    public static void prepare() {
        Set<Point> renderableCubes = new LinkedHashSet<Point>();
        for (int x = 0; x < length; x++)
            for (int y = 0; y < height; y++)
                for (int z = 0; z < width; z++)
                    if (isRenderableCube(get(x, y, z), x, y, z))
                        renderableCubes.add(new Point(x, y, z));



        boolean isTextured = ResourceBuffer.getTextured(cubeId);
        List<Float> vertexArray = new ArrayList<Float>();
        List<Float> normalArray = new ArrayList<Float>();
        List<Float> colorArray = new ArrayList<Float>();
        List<Float> textureArray = new ArrayList<Float>();

        for (Point coord : renderableCubes) {
            int x = coord.x();
            int y = coord.y();
            int z = coord.z();

            for (Face face : ResourceBuffer.getFaces(cubeId)) {
                Material material = face.getMaterial();

                // Get the first vertex of the face
                Vector3f v1 = ResourceBuffer.getVertices(cubeId).get((int) face.getVertex().x - 1);
                vertexArray.add(v1.x + x);
                vertexArray.add(v1.y + y);
                vertexArray.add(v1.z + z);

                // Get the second vertex of the face
                Vector3f v2 = ResourceBuffer.getVertices(cubeId).get((int) face.getVertex().y - 1);
                vertexArray.add(v2.x + x);
                vertexArray.add(v2.y + y);
                vertexArray.add(v2.z + z);

                // Get the third vertex of the face
                Vector3f v3 = ResourceBuffer.getVertices(cubeId).get((int) face.getVertex().z - 1);
                vertexArray.add(v3.x + x);
                vertexArray.add(v3.y + y);
                vertexArray.add(v3.z + z);

                // Get the first normal of the face
                Vector3f n1 = ResourceBuffer.getNormals(cubeId).get((int) face.getNormal().x - 1);
                normalArray.add(n1.x);
                normalArray.add(n1.y);
                normalArray.add(n1.z);

                // Get the second normal of the face
                Vector3f n2 = ResourceBuffer.getNormals(cubeId).get((int) face.getNormal().y - 1);
                normalArray.add(n2.x);
                normalArray.add(n2.y);
                normalArray.add(n2.z);

                // Get the third normal of the face
                Vector3f n3 = ResourceBuffer.getNormals(cubeId).get((int) face.getNormal().z - 1);
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

//                colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);
//                colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);
//                colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);

                if (isTextured) {
                    // Get the first texCoords of the face
                    Vector2f t1 = ResourceBuffer.getTexCoords(cubeId).get((int) face.getTexCoord().x - 1);
                    textureArray.add(t1.x);
                    textureArray.add(1 - t1.y);
                    //textureBuffer.put(t1.x).put(1 - t1.y);

                    // Get the second texCoords of the face
                    Vector2f t2 = ResourceBuffer.getTexCoords(cubeId).get((int) face.getTexCoord().y - 1);
                    textureArray.add(t2.x);
                    textureArray.add(1 - t2.y);
                    //textureBuffer.put(t2.x).put(1 - t2.y);

                    // Get the third texCoords of the face
                    Vector2f t3 = ResourceBuffer.getTexCoords(cubeId).get((int) face.getTexCoord().z - 1);
                    textureArray.add(t3.x);
                    textureArray.add(1 - t3.y);
                    //textureBuffer.put(t3.x).put(1 - t3.y);
                }
            }
        }



        size = vertexArray.size();
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9 * size);
        FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9 * size);
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(9 * size);
        FloatBuffer textureBuffer = null;
        if (isTextured) textureBuffer = BufferUtils.createFloatBuffer(6 * size);

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

        vertexBuffer.rewind();
        normalBuffer.rewind();
        colorBuffer.rewind();
        if (isTextured) textureBuffer.rewind();

        int vboVertexID = glGenBuffers();
        int vboNormalID = glGenBuffers();
        int vboColorID = glGenBuffers();
        int vboTextureID = isTextured ? glGenBuffers() : -1;

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
            glBindTexture(GL_TEXTURE_2D, ResourceBuffer.getTexturesID(cubeId));
            glBindBuffer(GL_ARRAY_BUFFER, vboTextureID);
            glTexCoordPointer(2, GL_FLOAT, 0, 0);
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboNormalID);
        glNormalPointer(GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
        glColorPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexID);
        glVertexPointer(3, GL_FLOAT, 0, 0);
    }

    public static void render(Shader shader) {
        Transform transform = new Transform();
        shader.setUniform("mModelTransform", transform.getTransform());

        glDrawArrays(GL_TRIANGLES, 0, size);
    }

    public static void finish() {
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public static boolean isRenderableCube(int id, int x, int y, int z, Frustum frustum) {
        return id == cubeId && !isSurrounded(x, y, z) && frustum.checkCube(x, y, z, 1) >= 1;
    }

    public static boolean isRenderableCube(int id, int x, int y, int z) {
        return id == cubeId && !isSurrounded(x, y, z);
    }

    public static boolean isSurrounded(int x, int y, int z) {
        if (x == length - 1 || y == height - 1 || z == width - 1 || x == 0 || y == 0 || z == 0)
            return false;
        if (world[x - 1][y][z] != 0 && world[x + 1][y][z] != 0)
            if (world[x][y - 1][z] != 0 && world[x][y + 1][z] != 0)
                if (world[x][y][z - 1] != 0 && world[x][y][z + 1] != 0)
                    return true;
        return false;
    }

    public static void set(int id, int x, int y, int z) {
        world[x][y][z] = id;
    }

    public static void setCube(int x, int y, int z) {
        set(cubeId, x, y, z);
    }

    public static void setAir(int x, int y, int z) {
        set(0, x, y, z);
    }

    public static int get(int x, int y, int z) {
        return world[x][y][z];
    }

    public static int[][][] getWorld() {
        return world;
    }

    public static int getLength() {
        return length;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getCubeId() { return cubeId; }
}
