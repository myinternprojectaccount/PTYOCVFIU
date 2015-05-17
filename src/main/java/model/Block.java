package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block {

	//this class for model xml box element to save properties
	private int block_id;
	private String flow;
	private String type;
	private int flow_ref_id;
	private List<Block> komsular;
	private String data;
	private HashMap<String,String> properties;
	public Block()
	{
		properties=new HashMap<String,String>();
		komsular=new ArrayList<Block>();
		
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getBlock_id() {
		return block_id;
	}
	public void setBlock_id(int block_id) {
		this.block_id = block_id;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HashMap<String, String> getProperties() {
		return properties;
	}
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
	public int getFlow_ref_id() {
		return flow_ref_id;
	}
	public void setFlow_ref_id(int flow_ref_id) {
		this.flow_ref_id = flow_ref_id;
	}
	public List<Block> getKomsular() {
		return komsular;
	}
	public void setKomsular(List<Block> komsular) {
		this.komsular = komsular;
	}
	
	
}
