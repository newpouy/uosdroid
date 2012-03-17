package com.google.droidar.gl.scenegraph;


import java.util.ArrayList;

import com.google.droidar.gl.Color;
import com.google.droidar.util.Vec;


public class MultiColoredShape extends Shape {

	ArrayList<Color> myColors = new ArrayList<Color>();

	/**
	 * no color parameter needed here because the color would be overpainted by
	 * the multicolor array
	 */
	public MultiColoredShape() {
		super(null);
		myRenderData = new MultiColorRenderData();
	}

	public void add(Vec v, Color c) {
		getMyShapeArray().add(v);
		myColors.add(c);
		myRenderData.updateShape(getMyShapeArray());
		((MultiColorRenderData) myRenderData).updateColorBuffer(myColors);
	}

}
