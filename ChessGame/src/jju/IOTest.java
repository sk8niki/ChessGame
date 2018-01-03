package jju;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.security.auth.login.CredentialException;


//文件IO的使用案例
public class IOTest {
	public static void main(String[] args) 
	{
		//1.在磁盘的某个目录下，创建一个文件
			//1.1定义一个Path对象
		Path f1=Paths.get("E:\\1.txt");
		
		//1.2创建文件
		//OutputStream os=Files.newOutputStream(f1,StandardOpenOption.CREATE);
		try {
			//OutputStream os=Files.newOutputStream(f1, StandardOpenOption.CREATE);
			OutputStream os=Files.newOutputStream(f1, StandardOpenOption.APPEND);
			os.write('A');
			os.write('D');
			os.write('C');
		
			
			//利用字符流，进行数据的写入
			//--PrintWrite
			PrintWriter pw=new PrintWriter(os);
							pw.print("傻逼");	
							pw.println("徐文鑫");
			
							pw.flush();//强制立即写入;
							pw.close();
							os.close();
			//利用字符流,读取文件中的内容
			BufferedReader br=
					Files.newBufferedReader(
							f1, Charset.defaultCharset());
			while(true)
			{
			String s=br.readLine();
				if(s==null)
					break;
			System.out.println(s);
			}
			//InputStream in=Files.newOutputStream(f1,StandardOpenOption.CREATE);
			byte byt[]=new byte[100];
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		File file=new File("E:\\2.txt");
		
		if(file.exists())
		{
			String name=file.getName();
			long length=file.length();
			System.out.println("文件名称为:"+name);
			System.out.println("文件长度是:"+length);
		}
		else
		{
			try {
				file.createNewFile();
			} catch (Exception e) {
				// TODO: handle exception
			}
			System.out.println("该文件已创建!");
		}
	}

}
