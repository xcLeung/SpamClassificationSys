package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.python.antlr.PythonParser.return_stmt_return;
import org.python.core.PyFunction;
import org.python.util.PythonInterpreter;

import email_decode.Decode_Mail;
import email_decode.Decode_UI;
import feature_extraction.extraction;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final UI frame = new UI("中文垃圾邮件过滤系统", 400, 550);
        frame.setVisible(true);
        
        //解码按钮事件
        frame.btnEmailDecode.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		/*遍历文件夹*/
        		String filePath = frame.dictionaryText.getText();
        		if(filePath==""){return ;}
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
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method
				String filePath = frame.dictionaryText.getText()+"-decode";
				if(filePath==""){return ;}
				try{
					//System.out.println(filePath);
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
			fileRes.delete();
			if(fileRes.createNewFile()==true){
				File[] files = root.listFiles();
				String featureString="";
				extraction myExtraction = new extraction();
				for(File file:files){
					featureString+=myExtraction.feature_extraction(file.getAbsolutePath())+"\r\n";
				}
				//写数据到result文件
				writeByFileWrite(fileRes.getAbsolutePath(), featureString);
			}
			else{
				System.out.println("创建文件失败！");
				return ;
			}
		}else{
			System.out.println("文件夹不存在！");
			return ;
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
