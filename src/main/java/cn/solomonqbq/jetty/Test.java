package cn.solomonqbq.jetty;

/**
 * Created by solomonqbq on 2015/8/28.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        JettyEmbedServer s = new JettyEmbedServer();
        s.setPort(80);


        s.setWebBase("/webapp");


        s.start();
    }
}
