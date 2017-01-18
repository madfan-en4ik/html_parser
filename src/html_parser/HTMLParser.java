package html_parser;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
  

public class HTMLParser{
	
	// Используются общие переменные для парсинга всех объявлений на 1 странице
	
	public int id;
	public String region; 
	public String make;
	
	// first_page_src - специальная переменная, хранящая адрес самого 1 объявления, чтобы в дальнейшем его сравнивать с другими объявлениями и определять какая страница открыта
	// особенность сайта в том, что при переходе на несуществующую страницу при проходе цикла (Например, rst.ua/oldcars/crimea/2.html - существует, а rst.ua/oldcars/crimea/3.html уже нет)
	// сайт перекидывает на главную rst.ua/oldcars/crimea/
	public static String first_page_src;
	
	// глобальная переменная, определяющая перекинули нас на главную страницу или нет, то есть закончились ли все страницы с объявлениями
	public static boolean isFirstPage = false;
	
	// массив, хранящий параметры, их всего 9, из-за особенностей сайта
	static String[] array = new String[9];
	// доп переменные
	static String title, image_src, link, desc;
	// массив со всеми объявлениями на 1 странице
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
	
        		// C помощью библиотеки JSOUP разбираем исходный код по тегам и классам, и находим нужные нам элементы 
	            Document doc = Jsoup.connect("http://rst.ua/oldcars/" + region + "/" + make + "/" + page + ".html").timeout(10*1000).get();           
	            
	            //Разбор блока объявлений, находим объявления по их общему классу
	            int m = 0;
	            Elements ads = doc.getElementsByClass("rst-ocb-i");
	            
	            // Прогоняем цикл по каждому объявлению
	            for (Element ad : ads){
	            	             	
	            	// Получаем html одного объявления (особенности библиотеки)
	            	String InnerData = ad.html();
	            	Document doc2 = Jsoup.parseBodyFragment(InnerData);
	            	
	            	// Получаем заголовок объявления
	            	title = doc2.getElementsByClass("rst-uix-b-item-head").text();
	       		 	
	            	
	            	System.out.println(title);              	
	 		 	
	            	// Парсим изображения
		       		Elements images = doc2.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
		       	    for (Element image : images) 
		       	    {
		       	    	image_src = image.attr("src");
		       	        System.out.println("Изображение src : " + image_src);	       	       
		       	    }
	       		 	
	       		 	// Находим все спаны, то есть наши параметры объявления и заполняем массив array
	    		 	Elements params = doc2.getElementsByTag("span");
	
	    		 	int i = 0;
	    		 	    		 	
	    		 	for (Element param : params){
	    		 		String parameter = param.text();   		 		
	    		 		array[i] = parameter;
	    		 		System.out.println(parameter);
	    		 		i++;
	    		 		
	    		 	}
	    		 	
	    		 	// Получаем ссылку на объявление
	    		 	link = doc2.getElementsByTag("a").attr("href");
	    		 	System.out.println("rst.ua" + link);
	    		 	
	    		 	// Если стоит галочка парсить описание, парсим его с помощью еще одного запроса. Т.к. сайт корявый, описание невозможно получить сразу
	    		 	// с исходной страницы, поэтому необходимо делать доп. переход по ссылке 
	    		 	if(parse_desc == true){
		    		 	// Описание 
		            	Document doc3 = Jsoup.connect("http://rst.ua" + link).timeout(10*1000).get();
		            	Element description = doc3.getElementById("rst-page-oldcars-item-option-block-container-desc");        	
		            	
		            	try{		            	
								desc = description.text();
								System.out.println(desc);							
							
		            	}catch(NullPointerException ex){	            	    
		            		desc = "";
		            	}
	            	    
	    		 	}else{
		            	desc = ""; // По умолчание описание пустое 
		            	System.out.println(desc);
	    		 	}    	
	    		 	
	    		 	System.out.println("");
	    		 	System.out.println("");    		 	
	    		 	
	            	// Собираем объект объявления
	    		 	advert[m] = new Ad(m, title, image_src, array, link, desc);
	            	
	    		 	// Сохраняем в first_page_src ссылку первого объявления, чтоб в дальнейшем сравнивать
	            	if(page == 1){
	            		first_page_src = advert[0].ad_src;
	            	}	       
	            	else{
	            		// Если текущее объявление совпадает с первым, то это главная
	            		if (advert[m].ad_src.equals(first_page_src)){
			            	HTMLParser.isFirstPage = true;			            	 
			            	break;
			        }}	         
	            	m++;	     	            	
	
	            }  //for  
	            	                        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Возвращаем массив со всеми объявлениями на странице
		return advert;
      
      }
	
	// Возможно данная функция лишняя, но я решил перестраховаться ею
	public static Boolean isFirstPage(String region, String make, int page){
        
		return HTMLParser.isFirstPage;
    	 
	}
	
}

