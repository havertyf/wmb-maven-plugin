/*
 * Copyright Sonatype
 * 
 * Based on ZipMojo from Sonatype's Maven: The Definitive Guide.
 * 
 * License: http://creativecommons.org/licenses/by-nc-nd/3.0/us/
 * 
 */
package se.slide.maven.wmb;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;

/**
 * Zips up the output directory.
 * 
 * @goal zip
 * @phase package
 */
public class ZipMojo extends AbstractMojo {
	/**
	 * The Zip archiver.
	 * 
	 * @parameter \
	 *            expression="${component.org.codehaus.plexus.archiver.Archiver#zip}"
	 */
	private ZipArchiver zipArchiver;

	/**
	 * Directory containing the build files.
	 * 
	 * @parameter expression="${srcDirectory}" default-value="src"
	 */
	private File srcDirectory;

	/**
	 * Base directory of the project.
	 * 
	 * @parameter expression="${targetDirectory}" default-value="target"
	 */
	private File targetDirectory;

	/**
	 * A set of file patterns to include in the zip.
	 * 
	 * @parameter alias="includes"
	 */
	private String[] mIncludes;

	/**
	 * A set of file patterns to exclude from the zip.
	 * 
	 * @parameter alias="excludes"
	 */
	private String[] mExcludes;

	public void setExcludes(String[] excludes) {
		mExcludes = excludes;
	}

	public void setIncludes(String[] includes) {
		mIncludes = includes;
	}

	public void execute() throws MojoExecutionException {
		try {
			zipArchiver = new ZipArchiver();
			//zipArchiver.addDirectory( srcDirectory, includes, excludes );
			zipArchiver.addDirectory(srcDirectory, mIncludes, mExcludes); // Cannot get the includes/excludes to work
			zipArchiver.setDestFile(new File(targetDirectory, "output.zip"));
			zipArchiver.createArchive();
		} catch (Exception e) {
			throw new MojoExecutionException("Could not zip", e);
		}
	}
}
