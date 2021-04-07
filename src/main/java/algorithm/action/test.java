package algorithm.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
public class test {
	static int c = 0;
	static int a = 0;
	static int b = 0;
	public static void eval(String javaStr) throws Exception {
        StringBuffer str = new StringBuffer();
        str.append("public class Eval {").append("public static void main(String[] args) {").append(javaStr).append("}}");
        OutputStream out = new FileOutputStream("Eval.java");
        out.write(str.toString().getBytes("gbk"));
        out.close();

        // 编译时  错误信息
        Process javacProcess = Runtime.getRuntime().exec("javac Eval.java");
        InputStream compileError = javacProcess.getErrorStream();
        System.err.println(read(compileError));
        compileError.close();

        Process javaProcess = Runtime.getRuntime().exec("java Eval");
        //运行时   错误信息
        InputStream err = javaProcess.getErrorStream();
        System.err.println(read(err));
        err.close();

        //运行时   正常信息
        InputStream success = javaProcess.getInputStream();
        System.out.println(read(success));
        success.close();

        //删除生成文件
        new File("Eval.java").delete();
        new File("Eval.class").delete();
    }

    private static String read(InputStream in) throws IOException {
        byte[] b = new byte[1024];
        int len = -1;
        StringBuffer str = new StringBuffer();
        while ((len = in.read(b)) != -1) {
            str.append(new String(b, 0, len, "gbk"));
        }
        return str.toString();
    }
    
	public static void main(String[] args) throws Exception{

	}
}
