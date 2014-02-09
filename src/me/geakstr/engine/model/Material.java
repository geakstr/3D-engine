package me.geakstr.engine.model;

import me.geakstr.engine.math.Vector3f;

public class Material {
	private Vector3f diffuse;
	private String name;

	public Material(String name) {
		diffuse = new Vector3f(1, 1, 1);
		this.name = name;
	}

	public Vector3f getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Vector3f diffuse) {
		this.diffuse = diffuse;
	}

	public String getName() {
		return name;
	}

}
