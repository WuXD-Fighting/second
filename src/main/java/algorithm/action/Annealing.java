package algorithm.action;

import java.util.ArrayList;
import java.util.List;
import algorithm.entity.Transition;
import algorithm.service.Impl.AnnealingServiceImpl;
import algorithm.service.Impl.SetServiceImpl;
import algorithm.entity.Path;
import algorithm.entity.Set;
import algorithm.utils.GetTransitionByXml;
import algorithm.utils.Judgement;

public class Annealing {
	static AnnealingServiceImpl annealingServiceImpl = new AnnealingServiceImpl();
	static SetServiceImpl setServiceImpl = new SetServiceImpl();
	static Transition[][] matrix ;//迁移矩阵
	static Double[][] distance ;//距离矩阵
	static Judgement judgement = new Judgement();//判断可行性工具类
	static int m = 8;//状态数
	static List<Path> pathSet;//路径集合
	static List<Integer> numList;//需要覆盖的迁移集合
	static List<List<Path>> pathSetList;
	
	public static void main(String[] args) throws Exception{
		//初始化
		pathSet = new ArrayList<Path>();
		pathSetList = new ArrayList<List<Path>>();
		List<Transition> tL = new ArrayList<Transition>();
		tL=GetTransitionByXml.getTransitionList();
		numList = new ArrayList<Integer>();
		numList=GetTransitionByXml.getNumSet(tL);
		matrix = new Transition[m][m];
		distance = new Double[m][m]; 
		annealingServiceImpl.initialMatrix(tL, matrix, distance);
//		List<Integer> states = new ArrayList<Integer>();
		//生成随机路径
//		annealingServiceImpl.getRandomPathList(matrix, 0, 7,  states, pathSet,30);
//		for(int i = 0 ; i < pathSet.size() ; i++) {
//			System.out.print("第"+(i+1)+"条路径");
//			pathSet.get(i).printSet();
//			System.out.print("总用时为："+ pathSet.get(i).getSumTim()+"\t");
//			System.out.print("States路径:"+pathSet.get(i).getStatesList());
//			System.out.println("\n");
//		}
//		boolean result=setServiceImpl.isCovered(numList, pathSet);
//		System.out.println(numList);
//		System.out.println(result);
//		
//		List<Path> paths = new ArrayList<Path>();
//		paths = setServiceImpl.getPathSets(numList,pathSet);
//		double time = 0;
//		for(Path path:paths) {
//			path.printSet();
//			System.out.println("\n");
//			time += path.getSumTim();
//		}
//		System.out.println("总用时："+time);
		
//		List<List<Path>> setList = new ArrayList<List<Path>>();
//		while(setList.size() < 3) {
//			System.out.println("-------------------------------------------");
//			List<Integer> states = new ArrayList<Integer>();
//			List<Path> temp = new ArrayList<Path>();
//			annealingServiceImpl.getRandomPathList(matrix, 0, 7,  states, temp,20);
//			List<Path> tempSet = new ArrayList<Path>();
//			tempSet= setServiceImpl.getPathSets(numList,temp);
//			boolean result=setServiceImpl.isCovered(numList, tempSet);
//			System.out.println(result);
//			if(result) {
//				setList.add(tempSet);
//				System.out.println("第"+setList.size()+"个集合");
//				for(int i = 0 ; i < tempSet.size() ; i++) {
//					System.out.println("第"+i+"条路径:"+tempSet.get(i).getSet());
//				}
//				
//			}
//		}
//		for(int i = 0 ; i < setList.size() ; i++) {
//			List<Path> pl = new ArrayList<Path>();
//			pl = setList.get(i);
//			System.out.println("第"+i+"个路径集合为:");
//			for(Path p : pl) {
//				p.printSet();
//				System.out.println("\n");
//			}
//		}
		List<Set> setList = new ArrayList<Set>();
		while(setList.size()<20) {
			List<Integer> states = new ArrayList<Integer>();
			List<Path> temp = new ArrayList<Path>();
			Set tempSet = new Set();
			annealingServiceImpl.getRandomPathList(matrix, 0, 7,  states, temp,20);
			tempSet.setPathSet(setServiceImpl.getPathSets(numList,temp));

			if(!tempSet.getPathSet().isEmpty()) {
				setList.add(tempSet);
				System.out.println("-------------------------------------------");
				System.out.println("第"+setList.size()+"个集合:");
				for(int i = 0 ; i < tempSet.getSize() ; i++) 
					System.out.println("第"+i+"条路径:"+tempSet.get(i).getSet());
			}
		}
	}
	
//	public static void initialMatrix(List<Transition> tL, int m) {
//		matrix = new Transition[m][m];
//		distance = new Double[m][m];
//		for(int i = 0 ; i < m ; i++) {
//			for( int j = 0 ; j < m ; j++) {
//				distance[i][j]=Double.POSITIVE_INFINITY;
//				matrix[i][j]=new Transition();
//			}
//		}
//		if(tL!=null)
//			for(Transition t:tL) {
//				int source = t.getSource();
//				int target = t.getTarget();
//				matrix[source][target] = t;
//			}
//		
//		for(int i = 0 ; i < m ; i++) {
//			for( int j = 0 ; j < m ; j++) {
//				if(matrix[i][j].getTime()!=0)
//					distance[i][j] = matrix[i][j].getTime();
//			}
//		}
//	}
//
//	public boolean equalList(List list1, List list2) {
//		if (list1.size() != list2.size())
//			return false;
//		for (Object object : list1) {
//			if (!list2.contains(object))
//				return false;
//		}
//		return true;
//	}
//	//得到随机路径组成的集合，每生成n条路径进行一次判断，直到全覆盖
//	public static void getRandomPathList(int n) {
//		List<List<Transition>> pathSet = new ArrayList<List<Transition>>();
//		
//		
//	}
//	//构建随机可行路径
//	public static List<Transition> randomPath(int start ,int end ,List<Transition> tL,List<Integer> states) throws Exception{
//		//将新的状态加入到该路径的states集合中
//		states.add(start);
//		//t为当前start状态的可用迁移
//		List<Transition> t = new ArrayList<Transition>();
//		for(int i = 0 ; i < m ; i++) {
//			if(matrix[start][i].getTime()!=0) {
//				t.add(matrix[start][i]);
//			}
//		}
//		//到达end就结束
//		if(start!=end) {
//			//可行性flag判断
//			boolean executable = false;
//			//执行循环次数上限，避免迁移进入死循环
//			int n = 50;
//			//用于记录产生迁移后的尾状态
//			int newStart=-1;
//			//执行循环
//			while(!executable&&n>0) {
//				//随机产生下一条迁移
//				Random r = new Random();
//				int random = r.nextInt(t.size());
//				Transition newT = t.get(random);
//				
//				//test为执行性判断的临时List
//				List<Transition> test =new ArrayList<Transition>();
//				test.addAll(tL);
//				test.add(newT);
//				executable=judgement.judge(test);
//				//如果可行则将新的迁移加入到tL中，跳出循环，且记录尾状态
//				if(executable) {
//					tL.add(newT);
//					newStart = newT.getTarget();
//				}
//				n--;
//			}
//	
//			//如果循环结束无可行路径 则丢弃该List
//			if(!executable) {
//				tL = null;
//				return tL;
//			}
//			//如果当前迁移未到达end状态，进入下一个节点进行迭代，直到到达end
//			randomPath(newStart ,end ,tL,states);
//		}
//		return tL;
//	}
//	
//	public static List<Transition> pushList(TransitionList li) {
//		List<Transition> tL = new ArrayList<Transition>();
//		List<Transition> l = li.gettL();
//		for(int i = 0 ; i < li.getSize() ; i++) {
//			tL.add(l.get(i));
//		}
//		return tL;
//	}
	
	
//	public static void main(String[] args) throws Exception{
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
//		
//	}
}
