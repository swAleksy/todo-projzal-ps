package ps;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import ps.servlet.TodoServlet;

import java.io.File;


//public class EmbeddedTomcatApp {
//    public static void main(String[] args) {
//        System.setProperty("org.apache.catalina.startup.DEBUG", "true");
//
//        Tomcat tomcat = new Tomcat();
//        // Set port
//        tomcat.setPort(8081);
//        try {
//            // Bind address
//            tomcat.getConnector().setProperty("address", "127.0.0.1");
//
//            // Set up webapp directory
//            File webappDir = new File("src/main/webapp").getAbsoluteFile();
//            System.out.println("Webapp directory: " + webappDir.getAbsolutePath());
//            System.out.println("Webapp directory exists: " + webappDir.exists());
//
//            // Add web application at root context
//            tomcat.addWebapp("", webappDir.getAbsolutePath());
//
//            // Start the server
//            tomcat.start();
//            System.out.println("Server started at http://localhost:8081");
//
//            tomcat.getServer().await();
//
//        } catch (LifecycleException e) {
//            e.printStackTrace();
//        }
//    }
//}

public class EmbeddedTomcatApp {
    public static void main(String[] args) {
        System.setProperty("org.apache.catalina.startup.DEBUG", "true");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);

        try {
            tomcat.getConnector().setProperty("address", "127.0.0.1");
            // Path to webapp directory
            File webappDir = new File("src/main/webapp").getAbsoluteFile();

            // Create WEB-INF/classes dynamically for compiled code
            File classesDir = new File("target/classes");
            File webInfDir = new File(webappDir, "WEB-INF");
            webInfDir.mkdirs(); // Ensure WEB-INF directory exists
            File webInfClassesDir = new File(webInfDir, "classes");
            webInfClassesDir.mkdirs();

            // Copy compiled classes to WEB-INF/classes
            org.apache.commons.io.FileUtils.copyDirectory(classesDir, webInfClassesDir);

            // Add the webapp
            tomcat.addWebapp("", webappDir.getAbsolutePath());

            tomcat.start();
            System.out.println("Server started at http://localhost:8081");

            tomcat.getServer().await();
        } catch (LifecycleException | java.io.IOException e) {
            e.printStackTrace();
        }
    }
}