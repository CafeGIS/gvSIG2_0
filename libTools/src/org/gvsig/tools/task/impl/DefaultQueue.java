package org.gvsig.tools.task.impl;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.tools.task.Queue;


public class DefaultQueue implements Queue {

	private List list = new ArrayList();

	public DefaultQueue() {
	}

	public void clear() {
		synchronized (list) {
			list.clear();
		}
	}

	public void put(Object data) {
		synchronized (list) {
			list.add(data);
			list.notify();
		}
	}

	public Object top() throws InterruptedException {
		Object data = null;
		synchronized (list) {
			if (list.isEmpty()) {
				list.wait();
			}
			data = list.get(0);
		}
		return data;
	}

	public Object top_nowait() {
		try {
			Object data;
			synchronized (list) {
				data = list.get(0);
			}
			return data;
		} catch (Exception except) {
			return null;
		}
	}

	public Object get() throws InterruptedException {
		Object data = null;
		synchronized (list) {
			if (list.isEmpty()) {
				list.wait();
			}
			data = list.get(0);
			list.remove(0);
		}
		return data;
	}

	public Object get_nowait() {
		try {
			Object data;
			synchronized (list) {
				data = list.get(0);
				list.remove(0);
			}
			return data;
		} catch (Exception except) {
			return null;
		}
	}

	public boolean isEmpty() {
		synchronized (list) {
			return list.isEmpty();
		}
	}

	public int getSize() {
		synchronized (list) {
			return list.size();
		}
	}
}