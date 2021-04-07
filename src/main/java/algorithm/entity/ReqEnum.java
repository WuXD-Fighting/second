package algorithm.entity;

import java.util.ArrayList;
import java.util.List;

public class ReqEnum {
	private List<Integer> tL = new ArrayList<Integer>();

	public List<Integer> getTL() {
		return tL;
	}
	public void setTL(List<Integer> tL) {
		this.tL = new ArrayList<Integer>();
		this.tL.addAll(tL);
	}
	public void addTL(int trNum) {
		this.tL.add(trNum);
	}
	public boolean isCovered(int trNum) {
		boolean flag = false;
		flag = tL.contains(trNum);
		return flag;
	}
	public ReqEnum(List<Integer> tL) {
		super();
		this.tL.addAll(tL);
	}
	public ReqEnum() {
		super();
	}
	
}
