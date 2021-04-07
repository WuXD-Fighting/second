package algorithm.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.entity.GeneticIndex;
import algorithm.entity.GeneticIndex2;
import algorithm.entity.Path;
import algorithm.entity.ReqEnum;
import algorithm.entity.Set;
import algorithm.entity.Transition;
import algorithm.service.GeneticAlgorithmService;
import algorithm.utils.Judgement;
import algorithm.utils.ListUtil;
import algorithm.utils.RandomUtil;

public class GeneticAlgorithmServiceImpl implements GeneticAlgorithmService{

	static ListUtil listUtil = new ListUtil();
	static RandomUtil randomUtil = new RandomUtil();
	Judgement judgement = new Judgement();
	AnnealingServiceImpl annealingServiceImpl = new AnnealingServiceImpl();
	SetServiceImpl setServiceImpl = new SetServiceImpl();
	GreedyServiceImpl greedyServiceImpl = new GreedyServiceImpl();
	
	public List<Path> crossover(Path path1 , Path path2  , int stateNum) throws Exception{
		//结果列表result
		List<Path> result = new ArrayList<Path>();
		//state1和states2为两条路径的状态列表
		List<Integer> states1 = new ArrayList<Integer>();
		List<Integer> states2 = new ArrayList<Integer>();
		states1.addAll(path1.getStatesList());
		states2.addAll(path2.getStatesList());
		
		List<Transition> newTL1 = new ArrayList<Transition>();
		List<Transition> newTL2 = new ArrayList<Transition>();
		//由于路径可能多次经过某一状态，对两条路径经过stateNum的序号进行随机抽取（下面的index1和index2）
		List<Integer> numList1 = listUtil.searchElement(states1, stateNum);
		List<Integer> numList2 = listUtil.searchElement(states2, stateNum);
		//可行性判断
		boolean flag= false;
		//迭代次数上限，如果无法构成两条可行路径，返回空的list
		int n =10;
		while(!flag&&n-->0) {
			newTL1 = new ArrayList<Transition>();
			newTL2 = new ArrayList<Transition>();
			int index1 = randomUtil.getRandomElement(numList1);
			int index2 = randomUtil.getRandomElement(numList2);
			newTL1.addAll(listUtil.getSubList(path1.gettL(), 0, index1));
			newTL2.addAll(listUtil.getSubList(path2.gettL(), 0, index2));
			newTL1.addAll(listUtil.getSubList(path1.gettL(), index2));
			newTL2.addAll(listUtil.getSubList(path2.gettL(), index1));
			flag = judgement.pathJudge(newTL2)&&judgement.pathJudge(newTL1);
		}
		if(flag==false) {
			return result;
		}else {
			Path newPath1 = new Path(newTL1);
			Path newPath2 = new Path(newTL2);
			result.add(newPath1);
			result.add(newPath2);
			return result;
		}
	}

	public void mutation() {
		
	}

	public void exchang(List<Path> pathSet1 , List<Path> pathSet2 , int n) {
		//临时列表
		List<Path> newPathSet1 = new ArrayList<Path>();
		List<Path> newPathSet2 = new ArrayList<Path>();
		//随机生成包含n个数字（代表交换元素的下标）的列表
		
		List<Integer> exchangeNum1 = randomUtil.getRandomN(n, pathSet1.size());
		List<Integer> exchangeNum2 = randomUtil.getRandomN(n, pathSet2.size());
		//交换
		newPathSet1.addAll(listUtil.getNewListWithoutEle(pathSet1, exchangeNum1));
		newPathSet2.addAll(listUtil.getNewListWithoutEle(pathSet2, exchangeNum2));
		newPathSet1.addAll(listUtil.getNewListInEle(pathSet2, exchangeNum2));
		newPathSet2.addAll(listUtil.getNewListInEle(pathSet1, exchangeNum1));
		
	}
	public void exchang(List<Path> pathSet1, int n1, List<Path> pathSet2, int n2) {
		//临时列表
		List<Path> newPathSet1 = new ArrayList<Path>();
		List<Path> newPathSet2 = new ArrayList<Path>();
		//集合1取n1个元素的下标，集合2取n2个元素的下标，分别存为列表
		List<Integer> exchangeNum1 = randomUtil.getRandomN(n1, pathSet1.size());
		List<Integer> exchangeNum2 = randomUtil.getRandomN(n2, pathSet2.size());
		//交换
		newPathSet1.addAll(listUtil.getNewListWithoutEle(pathSet1, exchangeNum1));
		newPathSet2.addAll(listUtil.getNewListWithoutEle(pathSet2, exchangeNum2));
		newPathSet1.addAll(listUtil.getNewListInEle(pathSet2, exchangeNum2));
		newPathSet2.addAll(listUtil.getNewListInEle(pathSet1, exchangeNum1));
	}


	public List<Set> GeneticAlgorithm(GeneticIndex index) throws Exception{
		List<Set> operateList = new ArrayList<Set>();
		operateList.addAll(setServiceImpl.selectShortN(index.getSetList(), index.getShortN()));
		List<Integer> numList = index.getNumList();
		int times = index.getTimes();
		while(0<times--) {
			Random r = new Random();
			int action = r.nextInt(5);
			if(action == 0) {
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				//随机产生的状态不能为末状态
				int stateNum = r.nextInt(index.getMatrix().length-1)+0;
				Path path1 = setServiceImpl.selectPathWithState(set1, stateNum);
				Path path2 = setServiceImpl.selectPathWithState(set2, stateNum);
				List<Path> newPath=crossover(path1, path2, stateNum);
				if(!newPath.isEmpty()) {
					set1.addPathList(newPath);
					set2.addPathList(newPath);
					setServiceImpl.completeSet(numList, set1, operateList);
					setServiceImpl.completeSet(numList, set2, operateList);
				}
			}else if(action == 1) {
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				List<Path> newPath1= new ArrayList<Path>();
				List<Path> newPath2= new ArrayList<Path>();
				newPath1.addAll(set1.getPathSet());
				newPath2.addAll(set2.getPathSet());
				//最多交换3/4,取路径数少的数量
				int max = (newPath1.size()>newPath2.size())?(3*newPath2.size()/4):(3*newPath1.size()/4);
				exchang(newPath1,newPath2,max);
				Set newSet1 = new Set(newPath1);
				Set newSet2 = new Set(newPath2);
				setServiceImpl.completeSet(numList, newSet1, operateList);
				setServiceImpl.completeSet(numList, newSet2, operateList);
				operateList.add(newSet1);
				operateList.add(newSet2);
			}else if(action == 2) {
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				List<Path> newPath1= new ArrayList<Path>();
				List<Path> newPath2= new ArrayList<Path>();
				newPath1.addAll(set1.getPathSet());
				newPath2.addAll(set2.getPathSet());
				int max1 = 3/4*newPath1.size();
				int max2 = 3/4*newPath2.size();
				exchang(newPath1, max1, newPath2, max2);
				Set newSet1 = new Set(newPath1);
				Set newSet2 = new Set(newPath2);
				setServiceImpl.completeSet(numList, newSet1, operateList);
				setServiceImpl.completeSet(numList, newSet2, operateList);
				operateList.add(newSet1);
				operateList.add(newSet2);
			}else if(action == 3) {
				int n = r.nextInt(operateList.size());
				List<Integer> num = randomUtil.getRandomN(n ,operateList.size());
				for(int i : num) {
					List<Path> pathSet = new ArrayList<Path>();
					List<Integer> states = new ArrayList<Integer>();
					int count = r.nextInt(operateList.get(i).getSize()/2);
					if(count<1)
						count = 1 ;
					annealingServiceImpl.getRandomPathList(index.getMatrix(), 0, index.getMatrix().length-1, states, pathSet, count);
					operateList.get(i).addPathList(pathSet);
				}
			}
			else if(action == 4) {
				//增加新集合
				Set tempSet = new Set();
				while(tempSet.getPathSet().isEmpty()) {
					List<Integer> tempStates = new ArrayList<Integer>();
					List<Path> temp = new ArrayList<Path>();
					//生成随机20条path
					annealingServiceImpl.getRandomPathList(index.getMatrix(), 0, index.getMatrix().length-1, tempStates, temp, 20);
					//拿到最小覆盖集，如果不能完全覆盖，得到空
					tempSet.setPathSet(setServiceImpl.getPathSets(numList,temp));
				}
				operateList.add(tempSet);
			}
			
			// 一定概率对所有路径集合执行贪心覆盖
			if(randomUtil.actionChance(40, 1)) {
				System.out.println("greed");
				for(Set set:operateList) {
					set.setPathSet(setServiceImpl.getPathSets(index.getNumList(),set.getPathSet()));
				}
			}
			//执行一定次数后（概率）挑选前n
			if(randomUtil.actionChance(40, 1)) {
				System.out.println("select");
				operateList=setServiceImpl.duplicationRemoving(operateList);
				operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
			}
		}
		//循环执行完成后每个集合挑选覆盖路径集
		for(Set set:operateList) {
			set.setPathSet(setServiceImpl.getPathSets(index.getNumList(),set.getPathSet()));
		}
		operateList=setServiceImpl.duplicationRemoving(operateList);
		operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
		return operateList;
	}
	
	public List<Set> GeneticAlgorithm(GeneticIndex2 index) throws Exception{
		List<Set> operateList = new ArrayList<Set>();
		operateList.addAll(setServiceImpl.selectShortN(index.getSetList(), index.getShortN()));
		List<ReqEnum> reqs = index.getReqs();
		int staticTimes = index.getTimes();
		int times = staticTimes;
		Set bestSet = new Set(operateList.get(0).getPathSet());
		while(0<times) {
			System.out.println(times);
			Random r = new Random();
			int action = r.nextInt(5);
			if(action == 0) {
				System.out.println("crossover");
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				//随机产生的状态不能为末状态
				int stateNum = r.nextInt(index.getMatrix().length-1)+0;
				Path path1 = setServiceImpl.selectPathWithState(set1, stateNum);
				Path path2 = setServiceImpl.selectPathWithState(set2, stateNum);
				List<Path> newPath=crossover(path1, path2, stateNum);
				if(!newPath.isEmpty()) {
					set1.addPathList(newPath);
					set2.addPathList(newPath);
					setServiceImpl.completeSetByReqs(reqs, set1, operateList);
					setServiceImpl.completeSetByReqs(reqs, set2, operateList);
				}
			}else if(action == 1) {
				System.out.println("exchange");
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				List<Path> newPath1= new ArrayList<Path>();
				List<Path> newPath2= new ArrayList<Path>();
				newPath1.addAll(set1.getPathSet());
				newPath2.addAll(set2.getPathSet());
				//最多交换3/4,取路径数少的数量
				int max = (newPath1.size()>newPath2.size())?(3*newPath2.size()/4):(3*newPath1.size()/4);
				exchang(newPath1,newPath2,max);
				Set newSet1 = new Set(newPath1);
				Set newSet2 = new Set(newPath2);
				setServiceImpl.completeSetByReqs(reqs, newSet1, operateList);
				setServiceImpl.completeSetByReqs(reqs, newSet2, operateList);
				operateList.add(newSet1);
				operateList.add(newSet2);
			}else if(action == 2) {
				System.out.println("exchange2");
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				List<Path> newPath1= new ArrayList<Path>();
				List<Path> newPath2= new ArrayList<Path>();
				newPath1.addAll(set1.getPathSet());
				newPath2.addAll(set2.getPathSet());
				int max1 = 3/4*newPath1.size();
				int max2 = 3/4*newPath2.size();
				exchang(newPath1, max1, newPath2, max2);
				Set newSet1 = new Set(newPath1);
				Set newSet2 = new Set(newPath2);
				setServiceImpl.completeSetByReqs(reqs, newSet1, operateList);
				setServiceImpl.completeSetByReqs(reqs, newSet2, operateList);
				operateList.add(newSet1);
				operateList.add(newSet2);
			}else if(action == 3) {
				System.out.println("add");
				int n = r.nextInt(operateList.size());
				List<Integer> num = randomUtil.getRandomN(n ,operateList.size());
				for(int i : num) {
					List<Path> pathSet = new ArrayList<Path>();
					List<Integer> states = new ArrayList<Integer>();
					int count = r.nextInt(operateList.get(i).getSize()/2);
					if(count<1)
						count = 1 ;
					annealingServiceImpl.getRandomPathList(index.getMatrix(), 0, index.getMatrix().length-1, states, pathSet, count);
					operateList.get(i).addPathList(pathSet);
				}
			}
			else if(action == 4) {
				//增加新集合
				Set tempSet = new Set();
				while(tempSet.getPathSet().isEmpty()) {
					List<Integer> tempStates = new ArrayList<Integer>();
					List<Path> temp = new ArrayList<Path>();
					//生成随机20条path
					annealingServiceImpl.getRandomPathList(index.getMatrix(), 0, index.getMatrix().length-1, tempStates, temp, 20);
					//拿到最小覆盖集，如果不能完全覆盖，得到空
					tempSet.setPathSet(setServiceImpl.getPathSetsByReqs(reqs,temp));
				}
				operateList.add(tempSet);
			}
			
			// 一定概率对所有路径集合执行贪心覆盖
//			if(randomUtil.actionChance(40, 1)) {
//				System.out.println("greed");
//				for(Set set:operateList) {
//					set.setPathSet(setServiceImpl.getPathSetsByReqs(index.getReqs(),set.getPathSet()));
//				}
//			}
			//执行一定次数后（概率）挑选前n
//			if(randomUtil.actionChance(40, 1)) {
//				System.out.println("select");
//				operateList=setServiceImpl.duplicationRemoving(operateList);
//				operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
//			}
			
			for(Set set:operateList) {
				set.setPathSet(setServiceImpl.getPathSetsByReqs(index.getReqs(),set.getPathSet()));
			}
			operateList=setServiceImpl.duplicationRemoving(operateList);
			operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
			if(operateList.get(0).getSumTime()<bestSet.getSumTime()) {
				bestSet = new Set(operateList.get(0).getPathSet());
				times = staticTimes;
			}else {
				times--;
			}
			
		}
		//循环执行完成后每个集合挑选覆盖路径集
		for(Set set:operateList) {
			set.setPathSet(setServiceImpl.getPathSetsByReqs(index.getReqs(),set.getPathSet()));
		}
		operateList=setServiceImpl.duplicationRemoving(operateList);
		operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
		return operateList;
	}
	
	public List<Set> GeneticAlgorithm3(GeneticIndex2 index) throws Exception{
		int end = index.getEnd();
		List<Set> operateList = new ArrayList<Set>();
		operateList.addAll(setServiceImpl.selectShortN(index.getSetList(), index.getShortN()));
		List<ReqEnum> reqs = index.getReqs();
		int staticTimes = index.getTimes();
		int times = staticTimes;
		Set bestSet = new Set(operateList.get(0).getPathSet());
		while(0<times) {
			Random r = new Random();
			int action = r.nextInt(5);
			if(action == 0) {
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				//随机产生的状态不能为末状态
				int stateNum = r.nextInt(index.getMatrix().length)+0;
				while(stateNum == index.getEnd()) {
					stateNum = r.nextInt(index.getMatrix().length)+0;
				}
				Path path1 = setServiceImpl.selectPathWithState(set1, stateNum);
				Path path2 = setServiceImpl.selectPathWithState(set2, stateNum);
				List<Path> newPath=crossover(path1, path2, stateNum);
				if(!newPath.isEmpty()) {
					set1.addPathList(newPath);
					set2.addPathList(newPath);
					setServiceImpl.completeSetByReqs(reqs, set1, operateList);
					setServiceImpl.completeSetByReqs(reqs, set2, operateList);
				}
			}else if(action == 1) {
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				List<Path> newPath1= new ArrayList<Path>();
				List<Path> newPath2= new ArrayList<Path>();
				newPath1.addAll(set1.getPathSet());
				newPath2.addAll(set2.getPathSet());
				//最多交换3/4,取路径数少的数量
				int max = (newPath1.size()>newPath2.size())?(3*newPath2.size()/4):(3*newPath1.size()/4);
				exchang(newPath1,newPath2,max);
				Set newSet1 = new Set(newPath1);
				Set newSet2 = new Set(newPath2);
				setServiceImpl.completeSetByReqs(reqs, newSet1, operateList);
				setServiceImpl.completeSetByReqs(reqs, newSet2, operateList);
				operateList.add(newSet1);
				operateList.add(newSet2);
			}else if(action == 2) {
				List<Integer> num = randomUtil.getRandomN(2, operateList.size());
				Set set1 = operateList.get(num.get(0));
				Set set2 = operateList.get(num.get(1));
				List<Path> newPath1= new ArrayList<Path>();
				List<Path> newPath2= new ArrayList<Path>();
				newPath1.addAll(set1.getPathSet());
				newPath2.addAll(set2.getPathSet());
				int max1 = 3/4*newPath1.size();
				int max2 = 3/4*newPath2.size();
				exchang(newPath1, max1, newPath2, max2);
				Set newSet1 = new Set(newPath1);
				Set newSet2 = new Set(newPath2);
				setServiceImpl.completeSetByReqs(reqs, newSet1, operateList);
				setServiceImpl.completeSetByReqs(reqs, newSet2, operateList);
				operateList.add(newSet1);
				operateList.add(newSet2);
			}else if(action == 3) {
				int n = r.nextInt(operateList.size());
				List<Integer> num = randomUtil.getRandomN(n ,operateList.size());
				for(int i : num) {
					List<Path> pathSet = new ArrayList<Path>();
					List<Integer> states = new ArrayList<Integer>();
					int count = r.nextInt(operateList.get(i).getSize()/2);
					if(count<1)
						count = 1 ;
					annealingServiceImpl.getRandomPathList(index.getMatrix(), 0, end, states, pathSet, count);
					operateList.get(i).addPathList(pathSet);
				}
			}
			else if(action == 4) {
				//增加新集合
				Set tempSet = new Set();
				while(tempSet.getPathSet().isEmpty()) {
					List<Integer> tempStates = new ArrayList<Integer>();
					List<Path> temp = new ArrayList<Path>();
					//生成随机20条path
					annealingServiceImpl.getRandomPathList(index.getMatrix(), 0, end, tempStates, temp, 20);
					//补全集合
					while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
						List<Path> newPath = new ArrayList<Path>();
						annealingServiceImpl.getRandomPathList(index.getMatrix(), 0, end, tempStates, newPath,1);
						temp.addAll(newPath);
					}
					
					tempSet.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, temp, 2));
				}
				operateList.add(tempSet);
			}
			
			// 一定概率对所有路径集合执行贪心覆盖
//			if(randomUtil.actionChance(40, 1)) {
//				System.out.println("greed");
//				for(Set set:operateList) {
//					set.setPathSet(setServiceImpl.getPathSetsByReqs(index.getReqs(),set.getPathSet()));
//				}
//			}
			//执行一定次数后（概率）挑选前n
//			if(randomUtil.actionChance(40, 1)) {
//				System.out.println("select");
//				operateList=setServiceImpl.duplicationRemoving(operateList);
//				operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
//			}
			//System.out.println(operateList.size());
			for(Set set:operateList) {
				//System.out.println("集合路径数量："+set.getSize()+" 是否覆盖："+setServiceImpl.isCoveredByReqs(reqs, set.getPathSet()) );
				set.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, set.getPathSet(), 2));
			}
			operateList=setServiceImpl.duplicationRemoving(operateList);
			operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
			if(operateList.get(0).getSumTime()<bestSet.getSumTime()) {
				bestSet = new Set(operateList.get(0).getPathSet());
				times = staticTimes;
			}else {
				times--;
			}
			
		}
		//循环执行完成后每个集合挑选覆盖路径集
		for(Set set:operateList) {
			set.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, set.getPathSet(), 2));
		}
		operateList=setServiceImpl.duplicationRemoving(operateList);
		operateList = setServiceImpl.selectShortN( operateList , index.getShortN());
		return operateList;
	}
	public static void main(String[] args) {
		List<Integer> result = new ArrayList<Integer>();
		result = randomUtil.getRandomN(10, 0, 10);
		System.out.println(result);
		
		
		List<List<Integer>> result2 = new ArrayList<List<Integer>>();
		while(result2.size()<10) {
			List<Integer> resultTemp = new ArrayList<Integer>();
			resultTemp.add(result2.size());
			result2.add(resultTemp);
		}
		for(List<Integer> L:result2) {
			System.out.println(L);
		}
	}


}
