package algorithm.entity;

import java.util.ArrayList;
import java.util.List;

public class MPGAEntity {
	
	private Transition[][] matrix ;
	//改为需要覆盖的需求迁移集
	private List<ReqEnum> reqs = new ArrayList<ReqEnum>();
	//种群数量
	private int populationCount;
	//每个种群大小
	private int populationSize;
	//末状态与起始状态
	private int end;
	private int start;
	//贪心算法参数
	private int bigStepGreedyIndex;
	
	//交叉率
	private double pc1;
	private double pc2;
	//变异率
	private double pm1;
	private double pm2;
	
	//种群迁移数量
	private int transfer;
	//迁移隔代数
	private int interval;
	//结果确认比较次数
	private int confirm;
	//锦标赛参数
	private int championshipIndex;
	private int championshipSize;
	
	public Transition[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(Transition[][] matrix) {
		this.matrix = matrix;
	}
	public List<ReqEnum> getReqs() {
		return reqs;
	}
	public void setReqs(List<ReqEnum> reqs) {
		this.reqs = reqs;
	}
	public int getPopulationCount() {
		return populationCount;
	}
	public void setPopulationCount(int populationCount) {
		this.populationCount = populationCount;
	}
	public int getPopulationSize() {
		return populationSize;
	}
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
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
	public int getTransfer() {
		return transfer;
	}
	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getConfirm() {
		return confirm;
	}
	public void setConfirm(int confirm) {
		this.confirm = confirm;
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
	
}
