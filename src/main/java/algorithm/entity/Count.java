package algorithm.entity;

import java.util.ArrayList;
import java.util.List;

public class Count {
	private List<Double> time  = new ArrayList<Double>();
	private double ave;
	private int count ;
	private int n;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Count() {
		super();
	}
	public void add(double time) {
		this.time.add(time);
	}
	
	public List<Double> getTime() {
		return time;
	}
	public void setTime(List<Double> time) {
		this.time = time;
	}
	public double getAve() {
		return ave;
	}
	public void setAve(double ave) {
		this.ave = ave;
	}
	
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	@Override
	public String toString() {
		return "Count [time=" + time + ", ave=" + ave + ", count=" + count + ", n=" + n + "]";
	}
	
}
