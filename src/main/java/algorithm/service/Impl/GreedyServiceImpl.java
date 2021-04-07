package algorithm.service.Impl;

import java.util.ArrayList;
import java.util.List;

import algorithm.entity.Path;
import algorithm.entity.ReqEnum;
import algorithm.entity.Set;
import algorithm.service.GreedyService;

public class GreedyServiceImpl implements GreedyService{
	//reqs为需要包含的req pathSet为当前操作的path集合 n为联合系数
	public List<Path> bigStepGreedy(List<ReqEnum> reqs, List<Path> pathSet ,int n ) {
		//防止改变pathSet
		List<Path> tempSet = new ArrayList<Path>();
		tempSet.addAll(pathSet);
		//当前结果包含的reqs
		List<ReqEnum> nowReqs = new ArrayList<ReqEnum>();
		//临时reqs 新增req数量
		List<ReqEnum> tempReqs = new ArrayList<ReqEnum>();
		//临时路径 用于计算联合权重
		List<Path> tempPaths = new ArrayList<Path>();
		//for循环结束后，将nums中的数字对应的路径加入到目标数组中
		List<Integer> nums = new ArrayList<Integer>();
		//目标数组
		List<Path> result = new ArrayList<Path>();
		//权重
		double bestWeight = Double.POSITIVE_INFINITY;
		if(n == 2) {
			while(!isCoveredByReqs(reqs,result)) {
				nowReqs = pathSetForReqs(reqs,result);
				nums = new ArrayList<Integer>();
				for( int i = 0 ;i < tempSet.size() ; i++) {
					tempPaths = new ArrayList<Path>();
					tempPaths.add(tempSet.get(i));
					tempReqs = getAdditionByReqs(nowReqs , reqs , tempPaths);
					if(!tempReqs.isEmpty())
					{	
						if(tempSet.get(i).getSumTim()/tempReqs.size()<bestWeight) {
							nums = new ArrayList<Integer>();
							nums.add(i);
							bestWeight = tempSet.get(i).getSumTim()/tempReqs.size();
						}
					}
					for(int j = i+1 ; j < tempSet.size() ; j++) {
						tempPaths = new ArrayList<Path>();
						tempPaths.add(tempSet.get(i));
						tempPaths.add(tempSet.get(j));
						tempReqs = getAdditionByReqs(nowReqs , reqs , tempPaths);
						if(!tempReqs.isEmpty())
						{	
							if((tempSet.get(i).getSumTim()+tempSet.get(j).getSumTim())/tempReqs.size()<bestWeight) {
								nums = new ArrayList<Integer>();
								nums.add(i);
								nums.add(j);
								bestWeight = (tempSet.get(i).getSumTim()+tempSet.get(j).getSumTim())/tempReqs.size();
							}
						}
					}
				}
				for(int num:nums) {
					result.add(tempSet.get(num));
				}
				for(int i = nums.size()-1 ; i >= 0 ;i--) {
					tempSet.remove((int) nums.get(i));
				}
				bestWeight = Double.POSITIVE_INFINITY;
			}
		}else if(n == 3) {
			while(!isCoveredByReqs(reqs, result)) {
				nowReqs = pathSetForReqs(reqs,result);
				nums = new ArrayList<Integer>();
				for( int i = 0 ;i < tempSet.size() ; i++) {
					tempPaths = new ArrayList<Path>();
					tempPaths.add(tempSet.get(i));
					tempReqs = getAdditionByReqs(nowReqs , reqs , tempPaths);
					if(!tempReqs.isEmpty())
					{	
						if(tempSet.get(i).getSumTim()/tempReqs.size()<bestWeight) {
							nums = new ArrayList<Integer>();
							nums.add(i);
							bestWeight= tempSet.get(i).getSumTim()/tempReqs.size();
						}
					}
					for(int j = i+1 ; j < tempSet.size() ; j++) {
						tempPaths = new ArrayList<Path>();
						tempPaths.add(tempSet.get(i));
						tempPaths.add(tempSet.get(j));
						tempReqs = getAdditionByReqs(nowReqs , reqs , tempPaths);
						if(!tempReqs.isEmpty())
						{	
							if((tempSet.get(i).getSumTim()+tempSet.get(j).getSumTim())/tempReqs.size()<bestWeight) {
								nums = new ArrayList<Integer>();
								nums.add(i);
								nums.add(j);
								bestWeight= (tempSet.get(i).getSumTim()+tempSet.get(j).getSumTim())/tempReqs.size();
							}
						}
						for(int k = j+1 ; k < tempSet.size() ; k++) {
							tempPaths = new ArrayList<Path>();
							tempPaths.add(tempSet.get(i));
							tempPaths.add(tempSet.get(j));
							tempPaths.add(tempSet.get(k));
							tempReqs = getAdditionByReqs(nowReqs , reqs , tempPaths);
							if(!tempReqs.isEmpty())
							{	
								if((tempSet.get(i).getSumTim()+tempSet.get(j).getSumTim()+tempSet.get(k).getSumTim())/tempReqs.size()<bestWeight) {
									nums = new ArrayList<Integer>();
									nums.add(i);
									nums.add(j);
									nums.add(k);
									bestWeight= (tempSet.get(i).getSumTim()+tempSet.get(j).getSumTim()+tempSet.get(k).getSumTim())/tempReqs.size();
								}
							}
						}
					}
				}
				for(int num:nums) {
					result.add(tempSet.get(num));
				}
				for(int i = nums.size()-1 ; i >= 0 ;i--) {
					tempSet.remove((int) nums.get(i));
				}
				bestWeight = Double.POSITIVE_INFINITY;
			}
		}
		nowReqs = pathSetForReqs(reqs,result);
		return result;
	}
	//判断路径集是否覆盖需求集合，一个需求可能对应多个迁移 仅需覆盖一条即可
		private boolean isCoveredByReqs(List<ReqEnum> reqs, List<Path> pathSet) {
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
	// nowReqs为现阶段pathSet已包含的需求
		private  List<ReqEnum> getAdditionByReqs(List<ReqEnum> nowReqs ,List<ReqEnum> reqs,List<Path> paths){
			List<ReqEnum> additionReqs = new ArrayList<ReqEnum>();
			List<ReqEnum> pathReqs = new ArrayList<ReqEnum>();
			for(Path path:paths)
				pathReqs.addAll(pathForReqs(reqs,path));
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
		//得到pathSet包含的迁移集合
		private List<ReqEnum> pathSetForReqs(List<ReqEnum> reqs,List<Path> pathSet){
			List<ReqEnum> setReq = new ArrayList<ReqEnum>();
			//当前Set的覆盖集合
			List<Integer> newList = new ArrayList<Integer>();
			for(int i = 0 ; i < pathSet.size() ; i++) {
				for(int j = 0 ; j < pathSet.get(i).getSize() ; j++) {
					if(!newList.contains(pathSet.get(i).gettL().get(j).getNum())){
						newList.add(pathSet.get(i).gettL().get(j).getNum());
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
}
