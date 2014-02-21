# -*- coding: utf-8 -*-
import urllib
import urllib2
import sgmllib
import json
	
class CDivideWord():
	def __init__(self):
		self.m_SinaServer="http://lxcdivideword.sinaapp.com?msg=lxc"
		self.m_Context="""
		"""

	def LoadServer(self,sUrl):
		self.m_SinaServer=sUrl

	def SetContext(self,sContext):
		self.m_Context=sContext
		self.m_PayLoad=urllib.urlencode([('context', sContext),])

	def Fetch(self):
		self.m_WebObject=urllib2.urlopen(self.m_SinaServer)
		sData=self.m_WebObject.read()
		self.m_WordData=json.loads(sData)

	def GetHtmlDom(self):
		return self.m_WordData

if __name__=="__main__":
	MyItem=CDivideWord()
	MyItem.Fetch()
	jData=MyItem.GetHtmlDom()
	for item in jData:
		print item["word"]
