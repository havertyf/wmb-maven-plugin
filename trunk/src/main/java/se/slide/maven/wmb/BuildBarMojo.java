package se.slide.maven.wmb;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import java.io.*;

/**
 * 
 * @author www.slide.se
 * @goal buildbar
 */
public class BuildBarMojo extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("building bar..");
		
		try
		{
			//String[] cmd = { "mqsicreatebar", "-version" }; // use for parameters
			String[] cmd = { "mqsicreatebar" };
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			getLog().info(p.exitValue() + "");
			
		}
		catch (Exception error) {
			getLog().info("Error running mqsicreatebar. Make sure mqsicreatebar.exe is in your path variables. Exception message: " + error.getMessage());
		}
		
	}

}
