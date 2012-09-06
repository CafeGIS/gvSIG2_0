package org.gvsig.tools.task;


public interface Queue {

	public void put(Object data);

	public Object top() throws InterruptedException;

	public Object top_nowait();

	public Object get() throws InterruptedException;

	public Object get_nowait();

	public boolean isEmpty();

	public int getSize();

	public void clear();


}