package me.geakstr.engine;

import me.geakstr.engine.core.Camera;
import me.geakstr.engine.core.Game;
import me.geakstr.engine.core.Shader;
import me.geakstr.engine.core.Transform;
import me.geakstr.engine.model.Model;

import me.geakstr.engine.model.ResourceBuffer;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Game {
	private Camera camera;
	private Shader shader;
	private Transform transform;

    private List<Model> models;
    Model box;

    public Main(String resDir) {
        super(resDir);
    }

	public void init() {
        Display.setTitle("3D Engine");
		
		camera = new Camera();
		camera.setPosition(0, 0, -8);

        ResourceBuffer.loadModels("cube/cube.obj");
        ResourceBuffer.loadBuffers();

        models = new ArrayList<Model>();

        box = ResourceBuffer.getModels().get("cube/cube.obj");
        for (float z = 0; z <= 10; z += 2) {
            for (float y = 0; y <= 10; y += 2) {
                for (float x = 0; x <= 10; x += 2) {
                    Model tmp = box.clone();
                    tmp.setup(x, y, z);
                    models.add(tmp);
                }
            }
        }

		shader = new Shader();
		shader.attachVertexShader("shader.vert");
		shader.attachFragmentShader("shader.frag");
		shader.link();
		
		transform = new Transform();
		//transform.rotate(new Vector3f(120f, 120f, 120f));
		
		glCullFace(GL_FRONT_FACE);
	}

	public void update() {
		camera.input();
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		
		camera.apply();

		shader.bind();
		shader.setUniform("mProjection", camera.getProjectionMatrix());
		shader.setUniform("mView", camera.getViewMatrix());
		shader.setUniform("lightPos", camera.getPosition());
		shader.setUniform("texture", 0);
		shader.setUniform("mNormal", camera.getNormalMatrix());
		shader.setUniform("mTransform", transform.getTransform());

        for (Model model : models) model.render();
        //glDrawArrays(GL_TRIANGLES, 0, 9 * ResourceBuffer.getFaces(1).size());

        shader.unbind();
	}

	public void resized() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void dispose() {
        for (Model model : models) model.dispose();
		shader.dispose();
	}

	public static void main(String[] args) {
		new Main(args[0]);
	}
}
