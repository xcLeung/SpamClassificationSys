package feature_extraction;
import net.sourceforge.pinyin4j.*;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.* ;

public class CheckFeature {
	
	public String  getHanyutoPinyin(String  s )   //获得汉字换换后的拼音
	{
		HanyutoPinyin pinyin = new HanyutoPinyin();
		String strPinyin = pinyin.getStringPinYin(s);
		return strPinyin;
	}
	
	public String getFtToJt(String s) throws IOException  //获得繁体转换后的简体
	{
		FtToJt fttojt = new FtToJt();
		return fttojt.simplized(s);
	}

}




class HanyutoPinyin //汉语转拼音
{
		private HanyuPinyinOutputFormat format = null;
        private String[] pinyin;
        public HanyutoPinyin()
         {
                   format = new HanyuPinyinOutputFormat();                  
                   //WITH_TONE_NUMBER是输出拼音的音调 
                  	//WITHOUT_TONE 是没有输出音调

                  // format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
                   format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);                
                   pinyin = null;
         }
         //转换单个字符
         public String getCharacterPinYin(char c)
         {
               try
                {
                            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
             }
                  catch(BadHanyuPinyinOutputFormatCombination e)

                   {

                            e.printStackTrace();

                   }
                   // 如果c不是汉字，toHanyuPinyinStringArray会返回null

                   if(pinyin == null) return null;
                   // 只取一个发音，如果是多音字，仅取第一个发音
                   return pinyin[0];   
         }
      //转换一个字符串
         public String getStringPinYin(String str)

         {
                   StringBuilder sb = new StringBuilder();
                   String tempPinyin = null;
                   for(int i = 0; i < str.length(); ++i)

                   {
                            tempPinyin =getCharacterPinYin(str.charAt(i));
                            if(tempPinyin == null)

                            {
                                     // 如果str.charAt(i)非汉字，则保持原样
                                     sb.append(str.charAt(i));
                            }
                            else
                            {
                                     sb.append(tempPinyin);
                            }
                   }

                   return sb.toString();
         }

}


 class FtToJt   		//繁体简体转换
 {
	String jtPy = "";
	String ftPy ="";
       
	FtToJt() throws IOException
	{
		FileInputStream fis = new FileInputStream("./src/txt/jtchar.txt");   //打开简体库并将内容赋值字符串jtPy
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader brjt = new BufferedReader(isr);
		String str = brjt.readLine();
		while(str!= null)
		{	
			jtPy=jtPy+str;
			str = brjt.readLine();
		}
		brjt.close();
	//	System.out.println(jtPy);
		
		FileInputStream fisft = new FileInputStream("./src/txt/ftchar.txt");   //打开繁体库并将内容赋值字符串jtPy
		InputStreamReader isrft = new InputStreamReader(fisft);
		BufferedReader brft = new BufferedReader(isrft);
		String strft = brft.readLine();
	
		while(strft!= null)
		{
			ftPy=ftPy+strft;
			strft = brft.readLine();
		}
		brft.close();
	//	System.out.println(ftPy);
		
	}
	 
    String simplized(String st) {   //繁体转简体
        String stReturn = "";   
        for (int i = 0; i < st.length(); i++) {   
            char temp = st.charAt(i);   
            if (ftPy.indexOf(temp) != -1)   
                stReturn += jtPy.charAt(ftPy.indexOf(temp));   
            else   
                stReturn += temp;   
        }   
        return stReturn;   
    }   
}

