package me.geakstr.engine;

import me.geakstr.engine.core.*;
import me.geakstr.engine.model.Model;

import me.geakstr.engine.model.ResourceBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Game {
    private Camera camera;
    private Frustum frustum;
    private Shader baseShader;
    private Transform transform;

    private World world;

    private boolean wasInput;

    public Main(String resDir) {
        super(resDir);
    }

    private int tick;

    public void init() {
        camera = new Camera(70, (float) Display.getWidth() / (float) Display.getHeight(), 0.1f, 70f);
        camera.setPosition(0, 0, -8);

        frustum = new Frustum();

        baseShader = new Shader();
        baseShader.attachVertexShader("shader.vert");
        baseShader.attachFragmentShader("shader.frag");
        baseShader.link();

        transform = new Transform();

        ResourceBuffer.loadModels("cube/cube.obj");
        Model box = ResourceBuffer.getModels().get("cube/cube.obj");
        int id = box.getId();

        world = new World(5, 16, 256);

        System.out.println(world.getLength() + " " + world.getWidth() + " " + world.getHeight());
        for (int x = 0; x < world.getWidth(); x += 1) {
            for (int z = 0, ang = 0; z < world.getLength(); z += 1) {
                for (int y = 0; y < world.getHeight(); y += 1) {
                    //world.setModelToMap(box.clone(x, y, z, ang, ang, ang, 0.5f, 0.5f, 0.5f));
                    world.setModelToMap(id, x, y, z);
                }
            }
        }

        wasInput = true;

        tick = 0;

        glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
        glClearDepth (1.0f);                                        // Depth Buffer Setup
        glDepthFunc (GL_LEQUAL);                                    // The Type Of Depth Testing (Less Or Equal)
        glEnable (GL_DEPTH_TEST);                                   // Enable Depth Testing
        glShadeModel (GL_SMOOTH);                                   // Select Smooth Shading
        glHint (GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        System.out.println(GLContext.getCapabilities().GL_EXT_framebuffer_object);
    }

    public void update() {
        wasInput = camera.input();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);

        camera.apply();

        if (wasInput) {
            frustum.update(camera.getProjectionMatrix(), camera.getViewMatrix());
        }

        baseShader.bind();
        baseShader.setUniform("mProjection", camera.getProjectionMatrix());
        baseShader.setUniform("mView", camera.getViewMatrix());
        baseShader.setUniform("mNormal", camera.getNormalMatrix());
        baseShader.setUniform("mTransform", transform.getTransform());
        baseShader.setUniform("lightPos", camera.getPosition());
        baseShader.setUniform("texture", 0);


        for (int x = 0; x < world.getWidth(); x += 1) {
            for (int z = 0; z < world.getLength(); z += 1) {
                for (int y = 0; y < world.getHeight(); y += 1) {
                    int id = world.getModelFromMap(x, y, z);
                    if (id != 0 && frustum.checkCube(x, y, z, 1) >= 1 && !world.isSurrounded(x, y, z)) {
                        Model.render(id, x, y, z, 0, 0, 0, 0.5f, 0.5f, 0.5f, baseShader);
                    }
                }
            }
        }

        tick++;
        if (tick == 5) {
            world.setNullToMap(0, 2, 2);
            world.setNullToMap(1, 2, 2);
            world.setNullToMap(2, 2, 2);
            world.setNullToMap(3, 2, 2);
            world.setNullToMap(4, 2, 2);
        }

        baseShader.unbind();
    }

    public void resized() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public void dispose() {
        for (Model model : ResourceBuffer.getModels().values()) model.dispose();
        baseShader.dispose();
    }

    public static void main(String[] args) {
        new Main(args[0]);
    }
}
