package html_parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
 
public class fileJson {
     
    private static final String FILENAME= System.getProperty("user.dir") + "\\file.json";
     
    @SuppressWarnings("unchecked")
	public static void write(Ad[] mass){
    	JSONObject object = new JSONObject();
        JSONArray messages = new JSONArray();
    	
        for(int k = 0; k<10; k++){   		
        	
        	if(mass[k].ad_title != null){ // Проверяем, есть ли еще объявления, если нет, выходим из цикла
        		
        		// Заливаем в файл все параметры объявления
	        	messages.add(mass[k].id);
	        	messages.add(mass[k].ad_title);
	        	messages.add(mass[k].ad_src);
	        	messages.add(mass[k].ad_image);  
	        	messages.add(mass[k].ad_desc); 
	        	  for(int u = 0; u<9; u++){
	        		  messages.add(mass[k].ad_params[u]);            
	        	  }
	        	messages.add("____________________________");
	            object.put("messages", messages);
	            
	    	}else{
	    		break;
	    	}
    	
        }
    	
        try (FileWriter writer = new FileWriter(FILENAME, true)){
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(fileJson.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {

    }
}