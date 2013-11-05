#!/usr/bin/python
# -*- coding: utf-8 -*-

import email
import os
import sys




def decodebody(file):                        
	fp = open(file,"r")
	msg = email.message_from_file(fp)   

	for par in msg.walk():               #循环信件中的每一个mime数据块
		if not par.is_multipart():     #判断是否属于multipart，不是的话其实是邮件的注释部分，没作用
			name = par.get_param("name")    #如果有附件，将会取出附件名
			if name:                    #有附件
				try:
					h = email.Header.Header(name)
				except:
					h = email.Header.Header(name,"gbk")
				dh = email.Header.decode_header(h)
				fname = dh[0][0]
				#下面这行是QP或base64解码
				data = par.get_payload(decode=True)  #解码出附件数据，存到文件中
			
				try :
					f = open(fname, 'wb')
				except:
					f = open('aaa', 'wb')
				f.write(data)
				f.close()
			else:
				print par.get_payload(decode=True)   #不是附件，直接解码输出文本内容
			#print '+' * 60


def decodeheader(file):          
	fp = open(file,"r")          
	msg = email.message_from_file(fp)  

	subject = msg.get("subject")   #取主题

	#下面是调用python的email模块解码，实质是对MIME数据块解码，信体类似的
	try:
		head = email.Header.Header(subject)   #加上gbk或者utf-8参数可以让系统转码的时候适应中文
	except:
		head = email.Header.Header(subject,"gbk")
	dh = email.Header.decode_header(head)
	subject = dh[0][0]

	#下面的打印内容可按需求调整
	print "subject:" , subject
	print "from:", email.utils.parseaddr(msg.get("from"))[1] 
	try:
		to = msg.get("to").split(',')
		print "to:",
		for i in to:
			print email.utils.parseaddr(i)[1],
		print ""
	except:
		print "to:", email.utils.parseaddr(msg.get("to"))[1]
	print "date:",msg.get("date")

	fp.close()

	
def decodebody_str(source,object):
	fp = open(source,"r")
	output = open(object,"a")
	mail = email.message_from_file(fp)  
	
	for par in mail.walk():               #循环信件中的每一个mime数据块
		if not par.is_multipart():     #判断是否属于multipart，不是的话其实是邮件的注释部分，没作用
			res = ''
			fname = None
			name = par.get_param("name")    #如果有附件，将会取出附件名			
			if name:
				print name
				#print par.get_content_type()
				charset =  par.get_param("charset")
				#print charset
				try:
					h = email.Header.Header(name,)
				except:
					h = email.Header.Header(name,"gbk")
				print h
				dh = email.Header.decode_header(h)
				print dh
				fname = dh[0][0]
				print fname
			else:			
				output.write(par.get_payload(decode=True) + "\n\n")
	if fname==None:
		output.write("\nNo Attachment")
	else:
		output.write("Attachment:" + fname)
	
	fp.close()
	output.close()
	
	#return res
		#	if name:                    #有附件
		#		try:
		#			h = email.Header.Header(name)
		#		except:
		#			h = email.Header.Header(name,"utf-8")
		#		dh = email.Header.decode_header(h) 
		#		fname = dh[0][0]
		#		print fname
			#else:
				#res = res + par.get_payload(decode=True)		
	#return res
	
#if __name__ == "__main__":
	#file="google.eml"
	#file = raw_input()
#	reload(sys)
#	sys.setdefaultencoding('utf-8')
#	file = sys.argv[1]
	#print file
#	if file:
#		decodeheader(file)
#		decodebody(file)
