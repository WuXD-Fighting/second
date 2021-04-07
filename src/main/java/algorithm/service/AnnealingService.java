package algorithm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.entity.Transition;
import algorithm.entity.Path;
import algorithm.utils.GetTransitionByXml;
import algorithm.utils.Judgement;

public interface AnnealingService {
	
	//将List存入迁移矩阵和距离矩阵中
	public  void initialMatrix(List<Transition> tL, Transition[][] matrix , Double[][] distance);
	//判断List是否相等
	public boolean equalList(List list1, List list2) ;
	//得到随机路径组成的集合，生成n条路径
	public  void getRandomPathList(Transition[][] matrix,int start ,int end,List<Integer> states,List<Path> pathSet,int n) ;
	//构建随机可行路径
	public  List<Transition> pushList(Path li);
}
