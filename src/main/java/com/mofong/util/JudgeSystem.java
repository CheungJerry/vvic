package com.mofong.util;

/**
 * @author mofong
 * get system from static object and function
 *
 */
public class JudgeSystem {

	public static int UN_JUDGE_SYSTEM = 0;
	public static int LINUX = 1;
	public static int WINDOWS = 2;

	private static int SYSTEMCODE;

	public static int getSystemCode() {
		return SYSTEMCODE;
	}

	private final static void setSystemCode(int systemCode) {
		SYSTEMCODE = systemCode;
	}

	static {
		String OS = System.getProperty("os.name").toLowerCase();
		SYSTEMCODE = OS.indexOf("linux") >= 0 ? LINUX : OS.indexOf("windows") >= 0 ? WINDOWS : 0;
	}

}
