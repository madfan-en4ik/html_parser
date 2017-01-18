
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
	// ������ ����� �� ����������, ��� � �������� �����, ���� � ���������� �� ���������������� � ������������ 
	public String region, make, model;
	public boolean desc, json, xml;

    public Form()
    {
        super("FlowLayout");
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize(650, 150);
        // ������ �����������
        Container container = getContentPane();
        /*
         * ����������� ����������������� ������������ 
         * � ������������� ����������� �� ������
         */
        container.setLayout (new FlowLayout(FlowLayout.CENTER));
        // ��������� ����������
        JLabel      labelRegion           = new JLabel("������ :"); 
        JLabel      labelMake           = new JLabel("����� :"); 
        JLabel      labelModel           = new JLabel("������ :"); 
        
        final String[] regions = {
        	    "������� ������",
        	    "��� �������",
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
        final JComboBox 	comboBoxRegion 	= new JComboBox(regions);   // ���������� ������ ��������     
        final JTextField  textFieldMake 	= new JTextField(10); 		// ����� 
        final JTextField  textFieldModel 	= new JTextField(10);    // ������    
        final JCheckBox   parseDesc 		= new JCheckBox("������� ��������");        
        final JCheckBox   writeJson 		= new JCheckBox("��������� � JSON");        
        final JCheckBox   writeXML 		= new JCheckBox("��������� � XML");        
        JButton     btnParse 		= new JButton("�������"   );
        JButton     btnClear 		= new JButton("��������"   ); 
        final JTextArea  	textArea  		= new JTextArea();         // ���� ��� ������ ������ ��� ����������� ��������
        
        
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
        
        
        // �������������� ��� ���� � ���������� �����������
        
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
            
        // ������� ������� ��������
        
        btnParse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {          
            	
            	// ���������, ��������� �� ������ � �����
            	if(make !=null || region != null){
            		// ���������, ���� ������ regions[1] (� ��� ���������� ����� "��� �������"), �� ������� ���������� region, ���� ��� ����������� � ��� �� ���� ������
            		if(region.equals(regions[1])){
            			region = "";
            			if(make == null){
            				textArea.setText("�������� ����� ����������!");
            			}
            		}else{
            			
            			// �������� ������ ���������
	            		long start_time, finish_time;
	            	    start_time = System.nanoTime();
	            	   
	            	    make = textFieldMake.getText();    
	            		model = textFieldModel.getText();
	            		
	            		// �������������� ������ ����������
	            		Ad[] arr = null;

	            		// ���� � ��� �� ������� ��������, �� ���� ���� �� ������ !HTMLParser.isFirstPage, � ������� �������� �������� ��� ��������
	            		// ������ ����� while �������� 1 �������� � ����� ������������ �� ������ � xml ��� json
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
	            			
							// �������� ������ ������, �� ��� ��� ������ ������� ���, ������� �������
							HTMLParser.isFirstPage = HTMLParser.isFirstPage(region, make+"/"+model, page);
							System.out.println(HTMLParser.isFirstPage);
	
							// �������� ��������� ������ � ���� 							
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
	            		// ������� ���������� ����������
	            		int parsed_ads = 0;
	            		try{
	            			parsed_ads = (page-1)*10 + arr[arr.length-1].id + 1;           		
	            		}catch(NullPointerException ex){	            	    
	            			System.out.println("NullPointerException");
		            	}
	            		// ������� ����� ������ � �������
	            		finish_time = System.nanoTime();
	            		double result = finish_time - start_time;
	            		result = result/1000000000; 
	            		textArea.setText(result + " ������; �������� " + parsed_ads + " ����������");
            			}
            	}else{
	            	textArea.setText("�������� ������ ��� ����� ����������!");
	            }
	            
            }
            
        });
        
        
        // ��������� ����
        setVisible(true);
    }
    

        
    public static void main(String[] args) {

    }
}