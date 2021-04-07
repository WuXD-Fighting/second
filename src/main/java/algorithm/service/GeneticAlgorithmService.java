package algorithm.service;

import java.util.List;

import algorithm.entity.GeneticIndex;
import algorithm.entity.Path;
import algorithm.entity.Set;

public interface GeneticAlgorithmService {
	//交叉。path1和path2在状态stateNum的后续序列进行互换，产生两条新的path
	public List<Path> crossover(Path path1 , Path path2 , int stateNum)throws Exception;
	//变异
	public void mutation();
	//交换n个元素
	public void exchang(List<Path> pathSet1 , List<Path> pathSet2 , int n);
	//集合1用n1个元素交换集合2的n2个元素
	public void exchang(List<Path> pathSet1 ,int n1 , List<Path> pathSet2 , int n2);
	//执行遗传算法
	public List<Set> GeneticAlgorithm(GeneticIndex index) throws Exception;
}
