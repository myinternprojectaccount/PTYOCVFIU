package model;

import java.util.HashMap;
import java.util.Map;

public class Header {

	private int headerId;
	private Map<String,String> properties;
	String data;
	public Header()
	{
		properties=new HashMap<String,String>();
	}
	public int getHeaderId() {
		return headerId;
	}
	public void setHeaderId(int headerId) {
		this.headerId = headerId;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
