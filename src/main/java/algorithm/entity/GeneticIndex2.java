package algorithm.entity;

import java.util.ArrayList;
import java.util.List;

public class GeneticIndex2 {
	//集合列表
	private List<Set> setList = new ArrayList<Set>();
	//需要覆盖的集合
	//private List<Integer> numList = new ArrayList<Integer>();
	//改为需要覆盖的需求迁移集
	private List<ReqEnum> reqs = new ArrayList<ReqEnum>();
	//执行次数
	private int times;
	private Transition[][] matrix ;
	//取前几的进行遗传
	private int shortN;
	//路径终点
	private int end;
	//起点
	private int start;
	
	//贪心算法参数
	private int bigStepGreedyIndex;
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
	
	private int populationSize;
	
	public int getPopulationSize() {
		return populationSize;
	}
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	public List<Set> getSetList() {
		return setList;
	}
	public void setSetList(List<Set> setList) {
		this.setList = setList;
	}
	public List<ReqEnum> getReqs() {
		return reqs;
	}
	public void setReqs(List<ReqEnum> reqs) {
		this.reqs = reqs;
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
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getBigStepGreedyIndex() {
		return bigStepGreedyIndex;
	}
	public void setBigStepGreedyIndex(int bigStepGreedyIndex) {
		this.bigStepGreedyIndex = bigStepGreedyIndex;
	}
	
}
