package baiToan8QuanHau;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import java.util.*;

public class BanCo implements ActionListener{
	
	JFrame frame;
	Scanner sc;
//TITLE PANEL
	JPanel titlePanel;
	JLabel titleText;
//BUTTON PANEL
	JPanel buttonPanel;
	JButton[][] buttons;
//OPTION PANEL
	JPanel optionPanel;
	JPanel optionPanel1;
	JPanel optionPanel2;
	JPanel optionPanel3;
	JComboBox colorCbb;
	JComboBox iconCbb;
//	JComboBox speedCbb;
	JButton clearButton;
	JButton quitButton;
	JButton autoSolve;
	JButton nextButton;
	JButton stopButton;
	JToggleButton showPS;
	JLabel psLabel;
	
//WELCOME PANEL
	JPanel welcomePanel = new JPanel();
//MAIN PANEl
	JPanel mainPanel = new JPanel();
	JButton startButton;
	JButton exitButton;
//	JButton exploreMode;
//CONTAINER PANEL
	JPanel contPanel = new JPanel();
//CARD LAYOUT
	CardLayout card = new CardLayout();
//OTHER
	
	private int timer = 30;
	private int solution=0;
	private int[][] banCo = new int[8][8];
	private boolean stop=false;
	private boolean auto_solving = false;
	private int stt=0;
	
	ImageIcon leo = new ImageIcon(getClass().getResource("/image/leo.png"));
	ImageIcon queen = new ImageIcon(getClass().getResource("/image/queen.png"));
	ImageIcon panda = new ImageIcon(getClass().getResource("/image/panda.png"));
	ImageIcon icon = queen;
	
	ImageIcon crown = new ImageIcon(getClass().getResource("/image/crown.png"));
	ImageIcon crown0 = new ImageIcon(getClass().getResource("/image/crown_0.png"));
	
	Color blue = new Color(0x4b7399), green = new Color(0x769656);
	Color brown = new Color(0xb58863);
	
	Color mauBanCo = green; //Xanh la
	Color mauDanhLoi = new Color(0xdf0d0d);
	Color mauTrang = new Color(0xeeeed2);
	Color mauHoanThanh = new Color(0xffff17);
	OKhongHopLe oKhongHopLe = new OKhongHopLe(); //Luu danh sach cac o khong hop le (dung de to mau)
	  
	List<OCo> dsQuanHau = new ArrayList<OCo>();
	int dapAn[][][] = new int[92][8][8];
	
	
	public BanCo() {
		frame = new JFrame();
		sc  = new Scanner(System.in);
		khoiTaoBanCo(banCo);
		frame.setSize(770,680);
		try {
			getDapAn(banCo,0);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		//CREATE NEW FONT
		Font customFont = null;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("customFont.otf")).deriveFont(32f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(customFont);
		//================
		Image crown_resize = crown.getImage();
		crown_resize = crown_resize.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);
		crown = new ImageIcon(crown_resize);
		
		crown_resize = crown0.getImage();
		crown_resize = crown_resize.getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH);
		crown0 = new ImageIcon(crown_resize);
		//================
				
		//TITLE PANEL SETTING
		titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setBackground(Color.green);
		titlePanel.setPreferredSize(new Dimension(1,30));
		
		titleText = new JLabel();
		titleText.setBackground(new Color(0xbebdb9));
		titleText.setForeground(Color.black);
		titleText.setFont(new Font("Arial",Font.BOLD,15));
		titleText.setHorizontalAlignment(JLabel.CENTER);
		titleText.setText("Chọn một ô bất kỳ để bắt đầu trò chơi!");
		titleText.setOpaque(true);
		
		titlePanel.add(titleText);
		//BUTTON PANEL SETTING
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(8,8));
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		buttons = new JButton[8][8];
		for(int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				buttons[i][j] = new JButton();
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].setFocusable(false);
				if ((i+j)%2==0) buttons[i][j].setBackground(mauTrang);
				else buttons[i][j].setBackground(mauBanCo);
				
				buttons[i][j].addActionListener(this);
				buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
				
			}
		}	
		//OPTION PANEL SETTING
		optionPanel = new JPanel();
		optionPanel.setPreferredSize(new Dimension(120,1));
		optionPanel.setLayout(new FlowLayout(FlowLayout.LEADING,20,10));
		clearButton = new JButton("Clear");
		clearButton.setFocusable(false);
		clearButton.setBackground(mauBanCo);
		clearButton.setForeground(mauTrang);
		clearButton.setPreferredSize(new Dimension(100,30));
		clearButton.addActionListener(this);
		
		JLabel colorLabel = new JLabel("Board color:");
		String[] color = { "Green","Blue"};
		colorCbb = new JComboBox(color);
		colorCbb.setBackground(mauBanCo);
		colorCbb.setForeground(mauTrang);
		colorCbb.setPreferredSize(new Dimension(100,30));
		colorCbb.addActionListener(this);
		
//		JLabel speedLabel = new JLabel("Speed:");
//		String[] speed = { "1","2","3"};
//		speedCbb = new JComboBox(speed);
//		speedCbb.setBackground(mauBanCo);
//		speedCbb.setForeground(mauTrang);
//		speedCbb.setPreferredSize(new Dimension(100,30));
//		speedCbb.addActionListener(this);
		
		JLabel iconLabel = new JLabel("Piece icon:");
		String[] icon_list = {"Queen", "Leo", "Panda"};
		iconCbb = new JComboBox(icon_list);
		iconCbb.setBackground(mauBanCo);
		iconCbb.setForeground(mauTrang);
		iconCbb.setPreferredSize(new Dimension(100,30));
		iconCbb.addActionListener(this);
		
		
		autoSolve = new JButton("Auto Solve");
		autoSolve.setPreferredSize(new Dimension(100,30));
		autoSolve.setBackground(mauBanCo);
		autoSolve.setForeground(mauTrang);
		autoSolve.setFocusable(false);
		autoSolve.addActionListener(this);
		
		nextButton = new JButton("Next");
		nextButton.setPreferredSize(new Dimension(100,30));
		nextButton.setBackground(mauBanCo);
		nextButton.setForeground(mauTrang);
		nextButton.setFocusable(false);
		nextButton.setEnabled(false);
		
		JPanel PSPanel = new JPanel();
		
		showPS = new JToggleButton();
		showPS.setIcon(crown0);
		showPS.setPreferredSize(new Dimension(100,60));
		showPS.setBackground(null);
		showPS.setForeground(mauTrang);
		showPS.setFocusable(false);
		showPS.addActionListener(this);
		showPS.setBorder(null);
		showPS.setContentAreaFilled(false);
		
		
		quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(100,30));
		quitButton.setBackground(mauDanhLoi);
		quitButton.setForeground(mauTrang);
		quitButton.setFocusable(false);
		quitButton.addActionListener(this);
		quitButton.setBounds(100, 100, 100, 30);
		
		
		stopButton = new JButton("Stop");
		stopButton.setPreferredSize(new Dimension(100,30));
		stopButton.setBackground(mauBanCo);
		stopButton.setForeground(mauTrang);
		stopButton.setFocusable(false);
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		
		psLabel = new JLabel();
		int temp_solution = checkSolution(banCo);
		psLabel.setText(""+temp_solution);
		psLabel.setFont(new Font("Arial",Font.BOLD,30));
//		psLabel.setBorder(BorderFactory.createEmptyBorder(0, 33, 10, 0));
	
		psLabel.setHorizontalTextPosition(JLabel.CENTER);
		psLabel.setVerticalTextPosition(JLabel.BOTTOM);
		psLabel.setVisible(false);
		
		PSPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		PSPanel.setPreferredSize(new Dimension(100,150));
		PSPanel.add(showPS);
		PSPanel.add(psLabel);
		
		optionPanel.setLayout(new BorderLayout());
		optionPanel1 = new JPanel();
		optionPanel2 = new JPanel();
		optionPanel3 = new JPanel();
		optionPanel.add(optionPanel1,BorderLayout.NORTH);
		optionPanel.add(optionPanel2,BorderLayout.CENTER);
		optionPanel.add(optionPanel3,BorderLayout.SOUTH);

//		optionPanel1.setBackground(Color.red);
//		optionPanel2.setBackground(Color.green);
//		optionPanel3.setBackground(Color.blue);
		optionPanel1.setPreferredSize(new Dimension(1,200));
		optionPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		optionPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		optionPanel3.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		
		
		
		optionPanel1.add(clearButton);
		optionPanel1.add(colorLabel);
		optionPanel1.add(colorCbb);
		optionPanel1.add(iconLabel);
		optionPanel1.add(iconCbb);
	
		
		optionPanel2.add(autoSolve);
		optionPanel2.add(nextButton);
		optionPanel2.add(stopButton);
//		optionPanel2.add(speedLabel);
//		optionPanel2.add(speedCbb);
		optionPanel2.add(PSPanel);
		
		optionPanel3.add(quitButton);
		
		//WELCOME PANEL SETTING
		welcomePanel.setBackground(Color.yellow);
		welcomePanel.setLayout(null);
		JLabel bg = new JLabel();
		ImageIcon background = new ImageIcon(getClass().getResource("/image/bg.png"));
		bg.setIcon(background);
		bg.setText("Hello");
		bg.setForeground(Color.white);
		bg.setBounds(0, 0, 800, 680);
		
		startButton = new JButton("START");
		startButton.setBackground(blue);
		startButton.setBorder(BorderFactory.createEmptyBorder());
		startButton.setOpaque(false);
		startButton.setFocusable(false);
		startButton.setPreferredSize(new Dimension(250,70));
		startButton.setForeground(Color.white);
		startButton.setFont(customFont);
		startButton.addActionListener(this);

		
		exitButton = new JButton("QUIT");
		exitButton.setBackground(green);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		exitButton.setOpaque(false);
		exitButton.setFocusable(false);
		exitButton.setPreferredSize(new Dimension(250,70));
		exitButton.setForeground(Color.white);
		exitButton.setFont(customFont);
		exitButton.addActionListener(this);
		
//		normalMode = new JButton("NORMAL MODE");
//		normalMode.setBackground(green);
//		normalMode.setBorder(BorderFactory.createEmptyBorder());
//		normalMode.setOpaque(false);
//		normalMode.setFocusable(false);
//		normalMode.setPreferredSize(new Dimension(250,70));
//		normalMode.setForeground(Color.white);
//		normalMode.setFont(customFont);
//		normalMode.addActionListener(this);
//		
//		exploreMode = new JButton("EXPLORE MODE");
//		exploreMode.setBackground(green);
//		exploreMode.setBorder(BorderFactory.createEmptyBorder());
//		exploreMode.setOpaque(false);
//		exploreMode.setFocusable(false);
//		exploreMode.setPreferredSize(new Dimension(250,70));
//		exploreMode.setForeground(Color.white);
//		exploreMode.setFont(customFont);
//		exploreMode.addActionListener(this);
		
		JPanel startOptionPanel = new JPanel();
		startOptionPanel.setLayout(new GridLayout(2,1));
		startOptionPanel.setBounds(0,180,800,100);
		startOptionPanel.setOpaque(false);
		
		JPanel option1 = new JPanel();
		JPanel option2 = new JPanel();
		option1.setOpaque(false);
		option2.setOpaque(false);
		
		option1.add(startButton);
//		option1.add(normalMode);
		option2.add(exitButton); 
//		option2.add(exploreMode);
//		normalMode.setVisible(false);
//		exploreMode.setVisible(false);
		
		startOptionPanel.add(option1);
		startOptionPanel.add(option2);
//		startOptionPanel.add(startButton);
		
//		startOptionPanel.add(normalMode);
//		startOptionPanel.add(exploreMode);
		bg.add(startOptionPanel);
		
		
		welcomePanel.add(bg);
		welcomePanel.setVisible(true); 
		
		//MAIN PANEL SETTING
		mainPanel.setBackground(green);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(titlePanel,BorderLayout.NORTH);
		mainPanel.add(buttonPanel, BorderLayout.CENTER);
		mainPanel.add(optionPanel, BorderLayout.EAST);
		
		//CONTAINER PANEL SETTING
		contPanel.setLayout(card);
		contPanel.add(welcomePanel,"1");
		contPanel.add(mainPanel, "2");
		card.show(contPanel, "1");
		
		//FRAME SETTING
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(contPanel);
		Image frameIcon = queen.getImage();
		frameIcon = frameIcon.getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH);
		frame.setIconImage(frameIcon);
		frame.setTitle("Trò Chơi 8 Quân Hậu");
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.pack();

		
	}
	public void khoiTaoBanCo(int banCo[][]) {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++) {
				banCo[i][j]=0;
			}
	}
	public void clearBanCo(int banCo[][]) {
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if (banCo[i][j]==1) {
					banCo[i][j]=0;
					buttons[i][j].setIcon(null);
				}
		ToMau(banCo);
		dsQuanHau.clear();
		psLabel.setText(""+checkSolution(banCo));
	}
	public boolean HopLe(int banCo[][]) {
		int sum=0;
		int i=0,j=0;
		
		//Duyet cac hang ngang
		for (i=0; i<8; i++) {
			for (j=0; j<8; j++)
				sum += banCo[i][j];
			if (sum>1) return false;
			sum=0;
		}
		//Duyet cac hang doc
		for (i=0; i<8; i++) {
			for (j=0; j<8; j++)
				sum += banCo[j][i];
			if (sum>1) return false;
			sum=0;
		}
		
		//Duyet duong cheo chinh
		for (int k=0; k<8; k++) {
			i = k;
			j = 7;
			while (i >= 0) { 
				sum += banCo[i][j];
				i--;
				j--;
			}
			if (sum>1) return false;
			sum=0;
		}
		for (int k=6; k>=0; k--) {
			i=7;
			j=k;
			while (j >= 0 ) {
				sum += banCo[i][j];
				i--;
				j--;
			}
			if (sum>1) return false;
			sum=0;
		}
		//==========================
		//Duyet duong cheo phu
		for (int k=0; k<8; k++) {
			i = k;
			j = 0;
			while (i >= 0) {
				sum += banCo[i][j];
				i--;
				j++;
			}
			if (sum>1) return false;
			sum=0;
		}
		
		sum=0;
		for (int k=1; k<8; k++) {
			i=7;
			j=k;
			while (j <= 7) {
				sum += banCo[i][j];
				i--;
				j++;
			}
			if (sum>1) {
				return false;
			}
			sum=0;
		}
		//==========================
		return true;
	}
	public int TongQuanHau(int banCo[][]) {
		int sum=0;
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++) {
				if (banCo[i][j]==1) sum++;
			}
		return sum;
	}	
	public void InMaTran(int banCo[][]) {
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++)
				System.out.print(banCo[i][j]+"");
			System.out.println("");
		}
		System.out.println("");
	}
	public void ToMau(int banCo[][]) {
		int sum=0;
		//To mau hang ngang
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++)
				sum += banCo[i][j];
			if (sum>1) {
				for (int k=0; k<8; k++)
					if (banCo[i][k]==1) {
						if (!oKhongHopLe.inIt(i, k)) {
							oKhongHopLe.add(i, k);
						}
					}
			}
			sum=0;
		}
		//To mau hang doc
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++)
				sum += banCo[j][i];
			if (sum>1) {
				for (int k=0; k<8; k++) {
					if (banCo[k][i]==1)
						if (!oKhongHopLe.inIt(k, i))
							oKhongHopLe.add(k, i);
				}
			}
			sum=0;
		}
		//To mau duong cheo phu
		for (int k=0; k<8; k++) {
			int i = k;
			int j = 0;
			while (i >= 0) {
				sum += banCo[i][j];
				i--; j++;
			}
			i++; //Cong du
			j--; //Cong du
			if (sum>1) {
				while (i <= k) {
					if (banCo[i][j]==1) {
						if (!oKhongHopLe.inIt(i, j)) oKhongHopLe.add(i, j);
					}
					i++; j--;
				}
			}
			sum=0;
		}
		for (int k=1; k<8; k++) {
			int i=7;
			int j=k;
			while (j <= 7) {
				sum += banCo[i][j];
				i--; j++;
			}
			i++; //Cong du
			j--; //Cong du
			if (sum>1) {
				while (j>=k) {
					if (banCo[i][j]==1){
						if (!oKhongHopLe.inIt(i, j)) oKhongHopLe.add(i, j);
					}
					i++; j--;
				}
			}
			sum=0;
		}
		//To mau duong cheo chinh
		for (int k=0; k<8; k++) {
			int i = k;
			int j = 7;
			while (i >= 0) { 
				sum += banCo[i][j];
				i--; j--;
			}
			i++; j++;
			if (sum>1) {
				while (i <=k ) {
					if (banCo[i][j]==1) {
						if (!oKhongHopLe.inIt(i, j)) oKhongHopLe.add(i, j);
					}
					i++; j++;
				}
			}
			sum=0;
		}
		for (int k=6; k>=0; k--) {
			int i=7;
			int j=k;
			while (j >= 0 ) {
				sum += banCo[i][j];
				i--; j--;
			}
			i++; j++;
			if (sum>1) {
				while (j <=k ) {
					if (banCo[i][j]==1) {
						if (!oKhongHopLe.inIt(i, j)) oKhongHopLe.add(i, j);	
					}
					i++; j++;
				}
			}
			sum=0;
		}
		//Xoa mau tat ca cac o
		for (int i=0; i<8; i++)
			for(int j=0; j<8; j++) {
				if ((i+j)%2==0) buttons[i][j].setBackground(mauTrang);
				else buttons[i][j].setBackground(mauBanCo);
			}
		if (oKhongHopLe.getTop()==0 && TongQuanHau(banCo)==8) {
			for (int i=0; i<8; i++)
				for(int j=0; j<8; j++)
					if (banCo[i][j]==1) buttons[i][j].setBackground(mauHoanThanh);
		}
		else {
			for (int i=0; i<oKhongHopLe.getTop(); i++) {
//				System.out.println("YES");
				int x = oKhongHopLe.getOCo(i).GetX();
				int y = oKhongHopLe.getOCo(i).GetY();
				if ( OHopLe(banCo, x, y) == true) {
					if ((x+y)%2==0) buttons[x][y].setBackground(mauTrang);
					else buttons[x][y].setBackground(mauBanCo);
					oKhongHopLe.remove(x, y);
					i--;
				}
				else 
					buttons[x][y].setBackground(mauDanhLoi);
			}	
		}
	}
	public boolean OHopLe(int banCo[][], int x, int y) {
		if (banCo[x][y]==0) return true;
		//Ngang
		int sum=0;
		for (int j=0; j<8; j++)
			sum += banCo[x][j];
		if (sum>1) return false;
		
		//Doc
		sum=0;
		for (int i=0; i<8; i++)
			sum += banCo[i][y];
		if (sum>1) return false;
		
		
		//Cheo phu
		sum=0;
		int i=x, j=y;
		while (i>=0 && j<=7) {
			sum += banCo[i][j];
			i--;
			j++;
		}
		i=x+1; j=y-1;
		while ( j>=0 && i<=7 ) {
			sum += banCo[i][j];
			i++;
			j--;
		}
		if (sum>1) {
			return false;
		}
		//Cheo chinh
		sum=0;
		i=x; j=y;
		while ( i>=0 && j>=0 ) {
			sum += banCo[i][j];
			i--;
			j--;
		}
		i=x+1; j=y+1;
		while ( i<=7 && j<=7 ) {
			sum += banCo[i][j];
			i++;
			j++;
		}
		if (sum>1) {
			return false;
		}
		
		return true;
	}
	public Color getColor(String name) {
		switch (name) {
		case "Green": return green;
		case "Blue" : return blue;
		default: return green;
		}
	}
	public static boolean isSafe(int banCo[][], int r, int c) {
		for (int i=0; i<8; i++)
			if (banCo[i][c]==1) return false;
		for (int i=r, j=c; i>=0 && j>=0; i--, j--) {
			if (banCo[i][j]==1) return false;
		}
		for (int i=r, j=c; i>=0 && j<8; i--, j++) {
			if (banCo[i][j]==1) return false;
		}
		return true;
	}
	public void inDapAn(int dapAn[][][]) {
		for (int k=0; k<92; k++) {
			for (int i=0; i<8; i++) {
				for (int j=0; j<8; j++) {
					System.out.print(dapAn[k][i][j]);
				}
			System.out.println("");
			}
			System.out.println("");
		}
	}
	public void nQueen(int banCo[][], int r) throws InterruptedException {
		if (stop) return;
		if (r==8) {
			for (int i=0; i<8; i++) {
				for (int j=0; j<8; j++)
					if (banCo[i][j]==1) buttons[i][j].setBackground(mauHoanThanh);
			}
			solution++;
			titleText.setText("Solution: "+solution+"");
			nextButton.setEnabled(true);
			while (!nextButton.getModel().isPressed()) {
				if (stop) break;
				Thread.sleep(1);
			}
			return;	
		}
		for (int i=0; i<8; i++) {
			if (isSafe(banCo,r,i)) {
				banCo[r][i]=1;
				if (!stop) buttons[r][i].setIcon(icon);
				Thread.sleep(timer);
								
				nQueen(banCo,r+1);
				nextButton.setEnabled(false);
				for (int i1=0; i1<8; i1++)
					for (int j=0; j<8; j++)
						if (banCo[i1][j]==1) {
							if ((i1+j)%2==0) buttons[i1][j].setBackground(mauTrang);
							else buttons[i1][j].setBackground(mauBanCo);
						}
				banCo[r][i]=0;
				buttons[r][i].setIcon(null);
				Thread.sleep(timer);
			}
			
		}
	}
	
	public void getDapAn(int banCo[][], int r) throws InterruptedException {
		if (r==8) {
			for (int i=0; i<8; i++) {
				for (int j=0; j<8; j++)
					dapAn[stt][i][j]=banCo[i][j];
			}
			stt++;
			return;	
		}
		for (int i=0; i<8; i++) {
			if (isSafe(banCo,r,i)) {
				banCo[r][i]=1;
				getDapAn(banCo,r+1);
				banCo[r][i]=0;
			}
			
		}
	}
	
	public int checkSolution(int banCo[][]) {
		int solution=0;
		boolean possible=true;
		for (int k=0; k<92; k++) { //Duyệt qua các đáp án
			possible = true;
			//Duyệt qua các quân hậu trên bàn cờ
			for (int i=0; i<dsQuanHau.size(); i++) 
				if (dapAn[k][dsQuanHau.get(i).GetX()][dsQuanHau.get(i).GetY()]==0) 
					possible=false;
			if (possible) solution++;
		}
		return solution;
	}
	
	public static void print(int bo[][]) {
		for (int i=0; i<8; i++) {
			for(int j=0; j<8; j++)
				System.out.print(bo[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	@Override
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==startButton) {
			startButton.setVisible(false);
			exitButton.setVisible(false);
//			normalMode.setVisible(true);
//			exploreMode.setVisible(true);
			optionPanel2.setVisible(true);
			card.show(contPanel, "2");
		}
		if (e.getSource()==exitButton)
			frame.dispose();
		
//		if (e.getSource()==normalMode) {
//			optionPanel2.setVisible(false);
//			card.show(contPanel, "2");
//		}
//		if (e.getSource()==exploreMode) {
//			optionPanel2.setVisible(true);
//			card.show(contPanel, "2");
//		}
			
		
		
		if (e.getSource()==showPS) {
			if (showPS.isSelected()) {
				showPS.setIcon(crown);
				psLabel.setVisible(true);
			}
			else {
				showPS.setIcon(crown0);
				psLabel.setVisible(false);
			}
		}
		
		if (e.getSource()==stopButton) stop=true;
		
		if (e.getSource()==quitButton) {
			clearBanCo(banCo);
			if (auto_solving) stop=true;
			startButton.setVisible(true);
			exitButton.setVisible(true);
//			normalMode.setVisible(false);
//			exploreMode.setVisible(false);
			card.show(contPanel, "1");
		}
		if (e.getSource()==clearButton) {
			clearBanCo(banCo);
			titleText.setText("Chọn một ô bất kỳ để bắt đầu trò chơi!");
		}
		if (e.getSource()==autoSolve) {
		  SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					clearBanCo(banCo);
					autoSolve.setEnabled(false);
					clearButton.setEnabled(false);
					quitButton.setEnabled(true);
					auto_solving=true;
					stopButton.setEnabled(true);
					dsQuanHau.clear();
					iconCbb.setEnabled(false);
					psLabel.setText(""+checkSolution(banCo));
					nQueen(banCo,0);
					clearButton.setEnabled(true);
					autoSolve.setEnabled(true);
					auto_solving=false;
					solution=0;
					stop=false;
					stopButton.setEnabled(false);
					titleText.setText("Chọn một ô bất kỳ để bắt đầu!");
					iconCbb.setEnabled(true);
					
					return null;
				}
			};
			worker.execute();
		}
		if (e.getSource()==colorCbb) {
			mauBanCo = getColor(colorCbb.getSelectedItem().toString());
			ToMau(banCo);
			clearButton.setBackground(mauBanCo);
			colorCbb.setBackground(mauBanCo);
			iconCbb.setBackground(mauBanCo);
			autoSolve.setBackground(mauBanCo);
			nextButton.setBackground(mauBanCo);
			stopButton.setBackground(mauBanCo);
			
		}
		if (e.getSource()==iconCbb) {
			switch (iconCbb.getSelectedIndex()) {
			case 0: icon = queen; break;
			case 1: icon = leo; break;
			case 2: icon = panda; break;
			//default: icon = queen;
			} 
			for (int i=0; i<dsQuanHau.size(); i++)
				buttons[dsQuanHau.get(i).GetX()][dsQuanHau.get(i).GetY()].setIcon(icon);
		}
		if (!auto_solving) {
			for (int i=0; i<8; i++)
				for (int j=0; j<8; j++) {
					if (e.getSource()==buttons[i][j]) {
						if (banCo[i][j]==1) { //Day la mot thao tac huy bo co
							banCo[i][j]=0;
							buttons[i][j].setIcon(null);
							OCo x = new OCo(i,j);
							for (int k=0; k<dsQuanHau.size();k++)
								if (dsQuanHau.get(k).isEqual(x)) {
//									System.out.println("YES");
									dsQuanHau.remove(k);
								}
							psLabel.setText(""+checkSolution(banCo));
							if (dsQuanHau.size()==0) titleText.setText("Chọn một ô bất kỳ để bắt đầu!");
						}
						else { //Day la mot thao tac danh co
							titleText.setText("Trò chơi 8 quân hậu");
							if (TongQuanHau(banCo)<8) {
								banCo[i][j]=1;
								buttons[i][j].setIcon(icon);
								dsQuanHau.add((new OCo(i,j)));
								psLabel.setText(""+checkSolution(banCo));
							}
						}
					}
					ToMau(banCo);
				}
		}
	}
}
//SOLUTION: 1-7-5-8-2-4-6-3
