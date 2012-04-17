/**
 * 
 */
package org.thripse.editors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.thripse.console.ConsoleLogger;

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
			ConsoleLogger.log(e);
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

	public List<String> parseGenerator() {
		List<String> generators = new ArrayList<String>();
		try {
			ProcessBuilder builder = new ProcessBuilder(compiler, "--help");
			builder.redirectErrorStream(true);
			Process proc = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			boolean begin = false;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Available generators")) {
					begin = true;
					continue;
				}
				if (begin && line.startsWith("  ") && !line.startsWith("    ") && !(line = line.trim()).isEmpty()) {
					String g = line.substring(0, line.length() - 1);
					generators.add(g);
				}
			}
			proc.waitFor();
		} catch (Exception e) {
			ConsoleLogger.log(e);
		}
		return generators;
	}

	public int compile(String thriftFile) {
		// command
		ConsoleLogger.print("compile:");
		int index = parameters.size() * 2 + 2;
		String[] cmd = new String[index];
		index = 0;
		cmd[index++] = compiler;
		ConsoleLogger.print(" " + compiler);
		for (String key : parameters.keySet()) {
			cmd[index++] = key;
			ConsoleLogger.print(" " + key);
			String value = parameters.get(key);
			cmd[index++] = value;
			ConsoleLogger.print(" " + value);
		}
		cmd[index] = thriftFile;
		ConsoleLogger.println(" " + thriftFile);
		// compile
		int result = 1;
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			Process proc = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				ConsoleLogger.println(line);
			}
			result = proc.waitFor();
		} catch (Exception e) {
			ConsoleLogger.log(e);
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThriftCompiler compiler = new ThriftCompiler("D:\\topic\\thrift\\thrift-0.7.0.exe");
		List<String> list = compiler.parseGenerator();
		for (String gen : list) {
			System.out.println(gen);
		}
	}

}
