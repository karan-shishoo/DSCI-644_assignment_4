package org.jhotdraw.contrib;


import java.util.List;
import org.jhotdraw.framework.DrawingView;
import java.awt.event.ContainerListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.Container;
import org.jhotdraw.standard.NullDrawingView;
import java.util.ListIterator;

public class DesktopEventServiceProduct {
	private java.util.List listeners;
	private DrawingView mySelectedView;
	private Desktop myDesktop;

	public java.util.List getListeners() {
		return listeners;
	}

	public void setListeners(java.util.List listeners) {
		this.listeners = listeners;
	}

	public DrawingView getMySelectedView() {
		return mySelectedView;
	}

	public void setMySelectedView(DrawingView mySelectedView) {
		this.mySelectedView = mySelectedView;
	}

	public Desktop getMyDesktop() {
		return myDesktop;
	}

	public void setMyDesktop(Desktop myDesktop) {
		this.myDesktop = myDesktop;
	}

	public void removeDesktopListener(DesktopListener dpl) {
		listeners.remove(dpl);
	}

	public ContainerListener createComponentListener(final DesktopEventService desktopEventService) {
		return new ContainerAdapter() {
			/**
			* If the dv is null assert
			* @todo  does adding a component always make it the selected view? Yes so far because this is only being used on single view Desktops. If it is to work on multipleView desktops, the we need to think further.
			*/
			public void componentAdded(ContainerEvent e) {
				DrawingView dv = Helper.getDrawingView((java.awt.Container) e.getChild());
				DrawingView oldView = mySelectedView;
				if (dv != null) {
					fireDrawingViewAddedEvent(dv);
					desktopEventService.setActiveDrawingView(dv);
					fireDrawingViewSelectedEvent(oldView, mySelectedView);
				}
			}

			/**
			* If dv is null assert dv will only be null if something thats not a drawingView was added to the desktop.  it would be simpler if we forbade that.
			*/
			public void componentRemoved(ContainerEvent e) {
				DrawingView dv = Helper.getDrawingView((java.awt.Container) e.getChild());
				if (dv != null) {
					DrawingView oldView = mySelectedView;
					desktopEventService.setActiveDrawingView(NullDrawingView.getManagedDrawingView(oldView.editor()));
					fireDrawingViewSelectedEvent(oldView, mySelectedView);
					fireDrawingViewRemovedEvent(dv);
				}
			}
		};
	}

	public void fireDrawingViewAddedEvent(final DrawingView dv) {
		ListIterator li = listeners.listIterator(listeners.size());
		DesktopEvent dpe = createDesktopEvent(mySelectedView, dv);
		while (li.hasPrevious()) {
			DesktopListener dpl = (DesktopListener) li.previous();
			dpl.drawingViewAdded(dpe);
		}
	}

	public void fireDrawingViewRemovedEvent(final DrawingView dv) {
		ListIterator li = listeners.listIterator(listeners.size());
		DesktopEvent dpe = createDesktopEvent(mySelectedView, dv);
		while (li.hasPrevious()) {
			DesktopListener dpl = (DesktopListener) li.previous();
			dpl.drawingViewRemoved(dpe);
		}
	}

	/**
	* This method is only called if the selected drawingView has actually changed
	*/
	public void fireDrawingViewSelectedEvent(final DrawingView oldView, final DrawingView newView) {
		ListIterator li = listeners.listIterator(listeners.size());
		DesktopEvent dpe = createDesktopEvent(oldView, newView);
		while (li.hasPrevious()) {
			DesktopListener dpl = (DesktopListener) li.previous();
			dpl.drawingViewSelected(dpe);
		}
	}

	/**
	* @param oldView  previous active drawing view (may be null because not all events require this information)
	*/
	public DesktopEvent createDesktopEvent(DrawingView oldView, DrawingView newView) {
		return new DesktopEvent(myDesktop, newView, oldView);
	}
}