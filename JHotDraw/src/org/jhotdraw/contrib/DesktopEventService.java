/*
 * @(#)DesktopEventService.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.standard.NullDrawingView;
import org.jhotdraw.util.CollectionsFactory;

import java.util.List;
import java.util.ListIterator;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerListener;
import java.awt.event.ContainerEvent;
import java.awt.*;

/**
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class DesktopEventService {

	private DesktopEventServiceProduct desktopEventServiceProduct = new DesktopEventServiceProduct();
	private Container myContainer;
	public DesktopEventService(Desktop newDesktop, Container newContainer) {
		desktopEventServiceProduct.setListeners(CollectionsFactory.current().createList());
		setDesktop(newDesktop);
		setContainer(newContainer);
		getContainer().addContainerListener(desktopEventServiceProduct.createComponentListener(this));
	}

	private void setDesktop(Desktop newDesktop) {
		desktopEventServiceProduct.setMyDesktop(newDesktop);
	}

	protected Desktop getDesktop() {
		return desktopEventServiceProduct.getMyDesktop();
	}

	private void setContainer(Container newContainer) {
		myContainer = newContainer;
	}

	protected Container getContainer() {
		return myContainer;
	}

	public void addComponent(Component newComponent) {
		getContainer().add(newComponent);
	}

	public void removeComponent(DrawingView dv) {
		Component[] comps = getContainer().getComponents();
		for (int x = 0; x < comps.length; x++) {
			if (dv == Helper.getDrawingView(comps[x])) {
				getContainer().remove(comps[x]);
			    break;
			}
		}
	}

	public void removeAllComponents() {
		getContainer().removeAll();
	}

	public void addDesktopListener(DesktopListener dpl) {
		desktopEventServiceProduct.getListeners().add(dpl);
	}

	public void removeDesktopListener(DesktopListener dpl) {
		desktopEventServiceProduct.removeDesktopListener(dpl);
	}

	protected void fireDrawingViewAddedEvent(final DrawingView dv) {
		desktopEventServiceProduct.fireDrawingViewAddedEvent(dv);
	}

	protected void fireDrawingViewRemovedEvent(final DrawingView dv) {
		desktopEventServiceProduct.fireDrawingViewRemovedEvent(dv);
	}

	/**
	 * This method is only called if the selected drawingView has actually changed
	 */
	protected void fireDrawingViewSelectedEvent(final DrawingView oldView, final DrawingView newView) {
		desktopEventServiceProduct.fireDrawingViewSelectedEvent(oldView, newView);
	}

	/**
	 * @param oldView previous active drawing view (may be null because not all events require this information)
	 */
	protected DesktopEvent createDesktopEvent(DrawingView oldView, DrawingView newView) {
		return desktopEventServiceProduct.createDesktopEvent(oldView, newView);
	}

	public DrawingView[] getDrawingViews(Component[] comps) {
		List al = CollectionsFactory.current().createList();
		for (int x = 0; x < comps.length; x++) {
			DrawingView dv = Helper.getDrawingView(comps[x]);
			if (dv != null) {
				al.add(dv);
			}
		}
		DrawingView[] dvs = new DrawingView[al.size()];
		al.toArray(dvs);
		return dvs;
	}

	public DrawingView getActiveDrawingView() {
		return desktopEventServiceProduct.getMySelectedView();
	}

	protected void setActiveDrawingView(DrawingView newActiveDrawingView) {
		desktopEventServiceProduct.setMySelectedView(newActiveDrawingView);
	}
	
	protected ContainerListener createComponentListener() {
		return desktopEventServiceProduct.createComponentListener(this);
	}
}
