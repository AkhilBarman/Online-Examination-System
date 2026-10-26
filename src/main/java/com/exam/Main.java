package com.exam;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Terminal Runner for the Online Examination System using Embedded Apache Tomcat 9.
 * Allows running the web application directly from the CLI without external IDE or Tomcat setup.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // Initialize default HTTP connector

        // Base working directory for Tomcat temp files
        File baseDir = new File("target/tomcat");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        tomcat.setBaseDir(baseDir.getAbsolutePath());

        // Set parent classloader for the host and engine
        ClassLoader classLoader = Main.class.getClassLoader();
        tomcat.getHost().setParentClassLoader(classLoader);

        // Webapp root directory
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webappDir);
        ctx.setParentClassLoader(classLoader);
        ctx.setSessionTimeout(30);

        // Tell Tomcat where the compiled classes are located
        File additionWebInfClasses = new File("target/classes");
        if (!additionWebInfClasses.exists()) {
            additionWebInfClasses.mkdirs();
        }

        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(
                resources,
                "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(),
                "/"
        ));
        ctx.setResources(resources);

        tomcat.start();

        System.out.println("==================================================================");
        System.out.println(" ONLINE EXAMINATION SYSTEM IS RUNNING IN TERMINAL!");
        System.out.println(" Open your browser and go to: http://localhost:" + port + "/");
        System.out.println("------------------------------------------------------------------");
        System.out.println(" Default Admin:   admin@exam.com   / admin123");
        System.out.println(" Student Access:  Register dynamically via /register.jsp");
        System.out.println(" Press Ctrl+C in this terminal to stop the server.");
        System.out.println("==================================================================");

        tomcat.getServer().await();
    }
}
