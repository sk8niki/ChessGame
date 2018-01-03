package jju;

public class ChessStep {
	int row;//行号
	int col;//列号
	int chessColor;//棋子颜色
	
	//创建一个set/get方法
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getChessColor() {
		return chessColor;
	}
	
	
	public void setChessColor(int chessColor) {
		this.chessColor = chessColor;
	}
	//创建构造的方法
	public ChessStep() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChessStep(int row, int col, int chessColor) {
		super();
		this.row = row;
		this.col = col;
		this.chessColor = chessColor;
	}
	
	
	

}
