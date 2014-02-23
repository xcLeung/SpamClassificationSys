package divide_word;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ICTCLAS.I3S.AC.*;
import feature_extraction.extraction;

public class DivideWord {

	private String m_FilePath="";
	private ArrayList<String> m_MailsContent=new ArrayList<String>();
	
	private Map<String, Integer> m_WordAllMap = new HashMap<String,Integer>();
	
	private static int m_WordSum=0;
	
	public DivideWord(String sFilePath){
		m_FilePath=sFilePath;
	}
	
	/***
	 * 对字符串分词(中科院分词)
	 */
	private String ICTCLASStringProcess(String sInput){	
		try {
			ICTCLAS50 oICTCLAS50 = new ICTCLAS50();
			String argu = ".";
			//初始化
			if (oICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return "";
			}
			//设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
			oICTCLAS50.ICTCLAS_SetPOSmap(2);
			//导入用户词典前分词
			byte nativeBytes[] = oICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 0);//分词处理
			System.out.println(nativeBytes.length);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("分词结果： " + nativeStr);//打印结果
			return nativeStr;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/***
	 * 获取邮件正文内容，分词处理，统计
	 */
	private void DealWords(){
		File fDictFile=new File(m_FilePath);
		File[] files=fDictFile.listFiles();
		extraction oExtraction=new extraction();
		String sContent="";
		String sResult="";
		String[] ssWord;
		for(File file:files){
			oExtraction.Release();
			sContent="";
			sResult="";
			sContent=oExtraction.GetEmailContent(file.getAbsolutePath());
			sContent=sContent.substring(0, sContent.length()/2);
			m_MailsContent.add(sContent);
			if(sContent.length()>0)
				sResult=ICTCLASStringProcess(sContent);
			ssWord=sResult.split(" ");
			if(ssWord.length>0)
				m_WordSum+=ssWord.length;
			for(int i=0;i<ssWord.length;i++){
				Integer oValue=m_WordAllMap.get(ssWord[i]);
				if(oValue!=null)
					m_WordAllMap.put(ssWord[i], oValue+1);
				else
					m_WordAllMap.put(ssWord[i], 1);
			}
		}
		
		for(String key:m_WordAllMap.keySet()){
			System.out.println(key+":"+(m_WordAllMap.get(key)*1./m_WordSum));
		}
	}
	
	/***
	 * 获取特征词
	 * @return
	 */
	public ArrayList<String> GetFeatureWord(){
		return null;
	}
	
	/**测试函数
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = "F:\\MailProject\\梁祥超-毕业设计\\emailtest3-decode";
		System.out.println("中文分词文件夹："+filePath);
		DivideWord oDivideWord=new DivideWord(filePath);
		oDivideWord.DealWords();
	}

}
