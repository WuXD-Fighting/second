package algorithm.service.Impl;

import java.util.List;

import algorithm.entity.GeneticIndex;
import algorithm.entity.Path;
import algorithm.entity.Set;
import algorithm.entity.Transition;

public class DD extends TT{

	public void initialMatrix(List<Transition> tL, Transition[][] matrix, Double[][] distance) {
		// TODO 自动生成的方法存根
		
	}

	public boolean equalList(List list1, List list2) {
		// TODO 自动生成的方法存根
		return false;
	}

	public void getRandomPathList(Transition[][] matrix, int start, int end, List<Integer> states, List<Path> pathSet,
			int n) {
		// TODO 自动生成的方法存根
		
	}

	public List<Transition> pushList(Path li) {
		// TODO 自动生成的方法存根
		return null;
	}

	public List<Path> crossover(Path path1, Path path2, int stateNum) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	public void mutation() {
		// TODO 自动生成的方法存根
		
	}

	public void exchang(List<Path> pathSet1, List<Path> pathSet2, int n) {
		// TODO 自动生成的方法存根
		
	}

	public void exchang(List<Path> pathSet1, int n1, List<Path> pathSet2, int n2) {
		// TODO 自动生成的方法存根
		
	}

	public List<Set> GeneticAlgorithm(GeneticIndex index) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

}
