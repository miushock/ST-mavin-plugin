package com.freelog.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Map;

import org.stringtemplate.v4.*;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "render", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class Renderer extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;

    @Parameter(defaultValue = "${project.basedir}/src/main/template_group", property = "templateDir")
    private String templateGroupDir;

    @Parameter(defaultValue = "${project.basedir}/src/main/template_group", property = "templateDir", required = true)
    private String startingRule;

    @Parameter
    private Map<String, String> templateArguments;

    public void execute() throws MojoExecutionException {
        File f = outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        STGroup group = new STGroupDir(templateGroupDir);
        ST st = group.getInstanceOf(startingRule);
        for (Map.Entry<String, String> entry: templateArguments.entrySet()) {
            st.add(entry.getKey(), entry.getValue());
        }
        String result = st.render();

        File generated = new File(f, "rendered.txt");
        FileWriter w = null;
        try {
            w = new FileWriter(generated);
            w.write(result);
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + generated, e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
