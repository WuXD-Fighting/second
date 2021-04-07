package algorithm.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import algorithm.entity.Transition;

public class GetTransitionByXml {
	private static String dirPath=GetTransitionByXml.class.getResource("/").getPath();
	
	private static Document getDocument() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(dirPath+"xml/transition.xml"));
	return document;
	}
	public static List<Transition> getTransitionList() throws Exception {
		List<Transition> tlist = new ArrayList<Transition>();
        Document document = getDocument();
        NodeList list = document.getElementsByTagName("transition");
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            Transition transition = new Transition();
            transition.setNum(Integer.parseInt(element.getElementsByTagName("num").item(0).getTextContent()));
            transition.setSource(Integer.parseInt(element.getElementsByTagName("source").item(0).getTextContent()));
            transition.setTarget(Integer.parseInt(element.getElementsByTagName("target").item(0).getTextContent()));
            transition.setTime(Double.parseDouble(element.getElementsByTagName("time").item(0).getTextContent()));
        	transition.setAction(element.getElementsByTagName("action").item(0).getTextContent());
        	transition.setCondition(element.getElementsByTagName("condition").item(0).getTextContent());
        	transition.setEvent(element.getElementsByTagName("event").item(0).getTextContent());
            tlist.add(transition);
        }
        return tlist;
    }
	public static List<Integer> getNumSet(List<Transition> t) {
		List<Integer> numList = new ArrayList<Integer>();
		for(int i = 0 ; i < t.size() ; i++) {
			if(!numList.contains(t.get(i).getNum()))
				numList.add(t.get(i).getNum());
		}
		return numList;
	}
	public static void main(String[] args) throws Exception{
	}
}
