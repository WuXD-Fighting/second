package algorithm.entity;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;
public class Path {
	private List<Transition> tL = new ArrayList<Transition>();
	private List<Integer> states = new ArrayList<Integer>();
	double weight;//权值
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public List<Transition> gettL() {
		return tL;
	}
	//重置集合
	public void settL(List<Transition> tL) {
		this.tL = new ArrayList<Transition>();
		this.tL.addAll(tL);
		//重置状态路径
		this.states = new ArrayList<Integer>();
		for(int i = 0 ; i < tL.size() ; i++) {
			if(i == 0) {
				this.states.add(tL.get(i).getSource());
			}
			this.states.add(tL.get(i).getTarget());
		}
	}
	//拿到状态路径
	public List<Integer> getStatesList() {
		return states;
	}
	public void setPath(Path path) {
		this.tL = new ArrayList<Transition>();
		this.tL.addAll(path.gettL());
		//重置状态路径
		this.states = new ArrayList<Integer>();
		for(int i = 0 ; i < tL.size() ; i++) {
			if(i == 0) {
				this.states.add(tL.get(i).getSource());
			}
			this.states.add(tL.get(i).getTarget());
		}
	}
	//增加迁移
	public void push(Transition t) {
		this.tL.add(t);
		this.states.add(t.getTarget());
	}
	//返回大小
	public int getSize() {
		return tL.size();
	}
	//判断空
	public boolean isEmpty() {
		if(tL.size()==0||tL.isEmpty()) 
			return true;
		else
			return false;
	}
	//拿到集合的编号
	public List<Integer> getSet(){
		List<Integer> intL =new ArrayList<Integer>();
		for(int i = 0 ; i < tL.size() ; i++) {
			if(!intL.contains(tL.get(i).getNum())) {
				intL.add(tL.get(i).getNum());
			}
		}
		return intL;
	}
	//打印经过的迁移编号
	public void printSet() {
		for(int i = 0 ; i < tL.size() ; i++) {
			System.out.print(tL.get(i).getNum()+" ");
		}
	}
	//集合加入新集合
	public void pushList(Path li) {
		List<Transition> l = li.gettL();
		for(int i = 0 ; i < li.getSize() ; i++) {
			if(this.tL.isEmpty())
				this.states.add(l.get(i).getSource());
			this.tL.add(l.get(i));
			this.states.add(l.get(i).getTarget());
		}
	}
	//判断相等
	public boolean isEqual(Path newTL) {
		if(tL.size()!=newTL.getSize()) {
			return false;
		}else {
			List<Integer> set =new ArrayList<Integer>();
			set = this.getSet();
			List<Integer> newSet =new ArrayList<Integer>();
			newSet = newTL.getSet();
			if(set .equals(newSet))
				return true;
			else
				return false;
		}
		
	}
	//拿到总时间
	public double getSumTim() {
		double time = 0;
		for(Transition t : tL) {
			time += t.getTime();
		}
		return time;
	}
	public Path(List<Transition> tL) {
		super();
		this.tL = new ArrayList<Transition>();
		this.tL .addAll(tL);
		//重置状态路径
		this.states = new ArrayList<Integer>();
		for(int i = 0 ; i < tL.size() ; i++) {
			if(i == 0) {
				this.states.add(tL.get(i).getSource());
			}
			this.states.add(tL.get(i).getTarget());
		}
	}
	
	public Path() {
		super();
	}
	public Path(Path p) {
		super();
		this.tL = new ArrayList<Transition>();
		this.tL .addAll(p.gettL());
		//重置状态路径
		this.states = new ArrayList<Integer>();
		for(int i = 0 ; i < tL.size() ; i++) {
			if(i == 0) {
				this.states.add(tL.get(i).getSource());
			}
			this.states.add(tL.get(i).getTarget());
		}
	}
	//用于测试 通过int list直接新建一个path
	public void setByNums(List<Integer> nums) {
		for(Integer i:nums) {
			Transition newT = new Transition();
			newT.setNum(i);
			tL.add(newT);
		}
	}
	public static void main(String[] args) {
//		List<Integer> intL =new ArrayList<Integer>();
//		intL = asList(4, 1, 6);
//		List<Integer> intL2 =new ArrayList<Integer>();
//		intL2 = asList(7, 9, 8 , 8);
//		List<Integer> intL4 =new ArrayList<Integer>();
//		intL4 = asList(8, 9, 0 , 85);
//		List<List<Integer>> list1 = new ArrayList<List<Integer>>();
//		list1=asList(intL,intL2,intL4);
//		System.out.println(list1);
//		List<Integer> intL5 =new ArrayList<Integer>();
//		intL5 = asList(8, 9, 0 , 85);
//		List<Integer> intL6 =new ArrayList<Integer>();
//		intL6 = asList(8, 0, 9 , 85);
//		List<Integer> intL7 =new ArrayList<Integer>();
//		intL7 = asList( 0, 4);
//		System.out.println(list1.contains(intL6));
//		System.out.println(list1.contains(intL7));
//		System.out.println(intL4.equals(intL5));
		
	}
}
