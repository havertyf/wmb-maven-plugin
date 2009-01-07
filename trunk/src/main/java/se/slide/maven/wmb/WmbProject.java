package se.slide.maven.wmb;

import java.util.ArrayList;
import java.util.Collection;

public class WmbProject {

	private String projectName = null;
	private Collection<String> projectFiles = new ArrayList<String>();

	public WmbProject() {

	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Collection<String> getProjectFiles() {
		return projectFiles;
	}

	public void setProjectFiles(Collection<String> projectFiles) {
		this.projectFiles = projectFiles;
	}

	public void addProjectFile(String filename) {
		this.projectFiles.add(filename);
	}
}
