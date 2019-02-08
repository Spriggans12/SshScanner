package fr.spriggans.models;

public class MyBoolean {

	private boolean b;
	
	public MyBoolean() {
		b = false;
	}
	
	public MyBoolean(boolean b) {
		this.b = b;
	}

	public boolean isB() {
		return b;
	}

	public void setB(boolean b) {
		this.b = b;
	}
}
