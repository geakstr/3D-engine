package me.geakstr.engine.model;

import me.geakstr.engine.core.Game;
import me.geakstr.engine.math.Vector2f;
import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.utils.FileUtil;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;


public class ResourceBuffer {
    private static int modelsCount = 0;

    private static Map<String, Model> models = new HashMap<String, Model>();
    private static Map<Integer, Texture> textures = new HashMap<Integer, Texture>();
    private static Set<String> loadedTextures = new HashSet<String>();

    private static Map<Integer, List<Vector3f>> vertices = new HashMap<Integer, List<Vector3f>>();
    private static Map<Integer, List<Vector3f>> normals = new HashMap<Integer, List<Vector3f>>();
    private static Map<Integer, List<Vector2f>> texCoords = new HashMap<Integer, List<Vector2f>>();
    private static Map<Integer, List<Face>> faces = new HashMap<Integer, List<Face>>();
    private static Map<Integer, Boolean> textured = new HashMap<Integer, Boolean>();
    private static Map<Integer, Map<String, Material>> materials = new HashMap<Integer, Map<String, Material>>();
    private static Map<Integer, Integer> texturesID = new HashMap<Integer, Integer>();

    private static Map<Integer, Integer> vboIndexSize = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> vboIndexID = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> vboVertexID = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> vboNormalID = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> vboColorID = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> vboTextureID = new HashMap<Integer, Integer>();

    public static void loadModels(String... modelsNames) {
        for (String name : modelsNames) {
            ++modelsCount;

            int id = modelsCount;

            Model model = new Model(id);
            Material material = null;

            vertices.put(id, new ArrayList<Vector3f>());
            normals.put(id, new ArrayList<Vector3f>());
            texCoords.put(id, new ArrayList<Vector2f>());
            textured.put(id, false);
            faces.put(id, new ArrayList<Face>());
            materials.put(id, new HashMap<String, Material>());

            String path = Game.RES_DIR + "/models/" + name;

            for (String line : FileUtil.readAllLines(path)) {
                String[] tokens = FileUtil.removeEmptyString(line.split(" "));
                if (tokens.length == 0 || tokens[0].equals("#")) continue;

                // Vertex
                if (line.startsWith("v ")) {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);
                    float z = Float.parseFloat(tokens[3]);
                    vertices.get(id).add(new Vector3f(x, y, z));
                }
                // Vertex normal
                else if (line.startsWith("vn ")) {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);
                    float z = Float.parseFloat(tokens[3]);
                    normals.get(id).add(new Vector3f(x, y, z));
                }
                // Texture coord
                else if (line.startsWith("vt ")) {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);
                    texCoords.get(id).add(new Vector2f(x, y));
                    textured.put(id, true);
                }
                // Face
                else if (line.startsWith("f ")) {
                    float v1 = Float.parseFloat(tokens[1].split("/")[0]);
                    float v2 = Float.parseFloat(tokens[2].split("/")[0]);
                    float v3 = Float.parseFloat(tokens[3].split("/")[0]);
                    Vector3f vertex = new Vector3f(v1, v2, v3);

                    float vn1 = Float.parseFloat(tokens[1].split("/")[2]);
                    float vn2 = Float.parseFloat(tokens[2].split("/")[2]);
                    float vn3 = Float.parseFloat(tokens[3].split("/")[2]);
                    Vector3f normal = new Vector3f(vn1, vn2, vn3);

                    Vector3f texCoords = null;
                    if (textured.get(id)) {
                        float vt1 = Float.parseFloat(tokens[1].split("/")[1]);
                        float vt2 = Float.parseFloat(tokens[2].split("/")[1]);
                        float vt3 = Float.parseFloat(tokens[3].split("/")[1]);
                        texCoords = new Vector3f(vt1, vt2, vt3);
                    }
                    faces.get(id).add(new Face(vertex, normal, texCoords, material));
                }
                // Material definitions
                else if (line.startsWith("mtllib ")) {
                    parseMaterial(id, path, line.replaceAll("mtllib ", "").trim());
                }
                // Material to use for upcoming faces
                else if (line.startsWith("usemtl ")) {
                    material = materials.get(id).get(line.replaceAll("usemtl ", "").trim());
                }
            }
            models.put(name, model);
        }
        loadBuffers();
    }

    public static void loadBuffers() {
        for (Model model : models.values()) {
            int id = model.getId();
            int curVboIndexID = glGenBuffers();
            int curVboVertexID = glGenBuffers();
            int curVboNormalID = glGenBuffers();
            int curVboColorID = glGenBuffers();

            vboIndexID.put(id, curVboIndexID);
            vboVertexID.put(id, curVboVertexID);
            vboNormalID.put(id, curVboNormalID);
            vboColorID.put(id, curVboColorID);

            int curVboTextureID = -1;
            boolean isTextured = textured.get(id);
            if (isTextured) {
                curVboTextureID = glGenBuffers();
                vboTextureID.put(id, curVboTextureID);
            }

            int size = faces.get(id).size();
            FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(9 * size);
            FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9 * size);
            FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(9 * size);
            IntBuffer indexBuffer = BufferUtils.createIntBuffer(9 * size);
            FloatBuffer textureBuffer = null;
            if (isTextured) textureBuffer = BufferUtils.createFloatBuffer(6 * size);

            int indexSize = 0;
            for (Face face : faces.get(id)) {
                Material material = face.getMaterial();

                // Get the first vertex of the face
                Vector3f v1 = vertices.get(id).get((int) face.getVertex().x - 1);
                vertexBuffer.put(v1.x).put(v1.y).put(v1.z);

                // Get the second vertex of the face
                Vector3f v2 = vertices.get(id).get((int) face.getVertex().y - 1);
                vertexBuffer.put(v2.x).put(v2.y).put(v2.z);

                // Get the third vertex of the face
                Vector3f v3 = vertices.get(id).get((int) face.getVertex().z - 1);
                vertexBuffer.put(v3.x).put(v3.y).put(v3.z);

                // Get the first normal of the face
                Vector3f n1 = normals.get(id).get((int) face.getNormal().x - 1);
                normalBuffer.put(n1.x).put(n1.y).put(n1.z);

                // Get the second normal of the face
                Vector3f n2 = normals.get(id).get((int) face.getNormal().y - 1);
                normalBuffer.put(n2.x).put(n2.y).put(n2.z);

                // Get the third normal of the face
                Vector3f n3 = normals.get(id).get((int) face.getNormal().z - 1);
                normalBuffer.put(n3.x).put(n3.y).put(n3.z);

                colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);
                colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);
                colorBuffer.put(material.getDiffuse().x).put(material.getDiffuse().y).put(material.getDiffuse().z);

                if (isTextured) {
                    // Get the first texCoords of the face
                    Vector2f t1 = texCoords.get(id).get((int) face.getTexCoord().x - 1);
                    textureBuffer.put(t1.x).put(1 - t1.y);

                    // Get the second texCoords of the face
                    Vector2f t2 = texCoords.get(id).get((int) face.getTexCoord().y - 1);
                    textureBuffer.put(t2.x).put(1 - t2.y);

                    // Get the third texCoords of the face
                    Vector2f t3 = texCoords.get(id).get((int) face.getTexCoord().z - 1);
                    textureBuffer.put(t3.x).put(1 - t3.y);
                }
                indexBuffer.put(indexSize++);
                indexBuffer.put(indexSize++);
                indexBuffer.put(indexSize++);
            }
            vboIndexSize.put(id, indexSize);
            vertexBuffer.rewind();
            normalBuffer.rewind();
            colorBuffer.rewind();
            indexBuffer.rewind();
            if (isTextured) textureBuffer.rewind();


            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, curVboIndexID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            // Bind buffers
            glBindBuffer(GL_ARRAY_BUFFER, curVboVertexID);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindBuffer(GL_ARRAY_BUFFER, curVboNormalID);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindBuffer(GL_ARRAY_BUFFER, curVboColorID);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            if (isTextured) {
                glBindBuffer(GL_ARRAY_BUFFER, curVboTextureID);
                glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }
        }
    }

    private static Texture loadTexture(String name) {
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

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(), bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return new Texture(textureID, bimg.getWidth(), bimg.getHeight());
    }

    private static void parseMaterial(int id, String path, String name) {
        path = FileUtil.getFileInSameLevelOf(path, name);

        if (loadedTextures.contains(path)) return;
        loadedTextures.add(path);

        Material material = null;
        for (String line : FileUtil.readAllLines(path)) {
            if (line.startsWith("newmtl ")) {
                if (material != null) {
                    materials.get(id).put(material.getName(), material);
                }
                material = new Material(FileUtil.removeEmptyString(line.split(" ", 2))[1]);
            } else if (line.startsWith("Kd ")) {
                String[] tokens = FileUtil.removeEmptyString(line.split(" "));

                float r = Float.parseFloat(tokens[1]);
                float g = Float.parseFloat(tokens[2]);
                float b = Float.parseFloat(tokens[3]);

                material.setDiffuse(new Vector3f(r, g, b));
            } else if (line.startsWith("map_Kd ")) {
                Texture texture = loadTexture(line.replaceAll("map_Kd", "").trim());
                texturesID.put(id, texture.id);
                textures.put(texture.id, texture);
            }
        }
        materials.get(id).put(material.getName(), material);
    }

    public static Map<String, Model> getModels() {


        return models;
    }

    public static int getTexturesID(int id) {
        return texturesID.get(id);
    }

    public static List<Vector3f> getVertices(int id) {
        return vertices.get(id);
    }

    public static List<Face> getFaces(int id) {
        return faces.get(id);
    }

    public static Texture getTexture(int id) {
        return textures.get(id);
    }

    public static boolean getTextured(int id) {
        return textured.get(id);
    }

    public static int getVboIndexID(int id) {
        return vboIndexID.get(id);
    }

    public static int getVboIndexSize(int id) {
        return vboIndexSize.get(id);
    }

    public static int getVboVertexID(int id) {
        return vboVertexID.get(id);
    }

    public static int getVboNormalID(int id) {
        return vboNormalID.get(id);
    }

    public static int getVboColorlID(int id) {
        return vboColorID.get(id);
    }

    public static int getVboTextureID(int modelID) {
        return vboTextureID.get(modelID);
    }
}