package baiToan8QuanHau;

public class OKhongHopLe {
	private OCo[] oco = new OCo[8];
	private int top;
	public OKhongHopLe() {
		top=0;
		for (int i=0; i<8; i++)
			oco[i] = new OCo();
	}
	public void add(int x, int y) {
		oco[top].Change(x, y);
		top=top+1;
	}
	public void remove(int x, int y) {
		int pos=0;
		for (int i=0; i<top; i++)
			if (oco[i].GetX()==x && oco[i].GetY()==y)
				pos=i;
		for (int i=pos; i<top-1; i++)
			oco[i].Change(oco[i+1].GetX(), oco[i+1].GetY());
		top=top-1;
	}
	public boolean inIt(int x, int y) {
		for (int i=0; i<top; i++)
			if (oco[i].GetX()==x && oco[i].GetY()==y) return true;
		return false;
	}
	public void print() {
		for (int i=0; i<top; i++)
			System.out.print(oco[i].GetX()+"-"+oco[i].GetY()+", ");
		System.out.println("");
	}
	public int getTop() {
		return top;
	}
	public OCo getOCo(int i) {
		return oco[i];
	}
}
