package Test;

import java.io.File;

import jxl.Workbook;
import jxl.write.*;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Bayes {

	public Bayes(){}
	 
	/***
	 * 执行贝叶斯算法
	 * @param sPath
	 */
	public void Run(String sPath,String sPath2){
		Instances instancesTrain = null;
        Instances instancesTest = null;
        try {
            /*
             * 1.读入训练
             * 在此我们将训练样本和测试样本是由weka提供的segment数据集构成的
             */

            File inputFile = new File(sPath);   //训练语料文件
            ArffLoader atf = new ArffLoader();
            atf.setFile(inputFile);

            instancesTrain = atf.getDataSet();

            //在使用样本之前一定要首先设置instances的classIndex，否则在使用instances对象是会抛出异常
            instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);

            inputFile = new File(sPath2); // 测试语料文件
            atf.setFile(inputFile);
            instancesTest = atf.getDataSet();    //读入测试文件
            //在使用样本之前一定要首先设置instances的classIndex，否则在使用instances对象是会抛出异常

            instancesTest.setClassIndex(instancesTest.numAttributes() - 1);                                                                           //设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数

            double sum = instancesTest.numInstances(), right = 0.0f;           //测试语料实例数

            System.out.println("测试总数量" + sum);

            /*
             * 3.根据分类算法训练并且测试每个样本
             */

            //创建文件

            WritableWorkbook book = Workbook.createWorkbook(new File(GetFilePath(sPath) +  "StatisResult.xls"));

            //天生名为“第一页”的工作表，参数0表示这是第一页

            WritableSheet sheet = book.createSheet("第一页", 0);

            //在Label对象的构造子中指名单元格位置是第一列第一行(0,0) 以及单元格内容为是否垃圾邮件

            Label label = new Label(0, 0, "邮件");

            //将定义好的单元格添加到工作表中

            sheet.addCell(label);


            //添加分类算法名
            label = new Label(1, 0, "Bayes");
            sheet.addCell(label);
            label = new Label(2, 0, "本身是不是垃圾邮件");
            sheet.addCell(label);
            right = 0;

            //Bayes的测试样例

            right = 0;
            NaiveBayes bayes = new NaiveBayes();

            bayes.buildClassifier(instancesTrain);  // 训练分类器

              for (int i = 0; i < sum; i++) {

            	label = new Label(0,i+1,String.format("邮件%s", i+1));
            	sheet.addCell(label);
            	  
                if (bayes.classifyInstance(instancesTest.instance(i)) == 1.0) {
                    label = new Label(1, i + 1, "YES");
                    sheet.addCell(label);
                } else {
                    label = new Label(1, i + 1, "NO");
                    sheet.addCell(label);
                }
                if (bayes.classifyInstance(instancesTest.instance(i)) == instancesTest.instance(i).classValue()) //如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
                {
                    right++;
                }

            }
            jxl.write.Number number = new jxl.write.Number(1, (int)sum + 1, (right / sum));
            sheet.addCell(number);



             for (int i = 0; i < sum; i++) {

                if (instancesTest.instance(i).classValue() == 1.0) {
                    label = new Label(2, i + 1, "YES");
                    sheet.addCell(label);
                } else {
                    label = new Label(2, i + 1, "NO");
                    sheet.addCell(label);
                }

            }
            number = new jxl.write.Number(2, (int)sum + 1, 1);
            sheet.addCell(number);

            book.write();
            book.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private String GetFilePath(String path){
		int pos = path.lastIndexOf("\\");
		if(pos!=-1){
			String res = path.substring(0,pos+1);
			return res;
		}
		return path;
	}
	
}
