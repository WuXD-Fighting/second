package algorithm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Set implements Serializable{
	/**
	 * Set类用于为PathSet添加方法
	 */
	private static final long serialVersionUID = 1L;
	private List<Path> pathSet = new ArrayList<Path>();
	private double sumTime = 0;
	public List<Path> getPathSet() {
		return pathSet;
	}
	public void setPathSet(List<Path> pathSet) {
		this.pathSet = new ArrayList<Path>();
		this.pathSet.addAll(pathSet);
		this.sumTime = 0 ;
		for(Path path:pathSet) {
			sumTime+=path.getSumTim();
		}
	}
	
	public void addPath(Path path) {
		this.pathSet.add(path);
		this.sumTime += path.getSumTim();
	}
	public void addPathList(List<Path> pl) {
		for(Path path : pl) {
			this.pathSet.add(path);
			this.sumTime += path.getSumTim();
		}
	}
	public double getSumTime() {
		return sumTime;
	}
	public Integer getSize() {
		return this.pathSet.size();
	}
	public Path get(int i) {
		return this.pathSet.get(i);
	}
	public void setEle(int i ,Path path) {
		this.pathSet.set(i, path);
	}
	public void removeEle(int i ) {
		this.pathSet.remove(i);
	}
	public Set() {
		super();
	}
	public Set(List<Path> pl) {
		super();
		this.pathSet = new ArrayList<Path>();
		this.pathSet.addAll(pl);
		sumTime=0;
		for(Path path:pathSet) {
			sumTime+=path.getSumTim();
		}
	}
	
}
