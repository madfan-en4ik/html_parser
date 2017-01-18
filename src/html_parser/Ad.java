package html_parser;

public class Ad {
	
	// ќснонвые пол€: id - номер объ€влени€, тайтл, ссылка на изображение, массив с параметрами, ссылка на объ€вление, описание
	
	public int id;
	public String ad_title;
	public String ad_image;
	public String ad_params[];
	public String ad_src;
	public String ad_desc;
	
	public Ad(int id, String ad_title, String ad_image, String[] ad_params, String ad_src, String ad_desc) {
		super();
		this.id = id;
		this.ad_title = ad_title;
		this.ad_image = ad_image;
		this.ad_params = ad_params;
		this.ad_src = ad_src;
		this.ad_desc = ad_desc;
		
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
