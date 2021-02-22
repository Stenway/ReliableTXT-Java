package com.stenway.reliabletxt;

public class SmlNamedNode extends SmlNode {
	public String Name;
	
	public SmlNamedNode(String name) {
		Name = name;
	}
	
	public boolean hasName(String name) {
		if (Name == null) return name == null;
		return Name.equalsIgnoreCase(name);
	}
}