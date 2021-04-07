package algorithm.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WordTranslation {
	public void testReadByExtractor() throws Exception {  
	      InputStream is = new FileInputStream("C:\\Users\\wxd\\Desktop\\系统设计报副本1.docx");  
	      XWPFDocument doc = new XWPFDocument(is);  
	      XWPFWordExtractor extractor = new XWPFWordExtractor(doc);  
	      String text = extractor.getText();  
	      System.out.println(text);  
	      CoreProperties coreProps = extractor.getCoreProperties();  
	      this.printCoreProperties(coreProps);  
	      extractor.close();
	      this.close(is);  
	      
	   }  
	
	public void testReadByExtractor2() throws Exception {  
	      InputStream is = new FileInputStream("C:\\Users\\wxd\\Desktop\\系统设计报副本1.docx");  
	      XWPFDocument doc = new XWPFDocument(is);  
	      List<XWPFParagraph> paras=doc.getParagraphs(); 
	      for (XWPFParagraph para : paras) {
	    	  System.out.println(para.getParagraphText());
	    	  
	    	  System.out.println(para.getStyle());
	      }
	      doc.close();
	      this.close(is);  
	      
	   }  
	public void testReadByExtractor3() throws Exception {  
		InputStream is = new FileInputStream("C:\\Users\\wxd\\Desktop\\系统设计报副本3.doc");  
		HWPFDocument doc = new HWPFDocument(is); 
		Range r = doc.getRange();
		
		int numStyles =doc.getStyleSheet().numStyles();
		List<TreeEntity> tree = new ArrayList<TreeEntity>();
		for (int i = 0; i < r.numParagraphs(); i++) {
			Paragraph p = r.getParagraph(i);
			int styleIndex = p.getStyleIndex();
	        if (numStyles > styleIndex) {
	            StyleSheet style_sheet = doc.getStyleSheet();
	            StyleDescription style = style_sheet.getStyleDescription(styleIndex);
	            String styleName = style.getName();
	            TreeEntity node = new TreeEntity();
	            if(styleName.contains("标题 1")) {
	            	node.setText(p.text());
	            	node.setLevel(1);
	            }else if(styleName.contains("标题 2")) {
	            	node.setText(p.text());
	            	node.setLevel(2);
	            }else if(styleName.contains("标题 3")) {
	            	node.setText(p.text());
	            	node.setLevel(3);
	            }else if(styleName.contains("标题 4")) {
	            	node.setText(p.text());
	            	node.setLevel(4);
	            }else {
	            	node.setText(p.text());
	            	node.setLevel(5);
	            }
	            tree.add(node);
	        }
		}
		writeToXml("H:\\SCA\\xml2\\behaviors\\standard\\tt.xml",tree);
		this.close(is);  
	   }
	
	private void writeToXml(String path , List<TreeEntity> tree) throws Exception{
		//创建文件
		File file = new File(path);
		file.createNewFile();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder() ;
		Document doc = builder.newDocument();
		Element root = doc.createElement("behaviors");
		Element first = doc.createElement("level1");
		Element second = doc.createElement("level2");
		Element third = doc.createElement("level3");
		Element forth = doc.createElement("level4");
		Element fifth;
		for(TreeEntity node:tree) {
			if(node.getLevel()==1) {
				first = doc.createElement("level1");
				first.setAttribute("name", node.getText());
				root.appendChild(first);
			}else if(node.getLevel()==2){
				second = doc.createElement("level2");
				second.setAttribute("name", node.getText());
				first.appendChild(second);
			}else if(node.getLevel()==3){
				third = doc.createElement("level3");
				third.setAttribute("name", node.getText());
				second.appendChild(third);
			}else if(node.getLevel()==4){
				forth = doc.createElement("level4");
				forth.setAttribute("name", node.getText());
				third.appendChild(forth);
			}else if(node.getLevel()==5){
				if(node.getText().contains("：")) {
					fifth = doc.createElement("comment");
					String name = node.getText().substring(0, node.getText().indexOf("："));
					String value = node.getText().substring(node.getText().indexOf("："));
					fifth.setAttribute("name", name);
					fifth.setTextContent(value);
					forth.appendChild(fifth);
				}
			}
		}
		doc.appendChild(root);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Source source = new DOMSource(doc);
		Result result = new StreamResult(file);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.transform(source, result);

		
	}
	private void printCoreProperties(CoreProperties coreProps) {  
	      System.out.println(coreProps.getCategory());   //分类  
	      System.out.println(coreProps.getCreator()); //创建者  
	      System.out.println(coreProps.getCreated()); //创建时间  
	      System.out.println(coreProps.getTitle());   //标题  
	   }  
	
	private void close(InputStream is) {  
	      if (is != null) {  
	         try {  
	            is.close();  
	         } catch (IOException e) {  
	            e.printStackTrace();  
	         }  
	      }  
	   }  
	public static void main(String[] args) {
		WordTranslation w= new WordTranslation();
		try {
			w.testReadByExtractor3();
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public static List<String> getWordTitles2003(String path) throws IOException{
		File file = new File(path);
		String filename = file.getName();
		filename = filename.substring(0, filename.lastIndexOf("."));
		InputStream is = new FileInputStream(path);
		HWPFDocument doc = new HWPFDocument(is); 
		Range r = doc.getRange();
		
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < r.numParagraphs(); i++) {
	        Paragraph p = r.getParagraph(i);
	        // check if style index is greater than total number of styles
	        int numStyles =doc.getStyleSheet().numStyles();
	        int styleIndex = p.getStyleIndex();
	        if (numStyles > styleIndex) {
	            StyleSheet style_sheet = doc.getStyleSheet();
	            StyleDescription style = style_sheet.getStyleDescription(styleIndex);
	            String styleName = style.getName();
	            if (styleName!=null&&styleName.contains("标题 3")) {
	            	//write style name and associated text
		            System.out.println(styleName + " -> " + p.text());
	            	System.out.println(p.text());
	            	String text = p.text();
	            	list.add(text);
				}
	        }
		}

        return list;
		}
	

}
