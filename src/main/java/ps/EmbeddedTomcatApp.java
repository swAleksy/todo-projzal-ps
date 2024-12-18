package ps;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import ps.servlet.TodoServlet;

import java.io.File;

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
            File webInfDir = new File(webappDir, "WEB-INF/classes");
            webInfDir.mkdirs(); // Ensure WEB-INF directory exists

            // Copy compiled classes to WEB-INF/classes
            FileUtils.copyDirectory(classesDir, webInfDir);

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