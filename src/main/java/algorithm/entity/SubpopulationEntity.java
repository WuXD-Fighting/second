package algorithm.entity;

import java.util.ArrayList;
import java.util.List;
//子种群内部遗传算法
public class SubpopulationEntity {
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
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	//发生贪心的概率（或者每n次发生一次贪心）
	private double greedyProbability;
	//交叉率
	private double pc1;
	private double pc2;
	//变异率
	private double pm1;
	private double pm2;
	//锦标赛参数
	private int championshipIndex;
	private int championshipSize;
	private int populationSize;
	public Transition[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(Transition[][] matrix) {
		int m =matrix.length;
		this.matrix = new Transition[m][m];
		this.matrix = matrix;
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
	public double getPc1() {
		return pc1;
	}
	public void setPc1(double pc1) {
		this.pc1 = pc1;
	}
	public double getPc2() {
		return pc2;
	}
	public void setPc2(double pc2) {
		this.pc2 = pc2;
	}
	public double getPm1() {
		return pm1;
	}
	public void setPm1(double pm1) {
		this.pm1 = pm1;
	}
	public double getPm2() {
		return pm2;
	}
	public void setPm2(double pm2) {
		this.pm2 = pm2;
	}
	public int getChampionshipIndex() {
		return championshipIndex;
	}
	public void setChampionshipIndex(int championshipIndex) {
		this.championshipIndex = championshipIndex;
	}
	public int getChampionshipSize() {
		return championshipSize;
	}
	public void setChampionshipSize(int championshipSize) {
		this.championshipSize = championshipSize;
	}
	public int getPopulationSize() {
		return populationSize;
	}
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	
}
