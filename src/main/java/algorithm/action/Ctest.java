package algorithm.action;

import java.io.IOException;

public class Ctest {
	public static void main(String[] args) {
		try {
			// String[] cmds = {"cmd.exe","/c"," dir","c:",">","d://aa.txt"};
			// Process pro = Runtime.getRuntime().exec(cmds);
			Process pro = Runtime.getRuntime().exec("D:\\Notepad++\\notepad++.exe");
			} catch (IOException exception) {
			}
	}
}
