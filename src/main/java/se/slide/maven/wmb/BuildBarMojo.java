package se.slide.maven.wmb;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

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
	
    /**
     * Base directory of the project.
     * @parameter expression="${basedir}"
     */
    private File baseDirectory;

	
	public void execute() throws MojoExecutionException, MojoFailureException {
		/*
		 * Set the workspace folder to our src\wmb folder
		 * TODO Make this a configurable setting
		 * 
		 */
		String workspace = ".\\src\\wmb";
		
		Collection<File> projects = new ArrayList<File>();
		Collection<String> msgflows = new ArrayList<String>();
		Collection<String> msgsets = new ArrayList<String>();
		
		for (File child : new File(workspace).listFiles())
		{
			if (child.isDirectory())
			{
				projects.add(child);
			}
		}
		
		for (File project : projects)
		{
			for (File file : project.listFiles())
			{
				getLog().info(file.getPath());
			}
		}
		
		getLog().info("Building bar..");
		
		String m = "";		
		
		try
		{
			
			//String[] cmd = { "mqsicreatebar", "-version" }; // use for parameters
			String[] cmd = { "mqsicreatebar", "-data C:\\Temp\\IC0541-Sample\\trunk\\src\\wmb", "-cleanBuild", "-b mike.bar", "-o IC0001-Sample\\IC0001_XML_to_IDOC.msgflow", "-p IC0001-Sample", "-o IC0002-Sample\\IC0001_XML_to_IDOC.msgflow", "-p IC0002-Sample" };
			//Process p = Runtime.getRuntime().exec(cmd);
			//p.waitFor();
			//if (p.exitValue() == 1)
			//getLog().info(p.exitValue() + " >> " + p.getErrorStream().toString());
			
			//String command = "mqsicreatebar -data .\\src\\wmb -cleanBuild -b mike.bar -o IC0001-Sample\\IC00012_XML_to_IDOC.msgflow -p IC0001-Sample";
			
			String command = "";
			
			Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            
            int exitVal = proc.waitFor();
            
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            
            System.out.println("<ERROR>");
            while ( (line = br.readLine()) != null)
                System.out.println(line);
            System.out.println("</ERROR>");
            
            System.out.println("Process exitValue: " + exitVal);

		}
		catch (Exception error) {
			getLog().info("Error running mqsicreatebar. Make sure mqsicreatebar.exe is in your path variables. Exception message: " + error.getMessage());
		}
		
	}

}
