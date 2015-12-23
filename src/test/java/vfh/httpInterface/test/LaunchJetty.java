package vfh.httpInterface.test;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动 jetty ,启动完成后通过 http://localhost:8080/httpInterface 访问项目
 *
 * @author maurice
 */
public class LaunchJetty {

    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchJetty.class);
    /**
     * 启动 jetty 时，默认访问的项目的端口
     */
    public static final int DEFAULT_PORT = 8080;
    /**
     * 启动 jetty 时，默认服务的Web项目路径
     */
    public static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";
    /**
     * 启动jetty时，默认方法的项目名称
     */
    public static final String DEFAULT_CONTEXT_PATH = "/httpInterface";
    /**
     * spring 环境属性
     */
    public static final String ACTIVE_PROFILE = "spring.profiles.active";

    public static void main(String[] args) {
        System.setProperty(ACTIVE_PROFILE, "development");
        Server server = new Server();
        // 设置在JVM退出时关闭Jetty
        server.setStopAtShutdown(true);

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(DEFAULT_PORT);
        // 由于 apache shiro 的 rememberMe 服务写入cookie值超出了 jeety 的默认 header buffer size 所有要加大，否则会有重定向错误问题。
        connector.setResponseHeaderSize(8192);
        // 重复启动Jetty端口冲突报错设置
        connector.setReuseAddress(false);
        server.setConnectors(new Connector[] { connector });

        WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, DEFAULT_CONTEXT_PATH);
        server.setHandler(webContext);

        try {
            server.start();
            LOGGER.info("Browse URL : http://localhost:" + DEFAULT_PORT + DEFAULT_CONTEXT_PATH);
            // 等待用户输入回车重启jetty.
            while (true) {
                char c = (char) System.in.read();
                if (c == '\n') {
                    // 获取当前 jetty 下的Web项目
                    WebAppContext currentContext = (WebAppContext) server.getHandler();
                    // 停止项目
                    currentContext.stop();

                    // 重新创建一个ClassLoader
                    WebAppClassLoader classLoader = new WebAppClassLoader(currentContext);
                    // 设置新编译的class路径给ClassLoader
                    classLoader.addClassPath("target/classes");
                    currentContext.setClassLoader(classLoader);

                    // 启动服务
                    currentContext.start();
                }
            }
        } catch (Exception e) {
            LOGGER.error("jetty 运行出错", e);
        }
    }

}
