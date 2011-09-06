package com.em.janus.model;


public class Tag extends Entity implements Comparable<Tag> {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(Tag o) {
		if(o == null) return 1;
		return this.getName().compareTo(o.getName());
	}	
}
