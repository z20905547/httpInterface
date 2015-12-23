package vfh.httpInterface.test.automation;

import vfh.httpInterface.test.LaunchJetty;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自动化单元测试记录
 *
 * @author maurice
 */
@Component
public class AutomationTestCaseSupport {

    public static final int DEFAULT_WAIT_TIME = 20;
    public static final String URL = "http://localhost:8080" + LaunchJetty.DEFAULT_CONTEXT_PATH;

    protected static DataSource dataSource;
    protected static Server jettyServer;
    protected static WebDriver driver;
    protected static ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Autowired
    public void setDataSource(DataSource dataSource) {
        AutomationTestCaseSupport.dataSource = dataSource;
    }

    /**
     * 构建测试环境
     * @throws Exception
     */
    @BeforeClass
    public static void install() throws Exception {

        //如果jetty没启动，启动jetty
        if (jettyServer == null) {
            // 设定Spring的profile
            System.setProperty(LaunchJetty.ACTIVE_PROFILE, "test");

            jettyServer = new Server();
            jettyServer.setStopAtShutdown(true);
            SelectChannelConnector connector = new SelectChannelConnector();

            connector.setPort(LaunchJetty.DEFAULT_PORT);
            connector.setReuseAddress(false);
            //  由于 apache shiro 的 rememberMe 服务写入cookie值超出了 jeety 的默认 header buffer size 所有要加大，否则会有重定向错误问题。
            connector.setResponseHeaderSize(8192);

            jettyServer.setConnectors(new Connector[] { connector });
            WebAppContext webContext = new WebAppContext(LaunchJetty.DEFAULT_WEBAPP_PATH, LaunchJetty.DEFAULT_CONTEXT_PATH);
            jettyServer.setHandler(webContext);
            System.out.println("[HINT] Don't forget to set -XX:MaxPermSize=128m");
            jettyServer.start();
        }

        //如果selenium没初始化，初始化selenium
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver","./chromedriver.exe");
            //System.setProperty ( "webdriver.firefox.bin" , "firefox 安装路径" );
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
            Runtime.getRuntime().addShutdownHook(new Thread("Selenium Quit Hook") {
                @Override
                public void run() {
                    driver.quit();
                }
            });
        }

        //载入新的数据
        executeScript(dataSource.getConnection(), "classpath:data/cleanup-data.sql","classpath:data/insert-data.sql");

        //打开浏览器，并登录
        open("/");
        input(By.xpath("//input[@id='username']"),"admin");
        input(By.xpath("//input[@id='password']"),"123456");
        click(By.xpath("//button[@type='submit']"));
    }

    @AfterClass
    public static void uninstall() {
        click(By.xpath("//li[@id='user-profile']//a[@class='dropdown-toggle']"));
        click(By.xpath("//li[@id='user-profile']//a[@href='" + LaunchJetty.DEFAULT_CONTEXT_PATH + "/logout']"));
    }

    /**
     * 打开地址,如果url为相对地址
     *
     * @param url 地址
     */
    public static void open(String url) {
        final String urlToOpen = url.indexOf("://") == -1 ? URL + (!url.startsWith("/") ? "/" : "") + url : url;
        driver.get(urlToOpen);
    }

    /**
     * 点击页面元素
     *
     * @param by 用于在 docuemnt 定位元素的类
     */
    public static void click(By by) {
        driver.findElement(by).click();
    }

    /**
     * 选中页面元素
     *
     * @param by 用于在 docuemnt 定位元素的类
     */
    public static void check(By by) {
        WebElement element = driver.findElement(by);
        if (!element.isSelected()) {
            element.click();
        }
    }

    /**
     * 输入文本
     *
     * @param by 用于在 docuemnt 定位元素的类
     * @param text 文本
     */
    public static void input(By by, String text) {
        WebElement element = driver.findElement(by);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * 获取 select 元素
     *
     * @param by 用于在 docuemnt 定位元素的类
     *
     * @return select 元素
     */
    public Select select(By by) {
        return new Select(driver.findElement(by));
    }

    /**
     * 获取页面元素
     *
     * @param by 用于在 docuemnt 定位元素的类
     *
     * @return 页面元素集合
     */
    public static List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    /**
     * 获取页面元素
     *
     * @param by 用于在 docuemnt 定位元素的类
     *
     * @return 页面元素集合
     */
    public static WebElement findElement(By by) {
        return driver.findElement(by);
    }

    /**
     * 等待某些东西
     *
     * @param conditon 等待条件
     *
     */
    public static void wait(ExpectedCondition<?> conditon) {
        wait(conditon, DEFAULT_WAIT_TIME);
    }

    /**
     * 等待某些东西
     *
     * @param conditon 等待条件
     * @param timeout 等待时间
     *
     */
    public static void wait(ExpectedCondition<?> conditon, int timeout) {
        (new WebDriverWait(driver, timeout)).until(conditon);
    }

    /**
     * 批量执行sql文件
     *
     * @param connection jdbc 链接对象
     * @param sqlResourcePaths sql 文件路径
     *
     * @throws java.sql.SQLException
     */
    private static void executeScript(Connection connection, String... sqlResourcePaths) throws SQLException {

        for (String sqlResourcePath : sqlResourcePaths) {
            Resource resource = resourceLoader.getResource(sqlResourcePath);
            ScriptUtils.executeSqlScript(connection, resource);
        }
        connection.close();
    }

}
