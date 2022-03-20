package org.jhotdraw.contrib.dnd;


import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragGestureListener;

public class DNDHelperProduct {
	private DragGestureRecognizer dgr;
	private DragGestureListener dragGestureListener;

	public DragGestureRecognizer getDgr() {
		return dgr;
	}

	public void setDgr(DragGestureRecognizer dgr) {
		this.dgr = dgr;
	}

	public DragGestureListener getDragGestureListener() {
		return dragGestureListener;
	}

	public void setDragGestureListener(DragGestureListener dragGestureListener) {
		this.dragGestureListener = dragGestureListener;
	}

	/**
	* Used to destroy the gesture listener which ineffect turns off dragability.
	*/
	public void destroyDragGestreRecognizer(DNDHelper dNDHelper) {
		if (dgr != null) {
			dgr.removeDragGestureListener(dragGestureListener);
			dgr.setComponent(null);
			dNDHelper.setDragGestureRecognizer(null);
		}
	}
}