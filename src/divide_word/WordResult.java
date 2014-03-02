package divide_word;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.python.antlr.PythonParser.return_stmt_return;

import ICTCLAS.I3S.AC.ICTCLAS50;

import feature_extraction.extraction;

public class WordResult {
	
	private File m_FileDict;
	private File[] m_Files;
	private int m_FilesSum=0;
	
	private Map<String, Integer> m_WordAllMap = new HashMap<String,Integer>();
	
	private int m_ClassIndex=-1;
	private ArrayList<String> m_Words=new ArrayList<String>();
	
	public WordResult(){
		
	}
	
	/***
	 * 获得本类别文档总数
	 * @return
	 */
	public int GetFilesSum(){
		return m_FilesSum;
	}
	
	public void SetFileDict(File file){
		m_FileDict=file;
		m_Files=m_FileDict.listFiles();
		m_FilesSum=m_Files.length;
	}
	
	public void SetFilesWord(){
		extraction oExtraction=new extraction();
		String sContent="";
		String sResult="";
		String[] ssWord;
		for(File file:m_Files){
			oExtraction.Release();
			sContent=oExtraction.GetEmailContent(file.getAbsolutePath());
			sContent=sContent.substring(0, sContent.length()/2);
			if(sContent.length()>0)
				sResult=ICTCLASStringProcess(sContent);
			ssWord=sResult.split(" ");
			for(int i=0;i<ssWord.length;i++){
				Integer oValue=m_WordAllMap.get(ssWord[i]);
				if(oValue!=null)
					m_WordAllMap.put(ssWord[i], oValue+1);
				else
					m_WordAllMap.put(ssWord[i], 1);
			}
		}	
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
			//System.out.println("分词结果： " + nativeStr);//打印结果
			return nativeStr;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
