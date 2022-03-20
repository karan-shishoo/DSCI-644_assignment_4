/*
 * @(#)NodeFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.samples.net;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.figures.*;
import org.jhotdraw.util.*;

import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * @version <$CURRENT_VERSION$>
 */
public class NodeFigure extends TextFigure {
	private NodeFigureProduct nodeFigureProduct = new NodeFigureProduct();
	private static final int BORDER = 6;
	private boolean     fConnectorsVisible;

	public NodeFigure() {
		initialize();
		nodeFigureProduct.setFConnectors(null);
	}

	public Rectangle displayBox() {
		Rectangle box = super.displayBox();
		int d = BORDER;
		box.grow(d, d);
		return box;
	}

	public boolean containsPoint(int x, int y) {
		// add slop for connectors
		if (fConnectorsVisible) {
			Rectangle r = displayBox();
			int d = LocatorConnector.SIZE/2;
			r.grow(d, d);
			return r.contains(x, y);
		}
		return super.containsPoint(x, y);
	}

	private void drawBorder(Graphics g) {
		Rectangle r = displayBox();
		g.setColor(getFrameColor());
		g.drawRect(r.x, r.y, r.width-1, r.height-1);
	}

	public void draw(Graphics g) {
		super.draw(g);
		drawBorder(g);
		drawConnectors(g);
	}

	public HandleEnumeration handles() {
		ConnectionFigure prototype = new LineConnection();
		List handles = CollectionsFactory.current().createList();
		handles.add(new ConnectionHandle(this, RelativeLocator.east(), prototype));
		handles.add(new ConnectionHandle(this, RelativeLocator.west(), prototype));
		handles.add(new ConnectionHandle(this, RelativeLocator.south(), prototype));
		handles.add(new ConnectionHandle(this, RelativeLocator.north(), prototype));

		handles.add(new NullHandle(this, RelativeLocator.southEast()));
		handles.add(new NullHandle(this, RelativeLocator.southWest()));
		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new NullHandle(this, RelativeLocator.northWest()));
		return new HandleEnumerator(handles);
	}

	private void drawConnectors(Graphics g) {
		if (fConnectorsVisible) {
			Iterator iter = nodeFigureProduct.connectors(this);
			while (iter.hasNext()) {
				((Connector)iter.next()).draw(g);
			}
		}
	}

	/**
	 */
	public void connectorVisibility(boolean isVisible, ConnectionFigure courtingConnection) {
		fConnectorsVisible = isVisible;
		invalidate();
	}

	/**
	 */
	public Connector connectorAt(int x, int y) {
		return nodeFigureProduct.findConnector(x, y, this);
	}

	private void initialize() {
		setText("node");
		Font fb = new Font("Helvetica", Font.BOLD, 12);
		setFont(fb);
		nodeFigureProduct.createConnectors(this);
	}

	/**
	 * Usually, a TextHolders is implemented by a Figure subclass. To avoid casting
	 * a TextHolder to a Figure this method can be used for polymorphism (in this
	 * case, let the (same) object appear to be of another type).
	 * Note, that the figure returned is not the figure to which the TextHolder is
	 * (and its representing figure) connected.
	 * @return figure responsible for representing the content of this TextHolder
	 */
	public Figure getRepresentingFigure() {
		return this;
	}

	public Object clone() {
		NodeFigure clone = (NodeFigure) super.clone();
		clone.nodeFigureProduct = (NodeFigureProduct) this.nodeFigureProduct.clone();
		return clone;
	}
}
