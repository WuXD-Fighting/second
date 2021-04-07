package algorithm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.entity.Set;
import algorithm.service.Impl.SetServiceImpl;

public class ListUtil {
	//采用泛型，搜索list中与obj相同的元素，返回下标列表
	public <T extends Object> List<Integer> searchElement(List<T> list,T obj) {
		List<Integer> result = new ArrayList<Integer>();
		for(int i = 0 ; i < list.size() ; i++) {
			if(list.get(i).equals(obj))
				result.add(i);
		}
		return result;
	}
	//取list子集，start到end-1
	public <T extends Object>List<T> getSubList(List<T> list , int start , int end ){
		List<T> newList = new ArrayList<T>();
		for(int i = start ; i < end ; i ++ ) {
			newList.add(list.get(i));
		}
		return newList;
	}
	//取子集，start到list尾
	public <T extends Object>List<T> getSubList(List<T> list , int start ){
		List<T> newList = new ArrayList<T>();
		int end = list.size();
		for(int i = start ; i < end ; i ++ ) {
			newList.add(list.get(i));
		}
		return newList;
	}
	//获取list的子列表，不包含下标为deleteEle中数字的元素
	public <T extends Object>List<T> getNewListWithoutEle(List<T> list , List<Integer> deleteEle){
		List<T> newList = new ArrayList<T>();
		for(int i = 0 ; i < list.size() ; i++ ) {
			if(!deleteEle.contains(i)) {
				newList.add(list.get(i));
			}
		}
		return newList;
	}
	//获取list的子集，下标包含在selectEle中的元素
	public <T extends Object>List<T> getNewListInEle(List<T> list , List<Integer> selectEle){
		List<T> newList = new ArrayList<T>();
		for(int i = 0 ; i < list.size() ; i++ ) {
			if(selectEle.contains(i))
				newList.add(list.get(i));
		}
		return newList;
	}
	//移除后n个元素
	public <T extends Object>void removeLastN(List<T> list , int n){
		for(int i = 0  ; i < n ; i++) {
			list.remove(list.size()-1);
		}
	}
	//随机抽取n个元素
	public <T extends Object>List<T> getRandomElementN(List<T> list , int n){
		List<T> newList = new ArrayList<T>();
		List<Integer> num = new ArrayList<Integer>();
		
		Random r = new Random();
		
		while(num.size()<n) {
			int random = r.nextInt(list.size());
			if(!num.contains(random))
				num.add(random);
		}
		
		for(int i : num) {
			newList.add(list.get(i));
		}
		
		return newList;
	}
}
