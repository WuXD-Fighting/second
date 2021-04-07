package algorithm.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlTree {
	private String name;
	private String id;
	private int level;
	private String value;
	private List<XmlTree> children = new ArrayList<XmlTree>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<XmlTree> getChildren() {
		return children;
	}
	public void setChildren(List<XmlTree> children) {
		this.children = children;
	}
	public void addChild(XmlTree child) {
		this.children.add(child);
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "XmlTree [name=" + name + ", id=" + id + ", level=" + level + ", value=" + value + ", children="
				+ children + "]";
	}
	
}
