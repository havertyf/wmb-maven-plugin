package se.slide.maven.wmb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
	 * 
	 * @parameter expression="${basedir}"
	 */
	private File baseDirectory;

	/**
	 * Base directory of the project.
	 * 
	 * @parameter expression="${wmbWorkspace}" default-value="src\\wmb"
	 */
	private String wmbWorkspace;

	final static String TARGET_FOLDER = "target";

	public void execute() throws MojoExecutionException, MojoFailureException {
		/*
		 * Set the workspace folder to our src\wmb folder TODO Make this a
		 * configurable setting
		 */
		String workspace = "." + File.separator + wmbWorkspace;

		// Set up the target folder
		createTargetFolderIfNeeded();

		// All folders in the workspace could be a project folder
		Collection<File> projects = new ArrayList<File>();
		for (File child : new File(workspace).listFiles()) {
			if (child.isDirectory()) {
				projects.add(child);
			}
		}

		// For every WMB project folder (we assume this to be true for every
		// project with a msgflow/mset
		// TODO What about ESQL-only projects?
		Collection<WmbProject> wmbProjects = new ArrayList<WmbProject>();
		for (File project : projects) {
			WmbProject wmbProject = new WmbProject();
			wmbProject.setProjectName(project.getName());
			for (File file : project.listFiles()) {
				if (file.getName().endsWith("msgflow")
						|| file.getName().endsWith("mset")) {
					wmbProject.addProjectFile(file.getName());
				}
			}
			if (!wmbProject.getProjectFiles().isEmpty()) {
				getLog().info(
						"Adding WMB project: " + wmbProject.getProjectName());
				wmbProjects.add(wmbProject);
			}
		}

		try {
			String command = "";
			command += "mqsicreatebar";
			command += " -data";
			command += " " + workspace;
			command += " -cleanBuild";
			command += " -b " + TARGET_FOLDER + File.separator
					+ project.getArtifactId() + ".bar";

			for (WmbProject wmbProject : wmbProjects) {
				command += " -p " + wmbProject.getProjectName();
				for (String filename : wmbProject.getProjectFiles()) {
					command += " -o " + wmbProject.getProjectName() + "\\"
							+ filename;
				}
			}

			getLog().info("Executing command: " + command);

			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec(command);

			int exitVal = process.waitFor();

			if (exitVal > 0) {
				// Output error
				String error = "Reported error: " + getOutAndErrStream(process);

				getLog().error(error);
				throw new MojoFailureException(error);
			}
		}
		// For exec
		catch (IOException e) {
			getLog().info("Error executing process: " + e.getMessage());
		}
		// For waitFor
		catch (InterruptedException e) {
			getLog().info("Error executing mqsicreatebar: " + e.getMessage());
		}

	}

	private String getOutAndErrStream(Process p) {
		StringBuffer cmd_out = new StringBuffer("");
		if (p != null) {
			BufferedReader is = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String buf = "";
			try {
				while ((buf = is.readLine()) != null) {
					cmd_out.append(buf);
					cmd_out.append(System.getProperty("line.separator"));
				}
				is.close();
				is = new BufferedReader(new InputStreamReader(p
						.getErrorStream()));
				while ((buf = is.readLine()) != null) {
					cmd_out.append(buf);
					cmd_out.append("\n");
				}
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cmd_out.toString();
	}

	private void createTargetFolderIfNeeded() {
		File targetFolder = new File(project.getBasedir().getAbsolutePath()
				+ File.separator + TARGET_FOLDER);

		if (!targetFolder.exists()) {
			targetFolder.mkdir();
			getLog().info("Target folder not found, created target folder.");
		}
	}
}
