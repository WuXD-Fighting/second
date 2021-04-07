package algorithm.service;

import java.util.List;

import algorithm.entity.Path;
import algorithm.entity.ReqEnum;
import algorithm.entity.Set;

public interface SetService {
	//判断目标集合是否被覆盖
	public boolean isCovered(List<Integer> set , List<Path> pathSet);
	//选取用时最短的、满足迁移全覆盖的路径集合 numList为需要覆盖的集合
	/*算法思想
	 初始S为空集
	 为路径集中所有路径添加一个权值：W。 W=路径总时间/路径中包含的未覆盖迁移（无重）
	 选取权值最小（或者一定概率接受权值较）的路径加入S
	 更新权值
	 迭代 直到全覆盖
	 */
	public List<Path> getPathSets(List<Integer> numList,List<Path> pathSet);
	//选取集合列表中耗时最小的前n的集合
	public List<Set> selectShortN(List<Set> setList , int n);
	//补全覆盖集,如果不能实现全覆盖从总集合中抽取权值最小的加入的到集合中
	public void completeSet(List<Integer> numList ,Set set , List<Set> setList);
	//从Set中随机抽取一条包含状态n的路径
	public Path selectPathWithState(Set set, int num);
	
	//需求集合判断的方法
	//判断路径集是否覆盖需求集合，一个需求可能对应多个迁移 仅需覆盖一条即可
	public boolean isCoveredByReqs(List<ReqEnum> reqs, List<Path> pathSet);
	//判断需求集是否相互覆盖
	public boolean isCoveredByReqSet(List<ReqEnum> reqs,List<ReqEnum> nowReqs);
	//从集合中挑选覆盖集合（存在需求测试由多条路径体现，任一覆盖一条即可）
	public List<Path> getPathSetsByReqs(List<ReqEnum> reqs,List<Path> pathSet);
	//根据需求集补全集合 reqs为需要包含的需求集合，set为当前需要进行补全的集合，setList为全部集合
	public void completeSetByReqs(List<ReqEnum> reqs , Set set , List<Set> setList);
	
}
