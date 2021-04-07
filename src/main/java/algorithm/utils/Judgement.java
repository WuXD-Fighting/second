package algorithm.utils;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import algorithm.entity.Path;
import algorithm.entity.Transition;

public class Judgement {
	private static String commonPremise = "var a,b,c,d;a=b=c=d=e=0;";
	
	//新增迁移条件判断，通过JS实现。每次只判断新加入的迁移条件是否可行
	public boolean judge(List<Transition> tL) throws Exception{
		ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("JavaScript");
		//路径是否可行的flag
	    boolean result=false;
	    //公共条件或者初始条件，初始化EFSM中出现的变量
		String str=commonPremise;
		for(int i = 0 ; i < tL.size() ; i++) {
			//前n-1条只单纯的进行action
			if(i!=tL.size()-1) {
				if(tL.get(i).getAction()!="") {
					str +=tL.get(i).getAction()+";";
				}
			}
			else {
				//只对新加入的迁移条件进行判断
				if(tL.get(i).getCondition()!="") {
					str=str+"if("+tL.get(i).getCondition()+") true;else false;";
					result=(Boolean)engine.eval(str);
				}
				else result=true;
			}
		}
		return result;
	}
	//路径可行性判断,采用逐步判断的方式
	public boolean pathJudge(List<Transition> tL)throws Exception{
		ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("JavaScript");
		boolean result=true;
		String str=commonPremise;
		for(int i = 0 ; i < tL.size() ; i++) {
			if(tL.get(i).getCondition()!="") {
				str=str+tL.get(i).getCondition()+";";
				result=(Boolean)engine.eval(str);
			}
			if(result==false)
				return result;
			if(tL.get(i).getAction()!="") {
				str +=tL.get(i).getAction()+";";
			}
		}
		return result;
	}
	public static void main(String[] args)  throws Exception{
		ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("JavaScript");
		boolean result=false;
		String str=commonPremise+"a=b=c=0;c<0;c>0;c=c+1;c>0?true:false";
		result=(Boolean)engine.eval(str);
		System.out.println(result);
	}
}
