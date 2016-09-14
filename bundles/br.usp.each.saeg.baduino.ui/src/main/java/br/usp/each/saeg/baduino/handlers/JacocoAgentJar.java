package br.usp.each.saeg.baduino.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.jacoco.agent.AgentJar;

public class JacocoAgentJar {

	private static final char BLANK = ' ';
	private static final char QUOTE = '"';
	private static final char SLASH = '\\';
	
	public String getVmArguments(String includes) throws CoreException {
		URL agentfileurl = null;
		try {
			agentfileurl = FileLocator.toFileURL(AgentJar.getResource());
		} catch (IOException e) {
			throw new CoreException(null);
		}
		
	    File jacocoJar = new Path(agentfileurl.getPath()).toFile();
		return String.format("-javaagent:%s=output=%s,includes=%s,dataflow=%s", jacocoJar, "tcpserver", includes,true);
	}
	
	public String getQuotedVmArguments(String includes) throws CoreException{
		return quote(getVmArguments(includes));
	}
	
	/**
	 * Quotes a single command line argument if necessary.
	 * 
	 * @param arg
	 *            command line argument
	 * @return quoted argument
	 */
	static String quote(final String arg) {
		final StringBuilder escaped = new StringBuilder();
		for (final char c : arg.toCharArray()) {
			if (c == QUOTE || c == SLASH) {
				escaped.append(SLASH);
			}
			escaped.append(c);
		}
		if (arg.indexOf(BLANK) != -1 || arg.indexOf(QUOTE) != -1) {
			escaped.insert(0, QUOTE).append(QUOTE);
		}
		
		return escaped.toString();
	}
}