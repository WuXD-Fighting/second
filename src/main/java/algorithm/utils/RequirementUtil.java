package algorithm.utils;

import java.util.ArrayList;
import java.util.List;

import algorithm.entity.ReqEnum;

public class RequirementUtil {
	public List<ReqEnum> strForReqs(String str){ 
		List<ReqEnum> reqs = new ArrayList<ReqEnum>();
		String[] strList=str.split(",");
		//用于辅助划分需求
		List<Integer> tempList = new ArrayList<Integer>();
		int i = 0;
		while (i < strList.length) {
			ReqEnum req = new ReqEnum();
			if(strList[i].contains("[")&&strList[i].contains("]")) {
				//该情况应很少发生 即需求包含路径集  路径集却只有一条路径
				String s = strList[i].replace("[", "");
				s = s.replace("]","");
				req.addTL(Integer.parseInt(s));
				reqs.add(req);
			}else if(strList[i].contains("[")) {
				String s = strList[i].replace("[", "");
				tempList.add(Integer.parseInt(s));
			}else if(strList[i].contains("]")){
				String s = strList[i].replace("]","");
				tempList.add(Integer.parseInt(s));
				req.setTL(tempList);
				//遇“[”重置tempList
				tempList = new ArrayList<Integer>();
				reqs.add(req);
			}else {
				//如果此时辅助集合不为空，说明该路径位于某需求集内
				if(tempList.isEmpty()) {
					req.addTL(Integer.parseInt(strList[i]));
					reqs.add(req);
				}else {
					tempList.add(Integer.parseInt(strList[i]));
				}
			}
			i++;
		}
		return reqs;
	}
	
	public static void main(String[] args) {
		RequirementUtil r = new RequirementUtil();
		List<ReqEnum> reqs = new ArrayList<ReqEnum>();
		reqs=r.strForReqs("788,[123,456],444,[4,5,8,9],[5],565656");
		for(ReqEnum req : reqs) {
			System.out.println(req.getTL());
		}
	}
}
	