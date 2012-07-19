package com.em.janus.model.response;

import java.util.Map;

public class JanusResponse {

	private int pageSize = -1;
	
	private int currentIndex = -1;
	
	private int items = -1;
	
	private Map<Object, Object> elements = null;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public Map<Object, Object> getElements() {
		return elements;
	}

	public void setElements(Map<Object, Object> elements) {
		this.elements = elements;
	}	
}
