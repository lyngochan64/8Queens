package baiToan8QuanHau;

public class OCo {
	private int x,y;
	public OCo() {
		x=y=0;
	}
	public OCo(int i, int j) {
		x=i; y=j;
	}
	public void Change(int i, int j) {
		x=i; y=j;
	}
	public int GetX() {
		return x;
	}
	public int GetY() {
		return y;
	}
	public boolean isEqual(OCo x2) {
		if (x2.GetX()!=x || x2.GetY()!=y) return false;
		return true;
	}
	

}
