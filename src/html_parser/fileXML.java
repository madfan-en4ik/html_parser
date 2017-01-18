package html_parser;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class fileXML {

	public static void write(Ad[] mass) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException{
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = factory.newDocumentBuilder().newDocument();
		
		Element root = doc.createElement("root");
		root.setAttribute("xmlns", "");
		doc.appendChild(root);
		
		for(int k = 0; k<10; k++){   		
			
			if(mass[k].ad_title != null){  // ѕровер€ем, есть ли еще объ€влени€, если нет, выходим из цикла
				
				Element ad = doc.createElement("ad");
				ad.setTextContent("ќбъ€вление є" + mass[k].id);
				root.appendChild(ad);
				 
				Element ad_title = doc.createElement("title");
				ad_title.setTextContent(mass[k].ad_title);
				ad.appendChild(ad_title);
				
				Element ad_src = doc.createElement("src");
				ad_src.setTextContent(mass[k].ad_src);
				ad.appendChild(ad_src);
	
				Element ad_image = doc.createElement("image");
				ad_image.setTextContent(mass[k].ad_image);
				ad.appendChild(ad_image);
				
				Element ad_desc = doc.createElement("desc");
				ad_desc.setTextContent(mass[k].ad_desc);
				ad.appendChild(ad_desc);
				
				 for(int u = 0; u<9; u++){
	        		 
	        		Element param = doc.createElement("param" + u);
	        		param.setTextContent(mass[k].ad_params[u]);
	     			ad.appendChild(param);    		
	        	 }
			 
			}else{
					break;
				} 
			 try {
		            Transformer tr = TransformerFactory.newInstance().newTransformer();
		            DOMSource source = new DOMSource(doc);
		            FileOutputStream fos = new FileOutputStream("file.xml");
		            StreamResult result = new StreamResult(fos);
		            tr.transform(source, result);
		        } catch (TransformerException | IOException e) {
		            e.printStackTrace(System.out);
		        }
    	}
		
	}
	
	public static void main(String[] args) {

    }

}
