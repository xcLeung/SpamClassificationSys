package Main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UI extends JFrame{
	final int TEXTLENGTH=28;
	final String label="请输入文件夹路径";
	
	JLabel dictionaryLabel = new JLabel(label);
	JLabel fileLabel1=new JLabel(label);
	JLabel fileLabel2=new JLabel(label);
	
	JTextField dictionaryText = new JTextField(TEXTLENGTH);
	JTextField txtExtraction = new JTextField(TEXTLENGTH);
	JTextField txtDividewordText = new JTextField(TEXTLENGTH);
	
	JButton btnEmailDecode = new JButton("邮件解码");
	JButton btnFeatureExtractionButton = new JButton("特征提取");
	JButton btnDivideWord=new JButton("中分分词");
	JButton Exitbtn = new JButton("退出");
	
	JPanel panelNorth=new JPanel();
	JPanel panelFileSource = new JPanel();//面板
    JPanel panelExtraction = new JPanel();//面板
    JPanel panelDivideWord = new JPanel();
    JPanel panelExit = new JPanel();
    
    public UI(){}
    
    public UI(String title,int height,int width){
    	this.setSize(width,height);
    	this.setTitle(title);
    	this.setLayout(new BorderLayout());
    	this.setVisible(true);
    	
    	panelNorth.setLayout(new BorderLayout());
    	
    	panelFileSource.setLayout(new FlowLayout(FlowLayout.LEFT));
    	panelFileSource.add(dictionaryLabel);
    	panelFileSource.add(dictionaryText);
    	panelFileSource.add(btnEmailDecode);
    	panelNorth.add(panelFileSource,BorderLayout.NORTH);
    	
    	panelExtraction.setLayout(new FlowLayout(FlowLayout.LEFT)); 	
    	panelExtraction.add(fileLabel1);
    	panelExtraction.add(txtExtraction);
    	panelExtraction.add(btnFeatureExtractionButton);
    	panelNorth.add(panelExtraction,BorderLayout.CENTER);
        
    	panelDivideWord.setLayout(new FlowLayout(FlowLayout.LEFT)); 	
    	panelDivideWord.add(fileLabel2);
    	panelDivideWord.add(txtDividewordText);
    	panelDivideWord.add(btnDivideWord);
    	panelNorth.add(panelDivideWord,BorderLayout.SOUTH);
    	
        add(panelNorth,BorderLayout.NORTH);
        
        panelExit.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelExit.add(Exitbtn);
        add(panelExit, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
