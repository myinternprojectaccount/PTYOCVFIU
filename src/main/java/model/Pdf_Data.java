package model;

public class Pdf_Data {
	//this class receive data from pdf file
	private double x;
	private double y;
	private double height;
	private double width;
	private double yscale;
	private String c_data;
	private String font;
	private float font_size;
	
	
	
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
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
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
