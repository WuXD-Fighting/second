package algorithm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {
	//随机抽取
	public <T extends Object>T getRandomElement(List<T> list){
		Random r = new Random();
		int size = list.size();
		int random = r.nextInt(size);
		T result = list.get(random);
		return result;
	}
	//在[start,end)中抽出n个不同的数字
	public List<Integer> getRandomN(int n , int start , int end){
		List<Integer> result = new ArrayList<Integer>();
		Random r = new Random();
		while(result.size()<n) {
			int random = r.nextInt(end-start)+start;
			if(!result.contains(random))
				result.add(random);
		}
		return result;
	}
	//在[0,end)中抽出n个不同的数字
	public List<Integer> getRandomN(int n  , int end){
		List<Integer> result = new ArrayList<Integer>();
		Random r = new Random();
		while(result.size()<n) {
			int random = r.nextInt(end);
			if(!result.contains(random))
				result.add(random);
		}
		return result;
	}
	//执行概率
	public boolean actionChance(int S , int action) {
		boolean flag = false;
		Random r = new Random();
		if(r.nextInt(S)<action)
			flag = true;
		return flag;
	}
	
	 public void Demo2(){
        Random r = new Random();
        double d1 = r.nextDouble();
        System.out.println("d1:"+d1);
    }
	 public static void main(String[] args) {
		 Random r = new Random();
		 System.out.println(r.nextInt(20)+0);
	}
	
}
