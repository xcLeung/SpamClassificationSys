package Main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.NumericShaper.Range;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.python.antlr.PythonParser.else_clause_return;
import org.python.antlr.PythonParser.return_stmt_return;
import org.python.core.PyFunction;
import org.python.util.PythonInterpreter;

import divide_word.DivideWord;


import email_decode.Decode_Mail;
import feature_extraction.extraction;

public class Main {
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final UI frame = new UI("中文垃圾邮件过滤系统", 400, 550);
        
        //解码按钮事件
        frame.btnEmailDecode.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		/*遍历文件夹*/
        		String filePath = "";
        		filePath=frame.dictionaryText.getText();
        		if(filePath.length()<=0){return ;}
        		try {
					dealFiles_decode(filePath);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1);
				}
        	}
        });
        
        //特征提取按钮事件
        frame.btnFeatureExtractionButton.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method
				String filePath = frame.txtExtraction.getText();
				if(filePath.length()<=0){return ;}
				try{
					System.out.println(filePath);
					dealFiles_feature_extraction(filePath);
				}catch (FileNotFoundException e1){
					System.out.println(e1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					return ;
				}				
			}
		});
        
        //分词处理按钮事件
        frame.btnDivideWord.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String filePath1 = frame.txtDividewordText.getText();
				String filePath2 = frame.txtDividewordText2.getText();
				if(filePath1.length()<=0 || filePath2.length()<=0){return ;}
				try {
					dealFiles_divideword(filePath1,filePath2);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
        
        //实验处理按钮事件
        frame.btnTest.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String sFilePath = frame.txtTest.getText();
				if(sFilePath.length()<=0) return ;
				dealFiles_Test(sFilePath);
			}
		});
        
        //退出按钮事件
        frame.Exitbtn.addActionListener(new ActionListener() {    
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
	}
	
	/**
	 * 邮件解码
	 * @param filePath
	 * @throws FileNotFoundException
	 */
	private static void dealFiles_decode(String filePath) throws FileNotFoundException{
		File root = new File(filePath);    //打开指定路径的文件
		if(root.exists() && root.isDirectory()){    //若是文件夹则将里面的文件解码
			PythonInterpreter interpreter = new PythonInterpreter();  
			interpreter.execfile("src\\email_decode\\decodemail.py");
			PyFunction func = (PyFunction)interpreter.get("decodebody_str",PyFunction.class);
			
			//System.out.println(filePath);
			File dict = new File(filePath+"-decode");
			dict.mkdirs();
			//System.out.println(dict.getAbsolutePath());
			File[] files = root.listFiles();
			for(File file:files){
				Decode_Mail.decodeMail(file.getAbsolutePath(),dict.getAbsolutePath(),func);
			}
		}else{
			System.out.println("文件夹不存在！");
		}
	}
	
	/**
	 * 特征提取
	 * @param filePath
	 * @throws IOException 
	 */
	private static void dealFiles_feature_extraction(String filePath) throws IOException{
		File root = new File(filePath);
		if(root.exists() && root.isDirectory()){    //若是文件夹则将里面的文件解码			
			System.out.println(root.getAbsolutePath());
			File fileRes=new File(filePath+"result.txt");
			File fileArff=new File(filePath+"result.arff");
			fileRes.delete();
			fileArff.delete();
			String featureString="";
			String[] tmp;
			if(fileRes.createNewFile()==true){
				File[] files = root.listFiles();
				tmp = new String[files.length];
				extraction myExtraction = new extraction();
				int i=0;
				for(File file:files){
					String result = myExtraction.feature_extraction(file.getAbsolutePath());
					featureString+=result+"\r\n";
					tmp[i++] = result;
				}
				//System.out.println(featureString);
				//写数据到result文件
				writeByFileWrite(fileRes.getAbsolutePath(), featureString);
			}
			else{
				System.out.println("创建特征串文件失败！");
				return ;
			}
			featureString = "\r\n@data\r\n";
			if(fileArff.createNewFile()==true){	
				int attLen = 0;
				for(String s:tmp){
					String dataString = "";
					attLen = s.length();
					for(int i=0;i<s.length();i++){
						if(i==s.length()-1)
							dataString += s.charAt(i);
						else
							dataString += (s.charAt(i) + ",");
					}
					featureString+=dataString + "\r\n";
				}
				System.out.println(featureString);
				String relationString = "@RELATION email\r\n\r\n";
				String RANGESTRING = "*0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
				String arrVal = "{";
				for(int i=0;i<RANGESTRING.length();i++){
					if (i == RANGESTRING.length()-1)
						arrVal += RANGESTRING.charAt(i);
					else {
						arrVal += RANGESTRING.charAt(i) + ",";
					}
				}
				arrVal+="}";
				String attrTmp = "";
				for(int i=0;i<attLen;i++){
					attrTmp += String.format("@ATTRIBUTE attr%s %s\r\n", i, arrVal);
				}
				writeByFileWrite(fileArff.getAbsolutePath(),relationString + attrTmp + featureString);
			}
			else{
				System.out.println("创建arff文件失败!");
				return ;
			}
		}else{
			System.out.println("文件夹不存在！");
			return ;
		}
	}
	
	/***
	 * 中文分词，特征词选择
	 * @param filePath
	 * @throws IOException 
	 */
	private static void dealFiles_divideword(String filePath1,String filePath2) throws IOException{
		File root1 = new File(filePath1);
		File root2 = new File(filePath2);
		if(root1.exists() && root1.isDirectory() && root2.exists() && root2.isDirectory()){
			File[] files={root1,root2};
			DivideWord oDivideWord=new DivideWord(files);
			oDivideWord.DealWords();
		}else{
			System.out.println("文件夹不存在！");
			return ;
		}
	}
	
	/***
	 * 训练集，测试集实验
	 * @param sFilePath
	 */
	private static void dealFiles_Test(String sFilePath){
		File root = new File(sFilePath);
		if(root.exists() && root.isDirectory()){
			
		}
	}
	
	/**
	 * 向文件写数据
	 * @param _sDestFile
	 * @param _sContent
	 * @throws IOException
	 */
	private static void writeByFileWrite(String _sDestFile, String _sContent) throws IOException {
			FileWriter fw = null;
			try {
				fw = new FileWriter(_sDestFile);
				fw.write(_sContent);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (fw != null) {
					fw.close();
					fw = null;
				}
			}
	}
}
