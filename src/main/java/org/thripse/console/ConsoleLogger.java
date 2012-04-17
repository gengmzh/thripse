/**
 * 
 */
package org.thripse.console;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @since 2012-4-17
 * @author gmz
 * 
 */
public class ConsoleLogger {

	public static final String CONSOLE = "_thrift_console_";

	public static MessageConsole getConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = manager.getConsoles();
		for (IConsole console : consoles) {
			if (CONSOLE.equals(console.getName())) {
				return (MessageConsole) console;
			}
		}
		MessageConsole console = new MessageConsole(CONSOLE, null);
		manager.addConsoles(new IConsole[] { console });
		return console;
	}

	public static MessageConsoleStream getConsoleStream() {
		MessageConsole console = getConsole();
		MessageConsoleStream out = new MessageConsoleStream(console);
		out.setEncoding("UTF-8");
		return out;
	}

	public static void print(String message) {
		getConsoleStream().print(message);
	}

	public static void println(String message) {
		getConsoleStream().println(message);
	}

	public static void log(Exception e) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(out);
		e.printStackTrace(stream);
		getConsoleStream().println(out.toString());
		stream.close();
	}

}
