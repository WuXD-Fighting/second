package algorithm.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import algorithm.entity.Transition;
import algorithm.service.Impl.AnnealingServiceImpl;
import algorithm.service.Impl.GeneticAlgorithmServiceImpl;
import algorithm.service.Impl.GreedyServiceImpl;
import algorithm.service.Impl.MPGAServiceImpl;
import algorithm.service.Impl.MPGAServiceImpl2;
import algorithm.service.Impl.MPGAServiceImpl3;
import algorithm.service.Impl.SetServiceImpl;
import algorithm.entity.GeneticIndex;
import algorithm.entity.GeneticIndex2;
import algorithm.entity.MPGAEntity;
import algorithm.entity.Path;
import algorithm.entity.ReqEnum;
import algorithm.entity.Set;
import algorithm.utils.GetTransitionByXml;
import algorithm.utils.Judgement;
import algorithm.utils.RequirementUtil;

public class MPGAAction {
	AnnealingServiceImpl annealingServiceImpl = new AnnealingServiceImpl();
	SetServiceImpl setServiceImpl = new SetServiceImpl();
	GeneticAlgorithmServiceImpl geneticAlgorithmServiceImpl = new GeneticAlgorithmServiceImpl();
	RequirementUtil requirementUtil = new RequirementUtil();
	GreedyServiceImpl greedyServiceImpl = new GreedyServiceImpl();
	Transition[][] matrix ;//迁移矩阵
	Double[][] distance ;//距离矩阵
	Judgement judgement = new Judgement();//判断可行性工具类
	MPGAServiceImpl3 mpgaServiceImpl = new MPGAServiceImpl3();
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
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,[33,64],34,35,36,37,[38,40,42,44],48,[50,52],53,54,[55,56,63],[58,61],60,63,[65,66]");
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,[38,40,42,44],48,[50,52],53,54");  
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44],48,[50,52],54");  //30
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44],48,[50,52]");//29
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44],48");//28
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[7,8,11,51],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44]");//27
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[15,16,46],17,19,20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44]");//26
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[15,16,46],19,20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44]");//25
		//reqs = requirementUtil.strForReqs("1,2,3,4,[13,10,12,14],[5,6,9,18,49],[15,16,46],20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44]");//24
		//reqs = requirementUtil.strForReqs("1,2,3,4,[5,6,9,18,49],[15,16,46],20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37,[38,40,42,44]");//23
		//reqs = requirementUtil.strForReqs("1,2,3,4,[5,6,9,18,49],[15,16],20,21,22,23,24,25,26,27,28,29,30,32,33,35,36,37");//22

		
		matrix = new Transition[m][m];
		distance = new Double[m][m]; 
		annealingServiceImpl.initialMatrix(tL, matrix, distance);
		
		MPGAEntity mpgaEntity = new MPGAEntity();
		mpgaEntity.setMatrix(matrix);
		mpgaEntity.setReqs(reqs);
		mpgaEntity.setStart(0);
		mpgaEntity.setEnd(21);
		//贪心参数
		mpgaEntity.setBigStepGreedyIndex(2);
		//隔代参数
		mpgaEntity.setConfirm(20);
		mpgaEntity.setInterval(3);
		//迁移数量
		mpgaEntity.setTransfer(8); 
		//种群参数
		mpgaEntity.setPopulationCount(1);
		mpgaEntity.setPopulationSize(100);
		//交叉变异参数
		mpgaEntity.setPc1(0.9);
		mpgaEntity.setPc2(0.6);
		mpgaEntity.setPm1(0.1);
		mpgaEntity.setPm2(0.001);
		//锦标赛选择参数
		mpgaEntity.setChampionshipIndex(3);
		mpgaEntity.setChampionshipSize(25);
		
		Set result = new Set();
		result = mpgaServiceImpl.MPGA(mpgaEntity);
		for(int i = 0 ;i <result.getSize() ; i++) {
			System.out.println("第"+i+"条路径："+result.get(i).getSet());
		}
		System.out.println("测试总用时:"+result.getSumTime());
	}
	

	
	public static void main(String[] args) throws Exception{
		MPGAAction g = new MPGAAction();
		g.genetic();
		
	}
	

}
