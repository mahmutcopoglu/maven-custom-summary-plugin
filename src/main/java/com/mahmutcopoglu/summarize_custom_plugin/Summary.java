package com.mahmutcopoglu.summarize_custom_plugin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name= "summarize", defaultPhase = LifecyclePhase.COMPILE)
public class Summary extends AbstractMojo{
	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject mavenProject;
	
	@Parameter(defaultValue = "${project.build.directory}", required=true)
	private String outputFile;
	
	
	public void execute() throws MojoExecutionException, MojoFailureException{
		
		outputFile+="\\myreposummary.txt";
		
		
		List<Developer> developers = mavenProject.getDevelopers();
		List<Dependency> dependencies = mavenProject.getDependencies();
		List<Plugin> plugins = mavenProject.getBuildPlugins();
		String groupID = mavenProject.getGroupId();
		String artifactID = mavenProject.getArtifactId();
		String version = mavenProject.getVersion();
		String releaseDate = mavenProject.getProperties().getProperty("release.date");
		
		String projectInfo = String.format("Project Info\n");
		projectInfo+=String.format("%s.%s.%s\n", groupID, artifactID, version);
		
		int devCounter=1;
		String developerInfo = String.format("Developers\n");
		for(Developer developer: developers){
			developerInfo+=String.format("Developer %d Name: %s\n", devCounter, developer.getName());
			devCounter++;
		}
		
		String dependenciesInfo = String.format("Dependencies\n");
		for(Dependency dependency: dependencies) {
			dependenciesInfo+=String.format("Dependency: %s.%s\n", dependency.getGroupId(), dependency.getArtifactId());
		}

		String pluginInfo=String.format("Plugins\n");
		for(Plugin plugin: plugins) {
			pluginInfo+=String.format("Plugin: %s\n", plugin.getArtifactId());
		}
		
		String releaseInfo = String.format("Release Date\n");
		releaseInfo+= String.format("Release Date: %s", releaseDate);
		
		String text=String.format("%s%s%s%s%s", projectInfo, developerInfo, releaseInfo, dependenciesInfo, pluginInfo);
		
		
		
		
		try {
			FileWriter dataText = new FileWriter(outputFile);
			BufferedWriter writer = new BufferedWriter(dataText);
			writer.write(text);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	
	
}
