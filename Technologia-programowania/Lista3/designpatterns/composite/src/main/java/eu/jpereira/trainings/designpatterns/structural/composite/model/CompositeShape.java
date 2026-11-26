/**
 * Copyright 2011 Joao Miguel Pereira
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.jpereira.trainings.designpatterns.structural.composite.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Joao Pereira
 * 
 */
public abstract class CompositeShape extends Shape {

	List<Shape> shapes;

	public CompositeShape() {
		this.shapes = createShapesList();
	}

	/**
	 * Remove a shape from this shape childrens
	 * 
	 * @param shape
	 *            the shape to remove
	 * @return true if the shape was present and was removed, false if the shape
	 *         was not present
	 */
	public boolean removeShape(Shape shape) {
		return this.shapes.remove(shape);
	}

	/**
	 * Return the total shapes count in case this is a composite
	 * 
	 * @return the total count of shapes if the shape is composite. -1 otherwise
	 */
	public int getShapeCount() {
		return this.shapes.size();
	}

	/**
	 * Add a shape to this shape.
	 * 
	 * @param shape
	 *            The shape to add
	 * @throws ShapeDoesNotSupportChildren
	 *             if this shape is not a composite
	 */
	public void addShape(Shape shape) {
		this.shapes.add(shape);
	}

	public List<Shape> getShapes() {
		// read-only view: children are changed only through addShape/removeShape
		return Collections.unmodifiableList(this.shapes);
	}

	/**
	 * @param type
	 * @return direct children of the given type
	 */
	public List<Shape> getShapesByType(ShapeType type) {
		List<Shape> result = new ArrayList<Shape>();
		for (Shape shape : this.shapes) {
			if (shape.getType() == type) {
				result.add(shape);
			}
		}
		return result;
	}

	/**
	 * Return all shapes that are leafs in the tree
	 * 
	 * @return
	 */
	public List<Shape> getLeafShapes() {
		List<Shape> leafs = new ArrayList<Shape>();
		for (Shape shape : this.shapes) {
			CompositeShape composite = shape.asComposite();
			if (composite == null) {
				leafs.add(shape);
			} else {
				leafs.addAll(composite.getLeafShapes());
			}
		}
		return leafs;
	}

	/**
	 * Factory method for the list of children of this shape
	 * 
	 * @return
	 */
	protected List<Shape> createShapesList() {
		return new ArrayList<Shape>();
	}

	@Override
	public CompositeShape asComposite() {
		return this;
	}
}
