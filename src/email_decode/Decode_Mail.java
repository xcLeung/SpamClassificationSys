package email_decode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.python.util.PythonInterpreter;
import org.python.util.install.*;  
import org.python.antlr.PythonParser.return_stmt_return;
import org.python.core.*;
import org.python.core.util.*;

public class Decode_Mail {
	
	protected static PythonInterpreter interpreter;
	
	/**
	 * 邮件头解码	
	 * @param filePath
	 * @param Path
	 */
	private static void decodeHeader(String filePath,String Path){
		File file = new File(filePath);				
		String strSubject = "Subject:";
		String strDate = "Date:";
		String strFrom ="From:";
		String strTo="To:";
		String charset = "";
		String encodeType="";
		String str;
		
		if(file.exists()){
			try{
				Scanner input = new Scanner(file);
				//str=input.nextLine();
				if(input.hasNext()){
				str=input.next();
				while(input.hasNext()){
				//	System.out.println(str);
					if(str.startsWith("charset=")){      //字符集
						str = str.replace("charset=", "");
						charset=str.replace("\"", "");
					}else if(str.equalsIgnoreCase("Content-Transfer-Encoding:")){   
						str=input.next();
						if("base64".equals(str) || "quoted-printable".equals(str)){
							encodeType=str;
						}
					}else if(strSubject.equalsIgnoreCase(str)){   //提取主题
						str=input.next();
						int i=1;
						while(str.startsWith("=?")){
							if(i==1){
								i++;
								strSubject = str;
							}
							else{
								strSubject+=" "+str;							
							}
							str=input.next();
						}
						continue;
					}else if(strDate.equalsIgnoreCase(str)){   //提取日期
						String[] date = new String[6];
						for(int i=0;i<6;i++){
							date[i]=input.next();
						}
						date[0] = date[0].replace(",", "");
						strDate = date[3] + "-"+date[2]+"-"+date[1]+" CST "+date[0]+" "+date[4];
						System.out.println(strDate);						
					}else if(strFrom.equalsIgnoreCase(str)){   //提取From
						str=input.next();
						if (str.startsWith("=?")) {							
                            strFrom = str;
                            str = input.next();
                            if (str.startsWith("<")) {
                                strFrom += " " + str;
                                str=input.next();
                            }
						}else {
							str=str.replace("\"", "");
							strFrom = str;
							str=input.next();
						/*	while (true) {
                                if (str.startsWith("<")) {
                                    strFrom += " " + str;
                                    break;
                                }
                                strFrom += " " + str;   //todo
                                str = input.next();
                            }
                         */
						}
						System.out.println(strFrom);
						continue;
					}else if(strTo.equalsIgnoreCase(str)){  //提取To
						str=input.next();
						str=str.replace("\"","");
						int i=1;			
						strTo=str;
						while(str.startsWith("=?")){   //有名字
							if(i==1){
								strTo=str;
								i++;
							}else{
								strTo+=str;
							}
							str=input.next();
							if(str.startsWith("<")){
								strTo+=" "+str;
							}
							str=input.next();
							str=str.replace("\"", "");
						}
						while(str.endsWith(",")){  //无名字
							if(i==1){
								strTo=str;
								i++;
							}else{
								strTo+=" "+str;
							}
							str=input.next();
							str=str.replace("\"", "");
						}
						System.out.println(strTo);
						continue;
					}
					
					str=input.next();
				}
				}
				
			/**********************头解码完毕，写文件*****************************/	
				File outputfile = new File(Path+"\\"+file.getName()+".txt");
				PrintWriter output = new PrintWriter(outputfile);
				output.println("源邮件："+file.getAbsolutePath());
				output.println("解码邮件："+file.getAbsolutePath()+".txt\r\n\r\n");
				
				
				if(strSubject.startsWith("=?")){
					if(strSubject.indexOf("=?x-unknown?")>=0){
						strSubject = strSubject.replaceAll("x-unknown","utf-8");
					}
					strSubject=decodeText(strSubject);				
				}else if(strSubject.equalsIgnoreCase("Subject:")){
					strSubject="无";
				}
				output.println("主题："+strSubject);
				
				
				System.out.println(strFrom);
				if(strFrom.startsWith("=?")){
					if(strFrom.indexOf("=?x-unknown?")>=0){
						strFrom = strFrom.replaceAll("x-unknown", "utf-8");
					}
					strFrom=decodeText(strFrom);
					InternetAddress ia = new InternetAddress(strFrom);
					output.println("发件人："+ia.getPersonal()+" "+ia.getAddress());
				}else if(strFrom.equalsIgnoreCase("From:")){
					strFrom="无";
					output.println("发件人："+strFrom);
				}else{
					output.println("发件人："+strFrom);
				}
			
					
				System.out.println(strTo);
				String[] Tos = strTo.split(",");
				if(!strTo.startsWith("To:")){				
					strTo="";
					for(String var:Tos){
						if(strTo.startsWith("=?")){
							if(var.indexOf("=?x-unknown")>=0){
								var = var.replaceAll("x-unknown", "utf-8");
							}
							var=decodeText(var);
						}
						strTo+=var+"\r\n";
					}
				}else{
					strTo="无";
				}
				output.println("收件人：\r\n"+strTo);
				
				if (strDate.length() > 10) {
                    output.println("发送日期："+strDate + "\r\n");
                }
				
				input.close();
				output.close();
				
			}catch(Exception ex){
				System.out.println(ex);
			}
		}else{
			System.out.println("文件不存在！");
		}
	}
	
	/**
	 * 对邮件正文解码
	 * @param filePath  
	 * @param func      
	 * @param Path
	 * @throws FileNotFoundException
	 */
	private static void decodeBody(String filePath,PyFunction func,String Path) throws FileNotFoundException{
		File file = new File(filePath);				
		File outputfile = new File(Path+"\\"+file.getName()+".txt");			 
		PyObject ans = func.__call__(new PyString(filePath),new PyString(outputfile.getAbsolutePath()));	//调用python文件进行解码
	}
	
	
	/**
	 * 邮件解码
	 * @param filePath  eml邮件的绝对路径
	 * @param Path      eml邮件解码后文件夹的绝对路径
	 * @param func      decodemail.py文件里的decode_str方法
	 * @throws FileNotFoundException
	 */
	public static void decodeMail(String filePath,String Path,PyFunction func) throws FileNotFoundException{
		decodeHeader(filePath,Path);
		decodeBody(filePath,func,Path);
	}
	
	
	/**
	 * 根据对应的字符编码对字符串进行解码
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String decodeText(String text)
            throws UnsupportedEncodingException {
        if (text.startsWith("=?")) {
            return MimeUtility.decodeText(text);
        } else {
            return new String(text.getBytes("ISO8859_1"));
        }
    }
	
	public static void main(String[] args) throws FileNotFoundException {
		interpreter = new PythonInterpreter();  
		interpreter.execfile("F:\\MailProject\\lxc\\decodemail.py");
		PyFunction func = (PyFunction)interpreter.get("decodebody_str",PyFunction.class);
		
		String filePath = "F:\\MailProject\\lxc\\emailtest1\\errorhead3.eml";
		File file = new File(filePath);	
		if(file.exists()){
			String path=file.getParent();
			File dict = new File(path+"-decode");
			dict.mkdirs();
			path=dict.getAbsolutePath();
			
			decodeHeader(filePath,path);
			decodeBody(filePath,func,path);
		}else{
			System.out.println("文件不存在！");
		}
	}
}
