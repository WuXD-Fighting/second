package algorithm.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import algorithm.entity.SubpopulationEntity;
import algorithm.entity.Transition;
import algorithm.service.MPGAService;
import algorithm.utils.Judgement;
import algorithm.utils.ListUtil;
import algorithm.utils.RandomUtil;
import algorithm.entity.Count;
import algorithm.entity.MPGAEntity;
import algorithm.entity.Path;
import algorithm.entity.ReqEnum;
import algorithm.entity.Set;

public class MPGAServiceImpl implements MPGAService{
	
	RandomUtil randomUtil = new RandomUtil();
	ListUtil listUtil = new ListUtil();
	SetServiceImpl setServiceImpl = new SetServiceImpl();
	AnnealingServiceImpl annealingServiceImpl = new AnnealingServiceImpl();
	Judgement judgement = new Judgement();
	GreedyServiceImpl greedyServiceImpl = new GreedyServiceImpl();
	public Set MPGA(MPGAEntity MPGAIndex) {
		Map<Integer , Count> map = new HashMap<Integer , Count>();
		for(int i = 1 ; i <= 1000 ; i++) {
			map.put(i, new Count());
		}
		//总体种群
		List<List<Set>> population = new ArrayList<List<Set>>();
		//起始状态
		int end = MPGAIndex.getEnd();
		//末状态
		int start = MPGAIndex.getStart();
		//需求覆盖集
		List<ReqEnum> reqs = new ArrayList<ReqEnum>();
		reqs = MPGAIndex.getReqs();
		//贪心算法参数
		int bigStepGreedyIndex = MPGAIndex.getBigStepGreedyIndex();
		//构建种群
		while(population.size() < MPGAIndex.getPopulationCount()) {
			List<Set> subpopulation = new ArrayList<Set>();
			//构建子种群
			while(subpopulation.size() < MPGAIndex.getPopulationSize()) {
				//生成种群的基因
				List<Integer> states = new ArrayList<Integer>();
				List<Path> temp = new ArrayList<Path>();
				annealingServiceImpl.getRandomPathList(MPGAIndex.getMatrix(), start , end,  states, temp,50);
				//如果不能完全覆盖 则新增路径
				while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
					List<Path> newPath = new ArrayList<Path>();
					annealingServiceImpl.getRandomPathList(MPGAIndex.getMatrix(), start, end,  states, newPath,1);
					temp.addAll(newPath);
				}
				//覆盖集挑选
				temp = greedyServiceImpl.bigStepGreedy(reqs, temp, bigStepGreedyIndex);
				//构造基因
				Set newSet = new Set(temp);
				if(!subpopulation.contains(newSet))
					subpopulation.add(newSet);
			}
			population.add(subpopulation);
			System.out.println("population:"+ population.size());
		}
		//结果确认比较次数
		int confirm = MPGAIndex.getConfirm();
		//当前最好结果 初始化为任意一个集合
		Set bestSet = new Set();
		bestSet = population.get(0).get(0);
		//迁移间隔
		int interval = MPGAIndex.getInterval();
		//迁移数量
		int transfer = MPGAIndex.getTransfer();
		//迭代数
		int n = 1;
		System.out.println("confirm:"+confirm);
		while(confirm > 0) {
			System.out.println("n:"+n);
			//各种群执行遗传算法
			for(int i = 0 ; i <population.size() ; i++ ) {
				SubpopulationEntity subpopulationEntity = new SubpopulationEntity();
				subpopulationEntity.setEnd(end);
				subpopulationEntity.setStart(start);
				subpopulationEntity.setMatrix(MPGAIndex.getMatrix());
				subpopulationEntity.setReqs(reqs);
				subpopulationEntity.setPc1(MPGAIndex.getPc1());
				subpopulationEntity.setPc2(MPGAIndex.getPc2());
				subpopulationEntity.setPm1(MPGAIndex.getPm1());
				subpopulationEntity.setPm2(MPGAIndex.getPm2());
				subpopulationEntity.setSetList(population.get(i));
				subpopulationEntity.setChampionshipIndex(MPGAIndex.getChampionshipIndex());
				subpopulationEntity.setChampionshipSize(MPGAIndex.getChampionshipSize());
				subpopulationEntity.setPopulationSize(MPGAIndex.getPopulationSize());
				try {
					population.set(i, subpopulation(subpopulationEntity));
					//各自产生下一代结束后 如果集合的个体数量少于规定数量 需要对集合进行补充
					while(population.get(i).size() < MPGAIndex.getPopulationSize()) {
						//生成种群的基因
						List<Integer> states = new ArrayList<Integer>();
						List<Path> temp = new ArrayList<Path>();
						annealingServiceImpl.getRandomPathList(MPGAIndex.getMatrix(), start , end,  states, temp,50);
						//如果不能完全覆盖 则新增路径
						while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
							List<Path> newPath = new ArrayList<Path>();
							annealingServiceImpl.getRandomPathList(MPGAIndex.getMatrix(), start, end,  states, newPath,1);
							temp.addAll(newPath);
						}
						//覆盖集挑选
						temp = greedyServiceImpl.bigStepGreedy(reqs, temp, bigStepGreedyIndex);
						//构造基因
						Set newSet = new Set(temp);
						population.get(i).add(newSet);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("子种群遗传赋值位置出错");
				}
				
				
				//各自产生下一代结束后 如果集合的个体数量少于规定数量 需要对集合进行补充
				while(population.get(i).size() < MPGAIndex.getPopulationSize()) {
					System.out.println("population.get(i).size():"+population.get(i).size() + " MPGAIndex.getPopulationSize():"+ MPGAIndex.getPopulationSize());
					System.out.println("补充");
					//生成种群的基因
					List<Integer> states = new ArrayList<Integer>();
					List<Path> temp = new ArrayList<Path>();
					annealingServiceImpl.getRandomPathList(MPGAIndex.getMatrix(), start , end,  states, temp,50);
					//如果不能完全覆盖 则新增路径
					while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
						List<Path> newPath = new ArrayList<Path>();
						annealingServiceImpl.getRandomPathList(MPGAIndex.getMatrix(), start, end,  states, newPath,1);
						temp.addAll(newPath);
					}
					//覆盖集挑选
					temp = greedyServiceImpl.bigStepGreedy(reqs, temp, bigStepGreedyIndex);
					//构造基因
					Set newSet = new Set(temp);
					population.get(i).add(newSet);
				}
				population.set(i, setServiceImpl.selectShortN( population.get(i) ,  MPGAIndex.getPopulationSize() ));
				System.out.println("第"+i+"种群第"+n+"代最好个体总时间："+population.get(i).get(0).getSumTime());
			}
			//到了迁移间隔 进行迁移
			if(n%interval == 0) {
				for(int i = 0 ; i <population.size() ; i++ ) {
					try {
						//迁移前先进行淘汰、排序
//						for(int j = 0 ; j < population.get(i).size() ; j++ ) {
//							System.out.println( "p:"+i+" s:"+ j + "  time:" + population.get(i).get(j).getSumTime()  );
//						}
						population.set(i, setServiceImpl.selectShortN( population.get(i) ,  MPGAIndex.getPopulationSize() ));
//						for(int j = 0 ; j < population.get(i).size() ; j++ ) {
//							System.out.println( "p:"+i+" s:"+ j + "  time:" + population.get(i).get(j).getSumTime()  );
//						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("迁移赋值位置出错");
					}
					System.out.println("population.get(i) best:"+population.get(i).get(0).getSumTime());
					//如果存在更好的结果  重置确认比较数
					if(population.get(i).get(0).getSumTime() < bestSet.getSumTime()) {
						System.out.println("迁移:"+(new Date()).toString());
						bestSet = new Set(population.get(i).get(0).getPathSet());
						confirm = MPGAIndex.getConfirm();
					}
				}
				cyclicTransfer(population, transfer);
				confirm--;
				System.out.println("confirm:"+confirm);
				
				
				Count count = map.get(n);
				int g = count.getCount();
				count.add(bestSet.getSumTime());
				double ave = count.getAve();
				ave = (ave*g + bestSet.getSumTime())/(g+1);
				g = g+1;
				count.setAve(ave);
				count.setCount(g);
				count.setN(n);
				map.replace(n, count);
			}
			n++;
		}
		System.out.println("迭代数："+n);
		int j = 1000;
		for(int i = 1 ; i < j ; i ++) {
			if(map.get(i).getCount() !=0 )
				System.out.println(map.get(i).toString());
		}
		return bestSet;
		
	}
	
	private List<Set> subpopulation(SubpopulationEntity index) throws Exception{
		List<Set> operateList = new ArrayList<Set>();
		operateList = index.getSetList();
		List<ReqEnum> reqs = index.getReqs();
		double maxFit = Double.NEGATIVE_INFINITY;
		double avgFit = 0;
		double sumFit = 0;
		double pc1 = index.getPc1();
		double pc2 = index.getPc2();
		double pm1 = index.getPm1();
		double pm2 = index.getPm2();
		
		operateList = championship(index.getChampionshipIndex(), index.getChampionshipSize(), operateList);
		
		for(Set set :operateList) {
			sumFit += set.getSumTime();
			if((-set.getSumTime())>maxFit) {
				maxFit = -set.getSumTime();
			}
		}
		
		avgFit = sumFit/operateList.size()*(-1);
		//新增交叉集合
		List<Set> newCrossoverSet = new ArrayList<Set>();
		
		//交叉
		while(operateList.size() >= 2 ) {
			/*
			 * 随机选择2个元素 进行无放回的交叉
			 * 若交叉判定为否 则适应值较大的直接保留 小的放回
			*/
			List<Integer> numList = new ArrayList<Integer>();
			numList.addAll(randomUtil.getRandomN(2, operateList.size()));
			
			int first = numList.get(0);
			int second = numList.get(1);
			
			if(ifCrossover(operateList.get(first),operateList.get(second),maxFit,avgFit,pc1,pc2)) {
				
				if(!newCrossoverSet.contains(operateList.get(first))) {
					newCrossoverSet.add(operateList.get(first));
				}
				if(!newCrossoverSet.contains(operateList.get(second))) {
					newCrossoverSet.add(operateList.get(second));
				}
				
				List<Set> temp  = new ArrayList<Set>();
				temp = crossover(operateList.get(first),operateList.get(second));
				for(Set set : temp ) {
					if(!newCrossoverSet.contains(set)) {
						newCrossoverSet.add(set);
					}
				}
				if(first>second) {
					operateList.remove(first);
					operateList.remove(second);
				}else {
					operateList.remove(second);
					operateList.remove(first);
				}
			}else {
				if(operateList.get(first).getSumTime() < operateList.get(second).getSumTime()) {
					if(!newCrossoverSet.contains(operateList.get(first)))
						newCrossoverSet.add(operateList.get(first));
					operateList.remove(first);
				}else {
					if(!newCrossoverSet.contains(operateList.get(second)))
						newCrossoverSet.add(operateList.get(second));
					operateList.remove(second);
				}
			}
		}
		if(!operateList.isEmpty()) {
			newCrossoverSet.addAll(operateList);
		}
		
		operateList = new ArrayList<Set>();
		operateList.addAll(newCrossoverSet);
		
		//补全所有集合
		for(Set set : operateList) {
			List<Path> temp = set.getPathSet();
			while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
				List<Path> newPath = new ArrayList<Path>();
				List<Integer> states = new ArrayList<Integer>();
				annealingServiceImpl.getRandomPathList(index.getMatrix(), index.getStart(), index.getEnd(),  states, newPath,1);
				temp.addAll(newPath);
			}
			set = new Set(temp);
		}
		
		//交叉补全后进行一次贪心
		for(Set set:operateList) {
			set.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, set.getPathSet(), 2));
		}
		
//		for(int i = 0 ; i < operateList.size() ; i++) {
//			for(int j = i+1 ; j < operateList.size() ; j++ ) {
//				if(ifCrossover(operateList.get(i),operateList.get(j),maxFit,avgFit,pc1,pc2)) {
//					System.out.println("crossover:"+(new Date()).toString());
//					newCrossoverSet.addAll(crossover(operateList.get(i),operateList.get(j)));
//				}else {
//					
//				}
//			}
//		}
		
		for(Set set : operateList) {
			setServiceImpl.completeSetByReqs(reqs, set, operateList);
		}
		
		//新增变异集合
		List<Set> newMutationSet = new ArrayList<Set>();
		//变异
		for(int i = 0 ; i < operateList.size() ; i++) {
			if(ifMutation(operateList.get(i), maxFit, avgFit, pm1, pm2)) {
				Set newSet = new Set();
				newSet = mutation(operateList.get(i), index);
				if(!newSet.getPathSet().isEmpty()) {
					newMutationSet.add(newSet);
				}
			}
		}
		
		
		operateList.addAll(newMutationSet);
		
		Random r = new Random();
		int action = r.nextInt(10); 
		while(action-- >0) {
			Set tempSet = new Set();
			while(tempSet.getPathSet().isEmpty()) {
				List<Integer> tempStates = new ArrayList<Integer>();
				List<Path> temp = new ArrayList<Path>();
				//生成随机20条path
				annealingServiceImpl.getRandomPathList(index.getMatrix(), index.getStart(), index.getEnd(), tempStates, temp, 20);
				//补全集合
				while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
					List<Path> newPath = new ArrayList<Path>();
					annealingServiceImpl.getRandomPathList(index.getMatrix(), index.getStart(), index.getEnd(), tempStates, newPath,1);
					temp.addAll(newPath);
				}
				
				tempSet.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, temp, 2));
			}
			operateList.add(tempSet);
		}
		
		
		//补全所有集合
		for(Set set : operateList) {
			List<Path> temp = set.getPathSet();
			while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
				List<Path> newPath = new ArrayList<Path>();
				List<Integer> states = new ArrayList<Integer>();
				annealingServiceImpl.getRandomPathList(index.getMatrix(), index.getStart(), index.getEnd(),  states, newPath,1);
				temp.addAll(newPath);
			}
			set = new Set(temp);
		}
		
		//每迭代一次进行一次贪心
		for(Set set:operateList) {
			set.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, set.getPathSet(), 2));
		}
		//除重
		operateList=setServiceImpl.duplicationRemoving(operateList);
		
		//补充
		while(operateList.size() < index.getPopulationSize() ) {
			Set tempSet = new Set();
			while(tempSet.getPathSet().isEmpty()) {
				List<Integer> tempStates = new ArrayList<Integer>();
				List<Path> temp = new ArrayList<Path>();
				//生成随机20条path
				annealingServiceImpl.getRandomPathList(index.getMatrix(), index.getStart(), index.getEnd(), tempStates, temp, 20);
				//补全集合
				while(!setServiceImpl.isCoveredByReqs(reqs, temp)) {
					List<Path> newPath = new ArrayList<Path>();
					annealingServiceImpl.getRandomPathList(index.getMatrix(), index.getStart(), index.getEnd() , tempStates, newPath,1);
					temp.addAll(newPath);
				}
				tempSet.setPathSet(greedyServiceImpl.bigStepGreedy(reqs, temp, 2));
			}
			operateList.add(tempSet);
		}
		
		
		return operateList;
	}
	//锦标赛选择法 每次抽取N个个体 最好的个体进入选择集合 其余放回 直到选择集合到达size
	private List<Set> championship(int index , int size , List<Set> subpopulation){
		List<Set> selectedList = new ArrayList<Set>();
		List<Set> candidate = new ArrayList<Set>();
		candidate.addAll(subpopulation);
		
		while(selectedList.size() < size) {
			//生成抽取集合的下标列表
			List<Integer> numList = new ArrayList<Integer>();
			numList.addAll(randomUtil.getRandomN(index, candidate.size()));
			//最好个体的下标
			int bestNum = numList.get(0);
			for(int i = 0 ; i < numList.size() ; i++) {
				if(candidate.get(numList.get(i)).getSumTime() < candidate.get(bestNum).getSumTime()) {
					bestNum = numList.get(i);
				}
			}
			
			selectedList.add(candidate.get(bestNum));
			//最好个体无放回
			candidate.remove(bestNum);
			
			//如果候选集的剩余数量不足 退出循环 
			/*
			 * 造成该情况的原因是子种群的个体数量不足
			 * 在后续需要进行补充
			 * */
			if(candidate.size() < index) {
				break;
			}
		}
		
		return selectedList;
	}
	
	private boolean ifCrossover(Set set1 , Set set2 , double maxFit,double avgFit ,double pc1 ,double pc2) {
		boolean flag= false;
		//发生概率
		double p ;
		//由于总时间越小适度值越大  适度值为总时间取反
		double fit = (-set1.getSumTime()) > (-set2.getSumTime())?(-set1.getSumTime()):(-set2.getSumTime());
		if(fit > avgFit) {
			p = pc1 - (pc1 - pc2) *(fit - avgFit)/(maxFit-avgFit);
		}
		else 
			p = pc1;
		
		Random r = new Random();
		//随机生成[0,1.0)
		double d = r.nextDouble();
		if( p > d ) {
			flag = true;
		}
		return flag;
	}
	private boolean ifMutation(Set set ,double maxFit , double avgFit ,double pm1 , double pm2) {
		boolean flag= false;
		double p ; 
		//取反
		double fit = set.getSumTime()*(-1);
		
		if( fit > avgFit) {
			p = pm1 - (pm1 - pm2) *(fit - avgFit)/(maxFit-avgFit);
		}
		else 
			p = pm1;
		
		Random r = new Random();
		//随机生成[0,1.0)
		double d = r.nextDouble();
		if( p > d ) {
			flag = true;
		}
		
		return flag;
	}
	private List<Set> crossover(Set set1 , Set set2){
		List<Set> newSets = new ArrayList<Set>();
		//临时列表
		List<Path> tempPathSet1 = new ArrayList<Path>();
		List<Path> tempPathSet2 = new ArrayList<Path>();
		
		tempPathSet1.addAll(set1.getPathSet());
		tempPathSet2.addAll(set2.getPathSet());
		
		int max1 = 3/4*tempPathSet1.size();
		int max2 = 3/4*tempPathSet2.size();
		List<Integer> exchangeNum1 = randomUtil.getRandomN(max1, tempPathSet1.size());
		List<Integer> exchangeNum2 = randomUtil.getRandomN(max2, tempPathSet2.size());
		
		List<Path> newPathSet1 = new ArrayList<Path>();
		List<Path> newPathSet2 = new ArrayList<Path>();
		
		newPathSet1.addAll(listUtil.getNewListWithoutEle(tempPathSet1, exchangeNum1));
		newPathSet2.addAll(listUtil.getNewListWithoutEle(tempPathSet2, exchangeNum2));
		newPathSet1.addAll(listUtil.getNewListInEle(tempPathSet2, exchangeNum2));
		newPathSet2.addAll(listUtil.getNewListInEle(tempPathSet1, exchangeNum1));
		
		Set newSet1 = new Set(newPathSet1);
		Set newSet2 = new Set(newPathSet2);
		newSets.add(newSet1);
		newSets.add(newSet2);
		return newSets;
	}
	
	private Set mutation(Set set , SubpopulationEntity index)throws Exception {
		List<Path> tempPathSet = new ArrayList<Path>();
		tempPathSet.addAll(set.getPathSet());
		//可行性判断
		boolean flag= false;
		
		//变异点选取
		int num = randomUtil.getRandomN(1, tempPathSet.size()).get(0);
		Path path = tempPathSet .get(num);
		//变异状态选取
		List<Integer> states = new ArrayList<Integer>();
		states.addAll(path.getStatesList());
		Random r = new Random();
		//变异位置不能是头和尾
		int stateNum = r.nextInt(states.size()-2)+1;
		int state = states.get(stateNum) ;
		
		List<Transition> newTL = new ArrayList<Transition>();
		int n = 20;
		while(!flag&& n-->0) {
			newTL = new ArrayList<Transition>();
			List<Path> tempPath = new ArrayList<Path>();
			List<Integer> tempStates = new ArrayList<Integer>();
			annealingServiceImpl.getRandomPathList(index.getMatrix(),index.getStart(), index.getEnd(), tempStates, tempPath,1 );
			Path newPath = tempPath.get(0);
			List<Integer> newStates = newPath.getStatesList();
			if(tempStates.contains(state)) {
				List<Integer> numList = listUtil.searchElement(newStates, state);
				int pointNum = randomUtil.getRandomElement(numList);
				newTL.addAll(listUtil.getSubList(path.gettL(), 0, stateNum));
				newTL.addAll(listUtil.getSubList(newPath.gettL(),pointNum));
				flag = judgement.pathJudge(newTL);
			}
		}
		Set newSet = new Set();
		if(flag==false) {
			return newSet;
		}
		Path newPath = new Path(newTL);
		tempPathSet.remove(num);
		tempPathSet.add(newPath);
		newSet = new Set(tempPathSet);
		
		return newSet;
	}
	
	//种群间迁移
	private void cyclicTransfer(List<List<Set>> population , int transferCount) {
		//依次向后迁移 最后一个集合向第一个集合迁移
		for(int i = 0 ; i < population.size() - 1 ; i++ ) {
			List<Set> tempList = new ArrayList<Set>();
			System.out.println("population.get(i):"+population.get(i).size());
			tempList.addAll(listUtil.getSubList(population.get(i), 0, transferCount));
			//移除下一集合的后transferCount个元素
			listUtil.removeLastN(population.get(i+1), transferCount);
			//将新元素加入下一集合
			population.get(i+1).addAll(tempList);
		}
		
		List<Set> tempList = new ArrayList<Set>();
		tempList.addAll(listUtil.getSubList(population.get(population.size()-1), 0, transferCount));
		population.get(0).addAll(tempList);
	}
	
}
