package email_decode;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;

public class Decode_UI extends JFrame{
	JLabel dictionaryLabel = new JLabel("请输入文件夹路径");
	JTextField dictionaryText = new JTextField();
	JButton OKbtn = new JButton("确定");
	JButton Exitbtn = new JButton("退出");
	JPanel jp = new JPanel();//面板
    JPanel jp1 = new JPanel();//面板
    
    public Decode_UI(){}
    
    public Decode_UI(String title,int height,int width){
    	this.setSize(width,height);
    	this.setTitle(title);
    	
    	jp.setLayout(new BorderLayout());
        jp.add(dictionaryLabel, BorderLayout.WEST);
        jp.add(dictionaryText, BorderLayout.CENTER);
        jp.add(OKbtn, BorderLayout.EAST);
        add(jp,BorderLayout.NORTH);
        
        jp1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jp1.add(Exitbtn);
        add(jp1, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
