package cn.solomonqbq.jetty;

/**
 * Created by solomonqbq on 2015/8/28.
 */

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

public class JettyEmbedServer {

    private String jettyConf = "/debug_jetty.xml";
    private String webBase = "/webapp";
    private Integer port = 8080;
    final Integer BUFFER_SIZE = 1024;

    private Server server;

    public JettyEmbedServer() {
    }

    private String InputStreamTOString(InputStream in, String encoding) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), encoding);
    }

    public void start() throws Exception {
        try {
            Resource configXml = Resource.newSystemResource(jettyConf);
            String fileContent = InputStreamTOString(configXml.getInputStream(), "UTF-8");
            fileContent = fileContent.replaceFirst("\\$\\{port\\}", String.valueOf(port));
            StringReader sr = new StringReader(fileContent);
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
            XmlConfiguration configuration = new XmlConfiguration(is);
            server = (Server) configuration.configure();
            Handler handler = server.getHandler();
            if (handler != null && handler instanceof WebAppContext) {
                WebAppContext webAppContext = (WebAppContext) handler;
                URL url = JettyEmbedServer.class.getResource(webBase);
                if (url==null){
                    System.out.println("can't find "+webBase+"in claspath!");
                    System.exit(0);
                }
                webAppContext.setResourceBase(url.toString());
            }
            server.start();
            System.out.println("##Jetty Embed Server is startup!");

            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    try {
                        System.out.println("## stop the jetty server");
                        join();
                    } catch (Throwable e) {
                        System.out.println("##something goes wrong when stopping jetty Server:\n"+
                                ExceptionUtils.getFullStackTrace(e));
                    } finally {
                        System.out.println("## jetty server is down.");
                    }
                }

            });
        } catch (Throwable e) {
            System.exit(0);
        }
    }

    private void join() throws Exception {
        server.join();
        System.out.println("##Jetty Embed Server joined!");
    }

    public void stop() throws Exception {
        server.stop();
        System.out.println("##Jetty Embed Server is stop!");
    }

    // ================ setter / getter ================

    public String getWebBase() {
        return webBase;
    }

    public void setWebBase(String webBase) {
        this.webBase = webBase;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
