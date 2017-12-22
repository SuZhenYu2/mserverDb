package com.mserver.db.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.easyuitools.util.StringUtils;

 
public class GenerateUdMapperTool {
	public static void main(String[] args) throws Exception {  
	    // 项目中jar包所在物理路径  
		//D:/Dev/Repository/com/lenovo/btcp/btcpDbSupport/btcpDbSupport/0.0.1/btcpDbSupport-0.0.1.jar
	    String jarName = "D:/logs/maven-repo/Repository/com/mserver/db/mserverDbSupport/0.1.9-product/mserverDbSupport-0.1.9-product.jar";  
	    @SuppressWarnings("resource")
		JarFile jarFile = new JarFile(jarName);  
	    Enumeration<JarEntry> entrys = jarFile.entries();  
	    while (entrys.hasMoreElements()) {  
	        JarEntry jarEntry = entrys.nextElement(); 
	        if(StringUtils.endsWith(jarEntry.getName(), "Mapper.class")){
	        	generatorJava(jarEntry);
	        	//System.out.println(jarEntry.getName());  
	        }else if(StringUtils.endsWith(jarEntry.getName(), ".xml")){
	        	//generatorXml(jarEntry);
	        	//System.out.println(jarEntry.getName());  
	        }
	    }                 
	}
	private static void generatorJava(JarEntry jarEntry){
//    	System.out.println(jarEntry.getName());  
    	
    	//src/main/java
    	URL url =GenerateUdMapperTool.class.getClassLoader().getResource("");
    	
    	
    	
    	String parentUrl =jarEntry.getName().replace(".class", "");
    	String[] parentUrls =parentUrl.split("/");
    	String parentName =parentUrls[parentUrls.length-1];
    	parentUrl= parentUrl.replaceAll("/", ".");
     
    	String packageStr =parentUrl.replace("atomic", "ud").replace("."+parentName, "");
    	
    	String template ="package "+packageStr+";\n"+
						"\n"+
						"import com.easyuitools.common.annotation.MyBatisDao;\n"+
						"\n"+
						"import "+parentUrl+";\n"+
						"/**\n"+
						" * 用户自定义接口  继承自 mybatis自动生成工具生成的接口\n"+
						" * 使用该接口，不建议再直接使用自动生成的接口\n"+
						" * 每个生成的接口类都默认添加了 @MyBatisDao 注解 \n"+
						" * 在扫描mapper scan时可以添加 指定类型的接口进行扫描注册\n"+
						" * 使用该接口，不建议再直接使用自动生成的接口\n"+
						" * @author suzy2\n"+
						" * \n"+
						" */\n"+
						"@MyBatisDao\n"+
						"public interface "+parentName.replace("Mapper", "UDMapper")+" extends "+parentName+" {\n"+
						"\n"+
						"}";
    	
    	String fileName = url.getPath()+jarEntry.getName().replace("Mapper.class", "UDMapper.java").replace("atomic", "ud");
    	
    	
    	File file= new File(fileName);
    	if(file.exists()){
    		System.out.println(fileName+":已经存在不在生成");
    	}else{
    		createFile(fileName,template);
    		generatorXml(fileName,packageStr+"."+parentName.replace("Mapper", "UDMapper"));
    	}
//    	System.out.println(fileName);  
	}
	private static void generatorXml(String fileName, String nameSpace) {
		fileName =fileName.replace(".java", ".xml").replace("com/", "mapper/com/");
		String template =   "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"+
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n"+
				"<mapper namespace=\""+nameSpace+"\" >\n"+
				" <!--用户自定义的接口实现demo  -->\n"+
				"<!-- <select id=\"selectByCondition\" resultMap=\"bean1\" parameterType=\"bean2\" >\n"+
				"    select  *  from demo where conditions\n"+
				"</select> "
				+ "Base_Column_List\r\n" + 
				"BaseResultMap\r\n" + 
				"ResultMapWithBLOBs\r\n" + 
				"Blob_Column_List"
				+ "-->\n"+
				" <!--用户自定义的接口请在此处添加  -->\n"+
				"</mapper>";
		createFile(fileName,template);
		
		
	}
	
	/**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName,String filecontent){
        Boolean bool = false;
        File file = new File(fileName);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
            	if(!file.getParentFile().exists()){
            		file.getParentFile().mkdirs();
            	}
                file.createNewFile();
                bool = true;
//                System.out.println("success create file,the file is "+filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(fileName, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;
    }
    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            
            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);
            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
}
