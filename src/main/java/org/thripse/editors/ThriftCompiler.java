/**
 * 
 */
package org.thripse.editors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @since 2012-4-17
 * @author gmz
 * 
 */
public class ThriftCompiler {

	private String compiler;
	private HashMap<String, String> parameters = new HashMap<String, String>();

	public ThriftCompiler(String compiler) {
		this.compiler = compiler;
	}

	public String parseVersion() {
		String line = null;
		try {
			ProcessBuilder builder = new ProcessBuilder(compiler, "-version");
			builder.redirectErrorStream(true);
			Process proc = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			line = reader.readLine();
			proc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] version = (line != null ? line.split(" ") : new String[0]);
		return version.length == 3 ? version[2] : "";
	}

	public String parseOutParameter(String version) {
		return "0.7.0".compareTo(version) > 0 ? "-o" : "-out";
	}

	public ThriftCompiler setSTR(String language) {
		parameters.put("--gen", language);
		return this;
	}

	public ThriftCompiler setOut(String outpath) {
		String out = this.parseOutParameter(this.parseVersion());
		parameters.put(out, outpath);
		return this;
	}

	public int compile(String thriftFile) {
		// command
		System.out.print("compile:");
		int index = parameters.size() * 2 + 2;
		String[] cmd = new String[index];
		index = 0;
		cmd[index++] = compiler;
		System.out.print(" " + compiler);
		for (String key : parameters.keySet()) {
			cmd[index++] = key;
			System.out.print(" " + key);
			String value = parameters.get(key);
			cmd[index++] = value;
			System.out.print(" " + value);
		}
		cmd[index] = thriftFile;
		System.out.println(" " + thriftFile);
		// compile
		int result = 1;
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			Process proc = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			result = proc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
