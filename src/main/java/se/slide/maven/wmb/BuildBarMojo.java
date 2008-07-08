package se.slide.maven.wmb;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * 
 * @author www.slide.se
 * @goal buildbar
 */
public class BuildBarMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private org.apache.maven.project.MavenProject project;
	
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
