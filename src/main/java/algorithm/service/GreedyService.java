package algorithm.service;

import java.util.List;

import algorithm.entity.Path;
import algorithm.entity.ReqEnum;

public interface GreedyService {
	public List<Path> bigStepGreedy(List<ReqEnum> reqs , List<Path> pathSet , int n);
}
