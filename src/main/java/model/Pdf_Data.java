package model;

public class Pdf_Data {
	//this class receive data from pdf file
	private Double x;
	private Double y;
	private double height;
	private double width;
	private double yscale;
	private String c_data;
	private String font;
	private float font_size;
	private int R;
	private int G;
	private int B;
	private double page_width;
	private double page_height;
	private double space_width;
	
	
	public double getSpace_width() {
		return space_width;
	}


	public void setSpace_width(double space_width) {
		this.space_width = space_width;
	}


	public Pdf_Data()
	{
	}
	
	
	public double getPage_width() {
		return page_width;
	}


	public void setPage_width(double page_width) {
		this.page_width = page_width;
	}


	public double getPage_height() {
		return page_height;
	}


	public void setPage_height(double page_height) {
		this.page_height = page_height;
	}


	public int getR() {
		return R;
	}


	public void setR(int r) {
		R = r;
	}


	public int getG() {
		return G;
	}


	public void setG(int g) {
		G = g;
	}


	public int getB() {
		return B;
	}


	public void setB(int b) {
		B = b;
	}


	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public float getFont_size() {
		return font_size;
	}
	public void setFont_size(float font_size) {
		this.font_size = font_size;
	}
	public String getC_data() {
		return c_data;
	}
	public void setC_data(String c_data) {
		this.c_data = c_data;
	}
	public Double getX() {
		return x;
	}
	public void setX(double x) {
		this.x=new Double(x);
	}
	public Double getY() {
		return y;
	}
	public void setY(double y) {
		this.y=new Double(y);
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getYscale() {
		return yscale;
	}
	public void setYscale(double yscale) {
		this.yscale = yscale;
	}
	
}
