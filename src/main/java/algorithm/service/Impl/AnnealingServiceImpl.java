package algorithm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.entity.Transition;
import algorithm.entity.Path;
import algorithm.service.AnnealingService;
import algorithm.utils.GetTransitionByXml;
import algorithm.utils.Judgement;

public class AnnealingServiceImpl implements AnnealingService{
	static Judgement judgement = new Judgement();
	
	
	public  void initialMatrix(List<Transition> tL,Transition[][] matrix , Double[][] distance) {
		int m = matrix.length;
		for(int i = 0 ; i < m ; i++) {
			for( int j = 0 ; j < m ; j++) {
				distance[i][j]=Double.POSITIVE_INFINITY;
				matrix[i][j]=new Transition();
			}
		}
		if(tL!=null)
			for(Transition t:tL) {
				int source = t.getSource();
				int target = t.getTarget();
				matrix[source][target] = t;
			}
		
		for(int i = 0 ; i < m ; i++) {
			for( int j = 0 ; j < m ; j++) {
				if(matrix[i][j].getTime()!=0)
					distance[i][j] = matrix[i][j].getTime();
			}
		}
	}

	public boolean equalList(List list1, List list2) {
		if (list1.size() != list2.size())
			return false;
		for (Object object : list1) {
			if (!list2.contains(object))
				return false;
		}
		return true;
	}
	//得到随机路径组成的集合，每生成n条路径
	public  void getRandomPathList(Transition[][] matrix,int start ,int end ,List<Integer> states,List<Path> pathSet,int n) {
		while(pathSet.size()<n) {
			List<Transition> newTL = new ArrayList<Transition>();
			try {
				newTL=randomPath(matrix, start, end, newTL, states);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			Path path = new Path(newTL);
			if(!isContains(pathSet,path)&&!path.isEmpty())
				pathSet.add(path);
		}
	}
	
	public  List<Transition> pushList(Path li) {
		List<Transition> tL = new ArrayList<Transition>();
		List<Transition> l = li.gettL();
		for(int i = 0 ; i < li.getSize() ; i++) {
			tL.add(l.get(i));
		}
		return tL;
	}
	private static boolean isContains(List<Path> pathSet,Path path) {
		for(int i = 0 ; i < pathSet.size() ; i++) {
			if(pathSet.get(i).getSize() == path.getSize() ) {
				List<Integer> set = pathSet.get(i).getSet();
				if(set.equals(path.getSet())) {
					return true;
				}
			}
		}
		return false;
	}
	//构建随机可行路径
	private  static List<Transition> randomPath(Transition[][] matrix,int start ,int end ,List<Transition> tL,List<Integer> states) throws Exception{
		int m =matrix.length;
		//将新的状态加入到该路径的states集合中
		states.add(start);
		//t为当前start状态的可用迁移
		List<Transition> t = new ArrayList<Transition>();
		for(int i = 0 ; i < m ; i++) {
			if(matrix[start][i].getTime()!=0) {
				t.add(matrix[start][i]);
			}
		}
		//到达end就结束
		if(start!=end) {
			//可行性flag判断
			boolean executable = false;
			//执行循环次数上限，避免迁移进入死循环
			int n = 50;
			//用于记录产生迁移后的尾状态
			int newStart=-1;
			//执行循环
			while(!executable&&n>0) {
				//随机产生下一条迁移
				Random r = new Random();
				int random = r.nextInt(t.size());
				Transition newT = t.get(random);
				
				//test为执行性判断的临时List
				List<Transition> test =new ArrayList<Transition>();
				test.addAll(tL);
				test.add(newT);
				executable=judgement.judge(test);
				//如果可行则将新的迁移加入到tL中，跳出循环，且记录尾状态
				if(executable) {
					tL.add(newT);
					newStart = newT.getTarget();
				}
				n--;
			}
	
			//如果循环结束无可行路径 则丢弃该List
			if(!executable) {
				tL = null;
				return tL;
			}
			//如果当前迁移未到达end状态，进入下一个节点进行迭代，直到到达end
			randomPath(matrix,newStart ,end ,tL,states);
		}
		return tL;
	}
	

	
	
	//public  void main(String[] args) throws Exception{
//		List<Transition> tL = new ArrayList<Transition>();
//		tL=GetTransitionByXml.getTransitionList();
//		initialMatrix( tL,  m);
//		for(int i = 0 ; i < m ; i++) {
//			for(int j = 0 ; j < m ; j++)
//			System.out.print(matrix[i][j].getTime()+"  ");
//			System.out.print("\n");
//		}
//		
//		List<Transition> tl = new ArrayList<Transition>();
//		List<Integer> states = new ArrayList<Integer>();
//		randomPath(0 ,7 ,tl,states);
//		for(int i = 0 ; i < tl.size() ; i++) {
//			System.out.print(tl.get(i).getNum()+"  ");
//		}
//		System.out.println();
//		System.out.println(states);
		
	//}
}
