package jju;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

//五子棋界面窗口类
public class CFrame extends JFrame implements MouseListener {
	// 定义游戏中几个常量
	final int N = 10; // 棋盘的维度
	final int D = 50; // 棋盘每个格子的大小
	final int x0 = 100, y0 = 100; // 棋盘的左上角坐标
	// 定义棋盘上每个顶点的位置状态的二位数组
	int[][] pos = new int[N][N];
	int chessColor = 1; // 创建一个动态的数组进行存放每一步的落子信息(行号，列号，颜色)
	// (里面的元素可以是不同的类型)
	//
	// ArrayList a =new ArrayList();
	// 定义存放每一步的动态数组
	ArrayList<ChessStep> steps = new ArrayList<ChessStep>();

	// 创建构造方法
	public CFrame() {
		setTitle("我的五子棋游戏（单机版）v0.1");
		setSize(1000, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置对窗体的鼠标监听事件
		addMouseListener(this);
		// setResizable(false);
	}

	public void paint(Graphics g) {
		// 通过graphic画布，来绘制屏幕
		// 1.绘制棋盘
		for (int i = 1; i <= N; i++) {
			// 第i条横线
			g.setColor(Color.red);
			g.drawLine(x0, y0 + (i - 1) * D, x0 + (N - 1) * D, y0 + (i - 1) * D);
			// 第i条竖线
			g.drawLine(x0 + (i - 1) * D, y0, x0 + (i - 1) * D, y0 + (N - 1) * D);

		}
		// 2.绘制棋子， --遍历棋盘上的每个顶点
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {

				int status = pos[i][j];
				if (status == 0)
					continue;
				if (status == 1)
					g.setColor(Color.green);
				else
					g.setColor(Color.blue);
				g.fillOval(x0 + j * D - 10, y0 + i * D - 10, 20, 20);

			}

	}

	public static void main(String[] args) {
		new CFrame().setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		// 1.获取当前鼠标的位置
		int x = e.getX();
		int y = e.getY();
		System.out.println("x:" + x + "y:" + y);
		// 在边界的一半 的范围内;点击交点的范围内取到交点的位置。
		int row = (int) ((y - y0) * 1.0 / D + 0.5);// 很帅,*1.0是因为前面得保证小数的存在
		int col = (int) ((x - x0) * 1.0 / D + 0.5);// 强制取整;
		// 判断该位置是否落子

		if (pos[row][col] != 0)
			return;
		// pos[row][col]=chessColor;

		// 3.设置棋盘上给位置的状态
		pos[row][col] = chessColor;// ? ?当前棋子的颜色
		// !!!将该步棋，存放到动态数组steps中
		ChessStep obj = new ChessStep(row, col, chessColor);
		steps.add(obj);
		// (设置下一步棋子的颜色)
		chessColor = chessColor == 1 ? 2 : 1;
		/*
		 * if(chessColor==1) chessColor=2; else chessColor=1;
		 */
		// 上面一样的方法
		// 刷新屏幕，添加棋子:
		repaint();

		// 进行下子的时候，进行判断胜负
		int result = checkWinner();
		if (result > 0) {
			String winner = result == 1 ? "黑方获胜" : "白方获胜";
			JOptionPane.showMessageDialog(null, "该游戏结束" + winner);
			// 进行游戏的复盘
			// 启动ReviewThread线程
			System.out.println("游戏开始复盘！！！");
			new ReviewThread().start();
			// 自动存盘;
			saveData();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/* 定义自动判胜负的方法 */
	// 返回值----0 没有分出胜负 1---黑方胜 2---白方胜
	int checkWinner() {
		int result = 0;

		// 从左到右，从上到下,依次遍历棋盘上的每个非空顶点
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				if (pos[i][j] == 0)
					continue;

				// (i,j)位置有棋子，则以(i,j)为起点进行8个方向的搜索;
				// 1.向 右 方向进行搜索 4个位置
				int count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (j + k) < N; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i][j + k] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];

				// 2.从 左 方向开始

				// (i,j)位置有棋子，则以(i,j)为起点进行8个方向的搜索;

				count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (j - k) >= 0; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i][j - k] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];

				// 3.从 上 开始搜索

				// (i,j)位置有棋子，则以(i,j)为起点进行8个方向的搜索;

				count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (i - k) >= 0; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i - k][j] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];

				// 4.从 下 开始搜索

				// (i,j)位置有棋子，则以(i,j)为起点进行8个方向的搜索;

				count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (i + k) < N; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i + k][j] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];

				// 5.从 左上 开始搜索

				// (i,j)位置有棋子，则以(i,j)为起点进行8个方向的搜索;
				// 1.向 上 方向进行搜索 4个位置
				count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (i - k) >= 0 && (j - k) >= 0; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i - k][j - k] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];

				// 6.从 左下 开始搜索

				// (i,j)位置有棋子，则以(i,j)为起点进行8个方向的搜索;

				count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (i + k) < N && (j - k) >= 0; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i + k][j - k] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];

				// 7.从右上 开始搜索

				count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (i + k) < N && (j - k) >= 0; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i + k][j - k] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];
				// 8.从 右下 开始搜索
				count = 1;// 相同的颜色的棋子
				for (int k = 1; k <= 4 && (i + k) < N && (j + k) < N; k++) {
					// 判定 是否与pos[i][j]的棋子颜色相同
					if (pos[i + k][j + k] == pos[i][j])
						count++;
					else
						break;
				}
				if (count == 5) // 存在5子连珠，返回该方棋子获胜
					return pos[i][j];
			}

		return result;
	}

	// 定义一个线程类ReviewThread..用于悔棋;
	//
	class ReviewThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 清除已有棋盘上的棋子
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++) {
					pos[i][j] = 0;
				}
			// 按顺序从steps取出一个棋子信息
			// for (ChessStep obj : steps) {

			for (ChessStep obj : steps) {
				int row = obj.getRow();
				int col = obj.getCol();
				int chessColor = obj.getChessColor();
				// 设置屏幕棋盘格子的状态
				pos[row][col] = chessColor;

				repaint();// 重新绘制屏幕

				try {
					sleep(1000 * 3);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
			// super.run();
		}

	}// end class

	// 定义一个存盘的方法saveData()
	public void saveData() {
		// 将存放在棋盘信息数组steps中的数据
		// 写入到文件中

		String dt = "201709271020";
		String filename = "E:\\" + dt + ".txt";
		Path f1 = Paths.get(filename);
		try {
			OutputStream os = Files.newOutputStream(f1,
					StandardOpenOption.CREATE);
			PrintWriter pw = new PrintWriter(os);
			// 遍历steps数组
			for (ChessStep obj : steps) {
				int row = obj.getRow();
				int col = obj.getCol();
				int color = obj.getChessColor();
				String info = row + "#" + col + "#" + color;
				pw.println(info);
				pw.flush();

			}
			pw.close();
			os.close();
			JOptionPane.showMessageDialog(null, "保存成功!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}// end saveData

	//
	public void loadData() {
		String filename = "";
		// 弹出文件选择对话框
		FileDialog dig = new FileDialog(this, "Open", FileDialog.LOAD);
		dig.setVisible(true);
		filename = dig.getDirectory() + "\\" + dig.getFile();
		System.out.println(filename);

		// filename="E:\\201709271020.txt";
		Path f1 = Paths.get(filename);
		try {
			BufferedReader br = Files.newBufferedReader(f1,
					Charset.defaultCharset());
			// 清空已经存放的棋盘信息
			steps.clear();
			// 遍历存放文件
			while (true) {
				String info = br.readLine();
				if (info == null)
					break;
				// 解析某一行信息，按照#分割成若干位置

				String[] ss = info.split("#");

				int row = Integer.parseInt(ss[0]);
				int col = Integer.parseInt(ss[1]);
				int color = Integer.parseInt(ss[2]);
				// 构建一个棋子对象
				ChessStep obj = new ChessStep();
				// 放入到数组中
				steps.add(obj);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
