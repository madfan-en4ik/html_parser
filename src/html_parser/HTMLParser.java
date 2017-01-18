package html_parser;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
  

public class HTMLParser{
	
	// ������������ ����� ���������� ��� �������� ���� ���������� �� 1 ��������
	
	public int id;
	public String region; 
	public String make;
	
	// first_page_src - ����������� ����������, �������� ����� ������ 1 ����������, ����� � ���������� ��� ���������� � ������� ������������ � ���������� ����� �������� �������
	// ����������� ����� � ���, ��� ��� �������� �� �������������� �������� ��� ������� ����� (��������, rst.ua/oldcars/crimea/2.html - ����������, � rst.ua/oldcars/crimea/3.html ��� ���)
	// ���� ������������ �� ������� rst.ua/oldcars/crimea/
	public static String first_page_src;
	
	// ���������� ����������, ������������ ���������� ��� �� ������� �������� ��� ���, �� ���� ����������� �� ��� �������� � ������������
	public static boolean isFirstPage = false;
	
	// ������, �������� ���������, �� ����� 9, ��-�� ������������ �����
	static String[] array = new String[9];
	// ��� ����������
	static String title, image_src, link, desc;
	// ������ �� ����� ������������ �� 1 ��������
    static Ad[] advert = new Ad[10];
    
	public HTMLParser(int id, String region, String make, String title){
		super();
		this.id = id;
		this.region = region;
		this.make = make;
	}
	
	
    public static void main(String[] args){
		
	}
    
	public static Ad[] parsePage(String region, String make, int page, boolean parse_desc) throws Exception{
    	     
        try {
	
        		// C ������� ���������� JSOUP ��������� �������� ��� �� ����� � �������, � ������� ������ ��� �������� 
	            Document doc = Jsoup.connect("http://rst.ua/oldcars/" + region + "/" + make + "/" + page + ".html").timeout(10*1000).get();           
	            
	            //������ ����� ����������, ������� ���������� �� �� ������ ������
	            int m = 0;
	            Elements ads = doc.getElementsByClass("rst-ocb-i");
	            
	            // ��������� ���� �� ������� ����������
	            for (Element ad : ads){
	            	             	
	            	// �������� html ������ ���������� (����������� ����������)
	            	String InnerData = ad.html();
	            	Document doc2 = Jsoup.parseBodyFragment(InnerData);
	            	
	            	// �������� ��������� ����������
	            	title = doc2.getElementsByClass("rst-uix-b-item-head").text();
	       		 	
	            	
	            	System.out.println(title);              	
	 		 	
	            	// ������ �����������
		       		Elements images = doc2.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
		       	    for (Element image : images) 
		       	    {
		       	    	image_src = image.attr("src");
		       	        System.out.println("����������� src : " + image_src);	       	       
		       	    }
	       		 	
	       		 	// ������� ��� �����, �� ���� ���� ��������� ���������� � ��������� ������ array
	    		 	Elements params = doc2.getElementsByTag("span");
	
	    		 	int i = 0;
	    		 	    		 	
	    		 	for (Element param : params){
	    		 		String parameter = param.text();   		 		
	    		 		array[i] = parameter;
	    		 		System.out.println(parameter);
	    		 		i++;
	    		 		
	    		 	}
	    		 	
	    		 	// �������� ������ �� ����������
	    		 	link = doc2.getElementsByTag("a").attr("href");
	    		 	System.out.println("rst.ua" + link);
	    		 	
	    		 	// ���� ����� ������� ������� ��������, ������ ��� � ������� ��� ������ �������. �.�. ���� �������, �������� ���������� �������� �����
	    		 	// � �������� ��������, ������� ���������� ������ ���. ������� �� ������ 
	    		 	if(parse_desc == true){
		    		 	// �������� 
		            	Document doc3 = Jsoup.connect("http://rst.ua" + link).timeout(10*1000).get();
		            	Element description = doc3.getElementById("rst-page-oldcars-item-option-block-container-desc");        	
		            	
		            	try{		            	
								desc = description.text();
								System.out.println(desc);							
							
		            	}catch(NullPointerException ex){	            	    
		            		desc = "";
		            	}
	            	    
	    		 	}else{
		            	desc = ""; // �� ��������� �������� ������ 
		            	System.out.println(desc);
	    		 	}    	
	    		 	
	    		 	System.out.println("");
	    		 	System.out.println("");    		 	
	    		 	
	            	// �������� ������ ����������
	    		 	advert[m] = new Ad(m, title, image_src, array, link, desc);
	            	
	    		 	// ��������� � first_page_src ������ ������� ����������, ���� � ���������� ����������
	            	if(page == 1){
	            		first_page_src = advert[0].ad_src;
	            	}	       
	            	else{
	            		// ���� ������� ���������� ��������� � ������, �� ��� �������
	            		if (advert[m].ad_src.equals(first_page_src)){
			            	HTMLParser.isFirstPage = true;			            	 
			            	break;
			        }}	         
	            	m++;	     	            	
	
	            }  //for  
	            	                        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // ���������� ������ �� ����� ������������ �� ��������
		return advert;
      
      }
	
	// �������� ������ ������� ������, �� � ����� ���������������� ��
	public static Boolean isFirstPage(String region, String make, int page){
        
		return HTMLParser.isFirstPage;
    	 
	}
	
}

