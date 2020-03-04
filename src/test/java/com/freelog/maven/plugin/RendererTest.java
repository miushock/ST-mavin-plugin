package com.freelog.maven.plugin;


import org.apache.maven.plugin.testing.MojoRule;
//import org.apache.maven.plugin.testing.WithoutMojo;

import org.junit.Rule;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;

public class RendererTest
{
    @Rule
    public MojoRule rule = new MojoRule()
    {
        @Override
        protected void before() throws Throwable 
        {
        }

        @Override
        protected void after()
        {
        }
    };

    /**
     * @throws Exception if any
     */
    @Test
    public void testRenderer()
            throws Exception
    {
        File pom = new File( "target/test-classes/project-to-test/" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        Renderer renderer = ( Renderer ) rule.lookupConfiguredMojo( pom, "render" );
        assertNotNull( renderer );
        renderer.execute();

        File outputDirectory = ( File ) rule.getVariableValueFromObject( renderer, "outputDirectory" );
        assertNotNull( outputDirectory );
        assertTrue( outputDirectory.exists() );

        File generated = new File( outputDirectory, "rendered.txt" );
        assertTrue( generated.exists() );

    }

}

