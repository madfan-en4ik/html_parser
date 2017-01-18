
package html_parser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class Form extends JFrame
{
	// Создаю такие же переменные, как и элементы формы, чтоб в дальнейшем их инициализировать и использовать 
	public String region, make, model;
	public boolean desc, json, xml;

    public Form()
    {
        super("FlowLayout");
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize(650, 150);
        // Панель содержимого
        Container container = getContentPane();
        /*
         * Определение последовательного расположения 
         * с выравниванием компонентов по центру
         */
        container.setLayout (new FlowLayout(FlowLayout.CENTER));
        // добавляем компоненты
        JLabel      labelRegion           = new JLabel("Регион :"); 
        JLabel      labelMake           = new JLabel("Марка :"); 
        JLabel      labelModel           = new JLabel("Модель :"); 
        
        final String[] regions = {
        	    "Выбрать регион",
        	    "без региона",
        		"crimea",
        	    "lvov",
        	    "kiev",
        	    "vinnica",
        	    "volyn",
        	    "dnepropetrovsk",
        	    "donetsk",
        	    "zhitomir",
        	    "zakarpatie",
        	    "zaporozhie",
        	    "ivano-frankovsk",        	 
        	    "kirovograd",
        	    "nikolaev",
        	    "odessa",
        	    "poltava",
        	    "rovno",
        	    "sumy",
        	    "ternopol",
        	    "kharkov",
        	    "kherson",
        	    "cherkassy",
        	    "chernigov",
        	    "khmelnitsk"
        	};       
        final JComboBox 	comboBoxRegion 	= new JComboBox(regions);   // выпадающий список регионов     
        final JTextField  textFieldMake 	= new JTextField(10); 		// марка 
        final JTextField  textFieldModel 	= new JTextField(10);    // модель    
        final JCheckBox   parseDesc 		= new JCheckBox("Парсить описание");        
        final JCheckBox   writeJson 		= new JCheckBox("Сохранить в JSON");        
        final JCheckBox   writeXML 		= new JCheckBox("Сохранить в XML");        
        JButton     btnParse 		= new JButton("Парсить"   );
        JButton     btnClear 		= new JButton("Очистить"   ); 
        final JTextArea  	textArea  		= new JTextArea();         // Поле для вывода ошибок или результатов парсинга
        
        
        container.add(labelRegion);     
        container.add(comboBoxRegion);  
        
        container.add(labelMake);     
        container.add(textFieldMake);   
        
        container.add(labelModel);     
        container.add(textFieldModel); 
        
        container.add(parseDesc);     
        container.add(writeJson);     
        container.add(writeXML);     
        container.add(btnParse);
        container.add(btnClear);   
        
        container.add(textArea); 
        
        
        // Инициализируем все поля с созданными переменными
        
        comboBoxRegion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JComboBox box = (JComboBox)e.getSource();
                region = (String)box.getSelectedItem();               
            }
        });
        
        textFieldMake.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	make = textFieldMake.getText();            	         
            }
        });
        
        textFieldModel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	model = textFieldModel.getText();
            }
        });
        
        parseDesc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(parseDesc.isSelected()){
            		desc = true;
            	}            	 
            }
        });
        
        writeJson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(writeJson.isSelected()){
            		json = true;
            	}            	 
            }
        });
        
        writeXML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(writeXML.isSelected()){
            		xml = true;
            	}          	 
            }
        });
        
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	comboBoxRegion.setSelectedItem(null);
            	textFieldMake.setText(null);
            	textFieldModel.setText(null);
            	parseDesc.setSelected(false);
            	writeJson.setSelected(false);
            	writeXML.setSelected(false);
            }
        });
            
        // Главная функция парсинга
        
        btnParse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {          
            	
            	// Проверяем, заполнены ли регион и марка
            	if(make !=null || region != null){
            		// Проверяем, если выбран regions[1] (а там содержится текст "без региона"), то очищаем переменную region, чтоб при подстановке в урл не было ошибки
            		if(region.equals(regions[1])){
            			region = "";
            			if(make == null){
            				textArea.setText("Выберите марку автомобиля!");
            			}
            		}else{
            			
            			// Засекаем начало программы
	            		long start_time, finish_time;
	            	    start_time = System.nanoTime();
	            	   
	            	    make = textFieldMake.getText();    
	            		model = textFieldModel.getText();
	            		
	            		// Инициализируем массив объявлений
	            		Ad[] arr = null;

	            		// Пока у нас не главная страница, то есть пока не первая !HTMLParser.isFirstPage, с помошью счетчика проходим все страницы
	            		// Внутри цикла while парсится 1 страница и сразу записывается по выбору в xml или json
	            		int page = 0;
	            		while(!HTMLParser.isFirstPage){
	            			page++;
	            			System.out.println(page);
	            			           			
							try {
								arr = HTMLParser.parsePage(region, make+"/"+model, page, desc);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}						
	            			
							// Возможно лишняя запись, но без нее коряво работал код, поэтому добавил
							HTMLParser.isFirstPage = HTMLParser.isFirstPage(region, make+"/"+model, page);
							System.out.println(HTMLParser.isFirstPage);
	
							// Проверка параметра записи в файл 							
	            			if(json == true){
	            				fileJson.write(arr);
	            			}
	            			if(xml == true){
	            				try {
									fileXML.write(arr);
								} catch (ParserConfigurationException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (TransformerFactoryConfigurationError e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (TransformerException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	            			}
	            		}
	            		// Считаем спарсенные объявления
	            		int parsed_ads = 0;
	            		try{
	            			parsed_ads = (page-1)*10 + arr[arr.length-1].id + 1;           		
	            		}catch(NullPointerException ex){	            	    
	            			System.out.println("NullPointerException");
		            	}
	            		// Считаем время работы и выводим
	            		finish_time = System.nanoTime();
	            		double result = finish_time - start_time;
	            		result = result/1000000000; 
	            		textArea.setText(result + " секунд; получено " + parsed_ads + " объявлений");
            			}
            	}else{
	            	textArea.setText("Выберите регион или марку автомобиля!");
	            }
	            
            }
            
        });
        
        
        // Открываем окно
        setVisible(true);
    }
    

        
    public static void main(String[] args) {

    }
}