package feature_extraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class extraction {
	
	private ArrayList<String> m_Content=new ArrayList<String>();
	
	private void GetFileContent(String filePath){
		File resfile = new File(filePath);
		String res="";
		if(resfile.exists()){
			try {
				Scanner input = new Scanner(resfile);
				while(input.hasNextLine()){
					String str=input.nextLine();
					m_Content.add(str);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else
			return ;
	}
	
	public String feature_extraction(String filePath){
		GetFileContent(filePath);
		System.out.println("正文内容：");
		for(String str:m_Content){
			System.out.println(str);
		}
		return  "lxc";
	}
	
	/***
	 * 功能测试函数
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {	
		String filePath = "F:\\MailProject\\梁祥超-毕业设计\\emailtest1-decode\\errorhead1.eml.txt";
		System.out.println("特征提取文件："+filePath);
		extraction myExtraction=new extraction();
		String testString=myExtraction.feature_extraction(filePath);
	}
}
