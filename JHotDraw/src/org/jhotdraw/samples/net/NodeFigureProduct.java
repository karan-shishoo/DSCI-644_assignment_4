package org.jhotdraw.samples.net;


import java.util.List;
import java.util.Iterator;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.standard.LocatorConnector;
import org.jhotdraw.standard.RelativeLocator;
import org.jhotdraw.framework.Connector;
import java.awt.Point;
import org.jhotdraw.util.Geom;

public class NodeFigureProduct implements Cloneable {
	private List fConnectors;

	public void setFConnectors(List fConnectors) {
		this.fConnectors = fConnectors;
	}

	/**
	*/
	public Iterator connectors(NodeFigure nodeFigure) {
		if (fConnectors == null) {
			createConnectors(nodeFigure);
		}
		return fConnectors.iterator();
	}

	public void createConnectors(NodeFigure nodeFigure) {
		fConnectors = CollectionsFactory.current().createList(4);
		fConnectors.add(new LocatorConnector(nodeFigure, RelativeLocator.north()));
		fConnectors.add(new LocatorConnector(nodeFigure, RelativeLocator.south()));
		fConnectors.add(new LocatorConnector(nodeFigure, RelativeLocator.west()));
		fConnectors.add(new LocatorConnector(nodeFigure, RelativeLocator.east()));
	}

	public Connector findConnector(int x, int y, NodeFigure nodeFigure) {
		long min = Long.MAX_VALUE;
		Connector closest = null;
		Iterator iter = connectors(nodeFigure);
		while (iter.hasNext()) {
			Connector c = (Connector) iter.next();
			Point p2 = Geom.center(c.displayBox());
			long d = Geom.length2(x, y, p2.x, p2.y);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public Object clone() {
		try {
			return (NodeFigureProduct) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}
}