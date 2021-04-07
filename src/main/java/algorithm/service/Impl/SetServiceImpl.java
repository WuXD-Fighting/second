package algorithm.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import algorithm.entity.Path;
import algorithm.entity.ReqEnum;
import algorithm.entity.Set;
import algorithm.entity.Transition;
import algorithm.service.SetService;
import algorithm.utils.ListUtil;
import algorithm.utils.RandomUtil;

public class SetServiceImpl implements SetService{
	ListUtil listUtil = new ListUtil();
	RandomUtil randomUtil = new RandomUtil();
	GreedyServiceImpl greedyServiceImpl = new GreedyServiceImpl();
	public boolean isCovered(List<Integer> set, List<Path> pathSet) {
		//临时集合nowSet，用于储存pathSet包含的所有不重复迁移编号
		List<Integer> nowSet = new ArrayList<Integer>();
		for(Path path : pathSet) {
			List<Integer> temp = path.getSet();
			for(Integer num : temp) {
				if(!nowSet.contains(num)) {
					nowSet.add(num);
				}
			}
		}
		//判断是否全部被包含
		for(Integer tN : set) {
			if(!nowSet.contains(tN))
				return false;
		}
		return true;
	}
	//判断路径集是否覆盖需求集合，一个需求可能对应多个迁移 仅需覆盖一条即可
	public boolean isCoveredByReqs(List<ReqEnum> reqs, List<Path> pathSet) {
		List<ReqEnum> nowSet = new ArrayList<ReqEnum>();
		for(Path path : pathSet) {
			List<Integer> temp = path.getSet();
			for(Integer num : temp) {
				for(ReqEnum req: reqs ) {
					if(!nowSet.contains(req)) {
						if(req.getTL().contains(num)) {
							nowSet.add(req);
						}
					}
				}
			}
		}
		
		for(ReqEnum req: reqs) {
			if(!nowSet.contains(req)) {
				return false;
			}
		}
		return true;
	}
	//判断需求集是否相互覆盖
	public boolean isCoveredByReqSet(List<ReqEnum> reqs,List<ReqEnum> nowReqs) {
		for(ReqEnum req:reqs) {
			if(!nowReqs.contains(req))
				return false;
		}
		return true;
	}
	//从集合中挑选全覆盖集
	public List<Path> getPathSets(List<Integer> numList,List<Path> pathSet) {
		List<Path> S = new ArrayList<Path>();
		List<Integer> newList = new ArrayList<Integer>();
		while(!listCover(numList,newList)&&!pathSet.isEmpty()) {
			//每挑选一条则重置权值
			weightCalculation(newList,pathSet);
			int num = getMinNum(pathSet);
			if(num!=-1) {
				S.add(pathSet.get(num));
				newList.addAll(getAddition(newList,pathSet.get(num)));
				pathSet.remove(num);
			}else {
				S = new ArrayList<Path>();
				return S; //如果选择出的路径包含集合中所有的迁移，而此时又不满足覆盖原则，则可能进入死循环 该步骤将空集合返回防止死循环
			}
		}
		return S;
	}
	//从集合中挑选覆盖集合（存在需求测试由多条路径体现，任一覆盖一条即可）
	public List<Path> getPathSetsByReqs(List<ReqEnum> reqs,List<Path> pathSet){   
		//newPathSet 为最终的返回的集合
		List<Path> newPathSet = new ArrayList<Path>();
		//nowReqs对应到newPathSet的需求集
		List<ReqEnum> nowReqs = new ArrayList<ReqEnum>();
		
		List<Path> tempPathSet = new ArrayList<Path>();
		tempPathSet.addAll(pathSet);
		
		while(!isCoveredByReqs(reqs,newPathSet)&&!tempPathSet.isEmpty()) {
			weightCalculationByReqs(nowReqs ,reqs, tempPathSet);
			int num = getMinNum(tempPathSet);
			if(num != -1) {
				newPathSet.add(tempPathSet.get(num));
				List<ReqEnum> pathReq = pathForReqs(reqs,tempPathSet.get(num));
				for(ReqEnum req: pathReq) {
					if(!nowReqs.contains(req))
						nowReqs.add(req);
				}
				tempPathSet.remove(num);
			}
			else {
				newPathSet =new ArrayList<Path>();
				return newPathSet;
			}
		}
		return newPathSet;
	}


	public List<Set> selectShortN(List<Set> setList,int n) {
		//根据总时间排序
		Collections.sort(setList, new Comparator<Set>() {
			public int compare(Set set1, Set set2) {
				return new Double(set1.getSumTime()).compareTo(new Double(set2.getSumTime())); //升序
		    }
		});	
		List<Set> temp = new ArrayList<Set>();
		int max = n > setList.size()?setList.size():n;
		temp.addAll(listUtil.getSubList(setList, 0, max));
		return temp;
	}
	//集合补全
	public void completeSet(List<Integer> numList ,Set set , List<Set> setList) {
		//当前Set的覆盖集合
		List<Integer> newList = new ArrayList<Integer>();
		for(int i = 0 ; i < set.getSize() ; i++) {
			for(int j = 0 ; j < set.get(i).getSize() ; j++) {
				if(!newList.contains(set.get(i).gettL().get(j).getNum())){
					newList.add(set.get(i).gettL().get(j).getNum());
				}
			}
		}
		if(!listCover(numList,newList)) {
			//总集合(只是一个临时集合)
			List<Path> S = new ArrayList<Path>();
			for(Set setEle:setList) {
				if(!setEle.equals(set))
					S.addAll(setEle.getPathSet());
			}
			while(!listCover(numList,newList)) {
				weightCalculation(newList,S);
				int num = getMinNum(S);
				set.addPath(S.get(num));
				S.remove(num);
			}
		}
	}
	//根据需求集补全集合 reqs为需要包含的需求集合，set为当前需要进行补全的集合，setList为全部集合
	public void completeSetByReqs(List<ReqEnum> reqs , Set set , List<Set> setList) {
		//记录当前已包含的Req 
		List<ReqEnum> nowReqs = new ArrayList<ReqEnum>();
		nowReqs = setForReqs(reqs,set);
		
		if(!isCoveredByReqSet(reqs,nowReqs)) {
			List<Path> S = new ArrayList<Path>();
			for(Set setEle:setList) {
				if(!setEle.equals(set))
					S.addAll(setEle.getPathSet());
			}
			while(!isCoveredByReqSet(reqs,nowReqs)) {
				weightCalculationByReqs(nowReqs, reqs, S);
				int num = getMinNum(S);
				set.addPath(S.get(num));
				S.remove(num);
				nowReqs = setForReqs(reqs,set);
			}
		}
		
	}
	//随机选择包含状态num的路径
	public Path selectPathWithState(Set set, int num) {
		List<Integer> index  = new ArrayList<Integer>();
		for(int i = 0 ; i < set.getPathSet().size() ; i++) {
			if(set.getPathSet().get(i).getStatesList().contains(num))
				index.add(i);
		}
		Path path = new Path();
		try {
		int result = randomUtil.getRandomElement(index);
		path = set.get(result);
		}catch (Exception e) {
			System.out.println("state:"+num);
			System.out.println("list:"+index);
		}
		return path;
	}
	//覆盖判断
	private  boolean listCover(List<Integer> numList,List<Integer> newList) {
		for(Integer tN : numList) {
			if(!newList.contains(tN))
				return false;
		}
		return true;
	}
	//权值计算并进行设置
	private  void weightCalculation(List<Integer> newList, List<Path> pathSet) {
		for(Path path : pathSet) {
			List<Integer> addList = getAddition(newList,path);
			double weight = Double.POSITIVE_INFINITY;
			if(!addList.isEmpty()) {
				weight = path.getSumTim()/addList.size();
			}
			path.setWeight(weight);
		}
	}
	//通过需求集 计算权值并设置
	private  void weightCalculationByReqs(List<ReqEnum> nowReqs ,List<ReqEnum> reqs, List<Path> pathSet) {
		for(Path path : pathSet) {
			List<ReqEnum> additionReqs = getAdditionByReqs(nowReqs,reqs,path);
			double weight = Double.POSITIVE_INFINITY;
			if(!additionReqs.isEmpty()) {
				weight = path.getSumTim()/additionReqs.size();
			}
			path.setWeight(weight);
		}
	}
	//拿权值最小的迁移在List中的编号
	private static int getMinNum(List<Path> pathSet) {
		double min = Double.POSITIVE_INFINITY;
		int num = -1;
		for(int i = 0 ; i < pathSet.size() ; i++) {
			if(pathSet.get(i).getWeight()<min) {
				min = pathSet.get(i).getWeight();
				num = i;
			}
		}
		return num;
	}
	//拿到新增迁移编号
	private  List<Integer> getAddition(List<Integer> newList ,Path path){
		/*
		 * 返回为addList
		 * 为新增的迁移集合
		 * */
		List<Integer> addList = new ArrayList<Integer>();
		List<Transition> tL = new ArrayList<Transition>();
		tL.addAll(path.gettL());
		for(int i = 0 ; i <tL.size() ; i++) {
			if(!newList.contains(tL.get(i).getNum())&&!addList.contains(tL.get(i).getNum()))
				addList.add(tL.get(i).getNum());
		}
		return addList;
	}
	// nowReqs为现阶段pathSet已包含的需求
	private  List<ReqEnum> getAdditionByReqs(List<ReqEnum> nowReqs ,List<ReqEnum> reqs,Path path){
		List<ReqEnum> additionReqs = new ArrayList<ReqEnum>();
		
		List<ReqEnum> pathReqs = new ArrayList<ReqEnum>();
		pathReqs = pathForReqs(reqs,path);
		for(ReqEnum pathReq : pathReqs ) {
			if(!nowReqs.contains(pathReq)&&!additionReqs.contains(pathReq)) {
				additionReqs.add(pathReq);
			}
		}
		return additionReqs;
	}
	//将Path所包含的迁移转为req的集合
	private List<ReqEnum> pathForReqs(List<ReqEnum> reqs,Path path){
		List<ReqEnum> pathReqs = new ArrayList<ReqEnum>();
		List<Integer> temp = path.getSet();
		for(Integer num : temp) {
			for(ReqEnum req: reqs ) {
				if(!pathReqs.contains(req)) {
					if(req.getTL().contains(num)) {
						pathReqs.add(req);
					}
				}
			}
		}
		return pathReqs;
	}
	//得到Set包含的迁移集合
	private List<ReqEnum> setForReqs(List<ReqEnum> reqs,Set set){
		List<ReqEnum> setReq = new ArrayList<ReqEnum>();
		//当前Set的覆盖集合
		List<Integer> newList = new ArrayList<Integer>();
		for(int i = 0 ; i < set.getSize() ; i++) {
			for(int j = 0 ; j < set.get(i).getSize() ; j++) {
				if(!newList.contains(set.get(i).gettL().get(j).getNum())){
					newList.add(set.get(i).gettL().get(j).getNum());
				}
			}
		}
		for(Integer num : newList) {
			for(ReqEnum req: reqs ) {
				if(!setReq.contains(req)) {
					if(req.getTL().contains(num)) {
						setReq.add(req);
					}
				}
			}
		}
		return setReq;
	}
	//集合相等判断
	public boolean IsEqual(Set set1 , Set set2) {
		boolean flag = false;
		if(set1.getSize()==set2.getSize()&&set1.getSumTime()==set2.getSumTime()) {
			for(int i = 0 ; i < set1.getSize(); i++){
				if(!set1.get(i).isEqual(set2.get(i)))
					return false;
			}
			flag = true;
		}
		return flag;
	}
	//集合除重方法
	public List<Set> duplicationRemoving(List<Set> setList){
		List<Set> temp = new ArrayList<Set>();
		for(int i = 0 ; i < setList.size() ; i++) {
			boolean flag = true;
			for(int j = 0 ; j < temp.size() ; j++) {
				if(IsEqual(temp.get(j), setList.get(i))) {
					flag = false;
					break;
				}
			}
			if(flag) {
				temp.add(setList.get(i));
			}
		}
		return temp;
	}
	
	public static void main(String[] args) {
		Path p1= new Path();
		Integer[] i1={2,3,4};
		p1.setByNums(java.util.Arrays.asList(i1));
		
		Path p2= new Path();
		Integer[] i2={5,4,7};
		p2.setByNums(java.util.Arrays.asList(i2));
		
		Path p3= new Path();
		Integer[] i3={9,8,10};
		p3.setByNums(java.util.Arrays.asList(i3));
		
		Path p4= new Path();
		Integer[] i4={11,15,19,17};
		p4.setByNums(java.util.Arrays.asList(i4));
		
		List<Path> pathSet = new ArrayList<Path>();
		pathSet.add(p1);
		pathSet.add(p2);
		pathSet.add(p3);
		pathSet.add(p4);
		
		for(Path p :pathSet) {
			System.out.println(p.getSet());
		}
		
		Integer[] ri1={2};
		List<Integer> r1 = new ArrayList<Integer>(Arrays.asList(ri1));
		ReqEnum req1 = new ReqEnum(r1);
		
		Integer[] ri2={4,3};
		List<Integer> r2 = new ArrayList<Integer>(Arrays.asList(ri2));
		ReqEnum req2 = new ReqEnum(r2);
		
		Integer[] ri3={6,18};
		List<Integer> r3 = new ArrayList<Integer>(Arrays.asList(ri3));
		ReqEnum req3 = new ReqEnum(r3);
		
		Integer[] ri4={11,18};
		List<Integer> r4 = new ArrayList<Integer>(Arrays.asList(ri4));
		ReqEnum req4 = new ReqEnum(r4);
		
		List<ReqEnum> reqs = new ArrayList<ReqEnum>();
		reqs.add(req4);
		reqs.add(req3);
		reqs.add(req2);
		reqs.add(req1);
		
		SetServiceImpl test = new SetServiceImpl();
		System.out.println(test.isCoveredByReqs(reqs, pathSet));
	}
}
