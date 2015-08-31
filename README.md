### Step 1:
        <dependency>
            <groupId>cn.solomonqbq.jetty</groupId>
            <artifactId>debug_server</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

### Setp 2:
       create a main class like this:
        public class Test {
            public static void main(String[] args) throws Exception {
                JettyEmbedServer s = new JettyEmbedServer();
                s.setPort(80);
                s.setWebBase("/webapp");
                s.start();
            }
        }
### Step 3:
    把包含webapp目录的目录加到classpath里，通常是main或main/resources


### Step 4:
    debug GO GO!