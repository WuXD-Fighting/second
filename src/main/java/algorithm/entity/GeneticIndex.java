package algorithm.entity;

import java.util.ArrayList;
import java.util.List;

public class GeneticIndex {
	//集合列表
	private List<Set> setList = new ArrayList<Set>();
	//需要覆盖的集合
	private List<Integer> numList = new ArrayList<Integer>();
	//执行次数
	private int times;
	private Transition[][] matrix ;
	//取前几的进行遗传
	private int shortN;
	public Transition[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(Transition[][] matrix) {
		int m =matrix.length;
		this.matrix = new Transition[m][m];
		this.matrix = matrix;
	}
	//发生贪心的概率（或者每n次发生一次贪心）
	private double greedyProbability;
	
	public List<Set> getSetList() {
		return setList;
	}
	public void setSetList(List<Set> setList) {
		this.setList = setList;
	}
	public List<Integer> getNumList() {
		return numList;
	}
	public void setNumList(List<Integer> numList) {
		this.numList = numList;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getShortN() {
		return shortN;
	}
	public void setShortN(int shortN) {
		this.shortN = shortN;
	}
	public double getGreedyProbability() {
		return greedyProbability;
	}
	public void setGreedyProbability(double greedyProbability) {
		this.greedyProbability = greedyProbability;
	}
}
