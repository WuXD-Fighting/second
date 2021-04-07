package algorithm.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algorithm.entity.Transition;
import algorithm.service.Impl.AnnealingServiceImpl;
import algorithm.service.Impl.GeneticAlgorithmServiceImpl;
import algorithm.service.Impl.GreedyServiceImpl;
import algorithm.service.Impl.SetServiceImpl;
import algorithm.entity.Count;
import algorithm.entity.GeneticIndex;
import algorithm.entity.GeneticIndex2;
import algorithm.entity.Path;
import algorithm.entity.ReqEnum;
import algorithm.entity.Set;
import algorithm.utils.GetTransitionByXml;
import algorithm.utils.Judgement;
import algorithm.utils.RequirementUtil;

public class GeneticAction3 {
	AnnealingServiceImpl annealingServiceImpl = new AnnealingServiceImpl();
	SetServiceImpl setServiceImpl = new SetServiceImpl();
	GeneticAlgorithmServiceImpl geneticAlgorithmServiceImpl = new GeneticAlgorithmServiceImpl();
	RequirementUtil requirementUtil = new RequirementUtil();
	GreedyServiceImpl greedyServiceImpl = new GreedyServiceImpl();
	Transition[][] matrix ;//迁移矩阵
	Double[][] distance ;//距离矩阵
	Judgement judgement = new Judgement();//判断可行性工具类
	int m = 24;//状态数
	List<Path> pathSet;//路径集合
//	static List<Integer> numList;//需要覆盖的迁移集合
	//改为需求迁移集合
	static List<ReqEnum> reqs = new ArrayList<ReqEnum>();
	static List<List<Path>> pathSetList;
	
	public void genetic() throws Exception{
		//初始化
		pathSet = new ArrayList<Path>();
		pathSetList = new ArrayList<List<Path>>();
		List<Transition> tL = new ArrayList<Transition>();
		tL=GetTransitionByXml.getTransitionList();
//		numList = new ArrayList<Integer>();
//		numList=GetTransitionByXml.getNumSet(tL);
		reqs = new ArrayList<ReqEnum>();
		reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,[38,40,42,44],48,[50,52],53,54");
		matrix = new Transition[m][m];
		distance = new Double[m][m]; 
		annealingServiceImpl.initialMatrix(tL, matrix, distance);
		//List<Integer> statesList = new ArrayList<Integer>();
		//path集合 相当于遗传算法的基因链
		List<Set> setList = new ArrayList<Set>();
		//辅助链表
		List<Integer> states = new ArrayList<Integer>();
		List<Path> temp = new ArrayList<Path>();
		//循环产生set直到有n个set
		while(setList.size()<30) {
			states = new ArrayList<Integer>();
			temp = new ArrayList<Path>();
			Set tempSet = new Set();
			//生成随机n条path
			annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, temp,50);
			while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
				List<Path> newPath = new ArrayList<Path>();
				annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, newPath,1);
				temp.addAll(newPath);
			}
			//拿到最小覆盖集，如果不能完全覆盖，得到空
			
			//tempSet.setPathSet(setServiceImpl.getPathSetsByReqs(reqs,temp));
			tempSet.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, temp, 2));
			//如果tempSet不为空，加入到setList
			if(!tempSet.getPathSet().isEmpty()) {
				setList.add(tempSet);
			}
		}
		GeneticIndex2 geneticIndex = new GeneticIndex2();
		geneticIndex.setSetList(setList);
		geneticIndex.setShortN(10);
		geneticIndex.setMatrix(matrix);
		geneticIndex.setTimes(1000);
		geneticIndex.setGreedyProbability(0);
		//geneticIndex.setNumList(numList);
		geneticIndex.setReqs(reqs);
		geneticIndex.setEnd(21);
		List<Set> result = new ArrayList<Set>();
		result.addAll(geneticAlgorithmServiceImpl.GeneticAlgorithm3(geneticIndex));
		for(Set set:result) {
			System.out.println("-------------------------------------------");
			for(int i = 0 ; i < set.getSize() ; i++) 
				System.out.println("第"+i+"条路径用时："+set.get(i).getSumTim());
			System.out.println("测试总用时："+set.getSumTime());
		}
		
		
	}
	
	public void randomSeed()throws Exception {
		//初始化
		pathSet = new ArrayList<Path>();
		pathSetList = new ArrayList<List<Path>>();
		List<Transition> tL = new ArrayList<Transition>();
		tL=GetTransitionByXml.getTransitionList();
		reqs = new ArrayList<ReqEnum>();
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18],[7,8,11],15,16,17,19,20");
		reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,[38,40,42,44],48,[50,52],53,54");
		//16
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26");
		//18
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28");
		//25
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35");

		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,[38,40,42,44],48,[50,52]");
		matrix = new Transition[m][m];
		distance = new Double[m][m]; 
		annealingServiceImpl.initialMatrix(tL, matrix, distance);
		List<Path> temp = new ArrayList<Path>();
		List<Integer> states = new ArrayList<Integer>();
		//初始生成50条路径
		annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, temp,50);
		//如果不能完成包含 则新增路径
		while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
			List<Path> newPath = new ArrayList<Path>();
			annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, newPath,1);
			temp.addAll(newPath);
		}
		//比较次数 经过20次比较后当前结果任然为最好结果，则判断当前为最佳值
		int m = 100;
		//oldPathSet用于记录当前最小pathSet
		List<Path> oldPathSet = new ArrayList<Path>();
		//oldPathSet = setServiceImpl.getPathSetsByReqs(reqs, temp);
		oldPathSet = greedyServiceImpl.bigStepGreedy(reqs, temp, 2);
		Set oldSet = new Set(oldPathSet);
		
		for(int i = 0 ;i <oldSet.getSize() ; i++) {
			System.out.println("第"+i+"条路径："+oldSet.get(i).getSet());
		}
		System.out.println("测试总用时:"+oldSet.getSumTime());
		while(m>0) {
			System.out.println("m:"+m);
			List<Path> newPath = new ArrayList<Path>();
			annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, newPath,1);
			temp.addAll(newPath);
			List<Path> pathSet = new ArrayList<Path>();
			//pathSet = setServiceImpl.getPathSetsByReqs(reqs, temp);
			pathSet = greedyServiceImpl.bigStepGreedy(reqs, temp, 2);
			Set set = new Set(pathSet);
			
			if(set.getSumTime()<oldSet.getSumTime()) {
				for(int i = 0 ;i <set.getSize() ; i++) {
					System.out.println("第"+i+"条路径："+set.get(i).getSet());
				}
				System.out.println("测试总用时:"+set.getSumTime());
				
				
				oldPathSet =  new ArrayList<Path>();
				oldPathSet.addAll(pathSet);
				oldSet = new Set(oldPathSet);
				//重置比较次数
				m = 100;
			}else {
				m--;
			}
		}
		int l =0 ;
		for(int i = 0 ;i <oldSet.getSize() ; i++) {
		System.out.println("第"+i+"条路径："+oldSet.get(i).getSet() + "用时："+oldSet.get(i).getSumTim());
		l += oldSet.get(i).getSize();
		}
		System.out.println("测试总用时:"+oldSet.getSumTime());
		System.out.println("测试路径总长度："+l);
//		List<Path> pathSet = new ArrayList<Path>();
//		pathSet = setServiceImpl.getPathSetsByReqs(reqs, temp);
//		Set set = new Set(pathSet);
//		System.out.println(setServiceImpl.isCoveredByReqs(reqs, pathSet));
//		for(int i = 0 ;i <set.getSize() ; i++) {
//			System.out.println("第"+i+"条路径："+set.get(i).getSet());
//		}
//		System.out.println("测试总用时:"+set.getSumTime());
	}
	
	public void randomSeed2()throws Exception {
		Map<Integer , Count> map = new HashMap<Integer , Count>();
		for(int i = 50 ; i <= 250 ; i++) {
			map.put(i, new Count());
		}
		int c = 0 ;
		while(c++<5) {
			int be = 50;
			//初始化
			pathSet = new ArrayList<Path>();
			pathSetList = new ArrayList<List<Path>>();
			List<Transition> tL = new ArrayList<Transition>();
			tL=GetTransitionByXml.getTransitionList();
			reqs = new ArrayList<ReqEnum>();
			//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18],[7,8,11],15,16,17,19,20");
			reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,[38,40,42,44],48,[50,52],53,54");
			//16
			//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26");
			matrix = new Transition[m][m];
			distance = new Double[m][m]; 
			annealingServiceImpl.initialMatrix(tL, matrix, distance);
			List<Path> temp = new ArrayList<Path>();
			List<Integer> states = new ArrayList<Integer>();
			//初始生成50条路径
			annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, temp,be);
			//如果不能完成包含 则新增路径
			while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
				List<Path> newPath = new ArrayList<Path>();
				annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, newPath,1);
				temp.addAll(newPath);
			}
			be = temp.size();
			while(be<=250) {
				//oldPathSet用于记录当前最小pathSet
				List<Path> oldPathSet = new ArrayList<Path>();
				//oldPathSet = setServiceImpl.getPathSetsByReqs(reqs, temp);
				oldPathSet = greedyServiceImpl.bigStepGreedy(reqs, temp, 3);
				Set oldSet = new Set(oldPathSet);
				
				for(int i = 0 ;i <oldSet.getSize() ; i++) {
					System.out.println("第"+i+"条路径："+oldSet.get(i).getSet());
				}
				System.out.println("测试总用时:"+oldSet.getSumTime());
				
				Count count = map.get(be);
				int g = count.getCount();
				count.add(oldSet.getSumTime());
				double ave = count.getAve();
				ave = (ave*g + oldSet.getSumTime())/(g+1);
				g = g+1;
				count.setAve(ave);
				count.setCount(g);
				count.setN(be);
				map.replace(be, count);
				
				
				annealingServiceImpl.getRandomPathList(matrix, 0, 21,  states, temp,be+2);
				be = temp.size();
			}
		}
		for(int i = 50 ; i <= 400 ; i ++) {
			if(map.get(i).getCount() !=0 )
				System.out.println(map.get(i).toString());
		}
	}
	
	public static void main(String[] args) throws Exception{
		GeneticAction3 g = new GeneticAction3();
		g.randomSeed2();
		
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
