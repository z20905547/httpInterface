package vfh.httpInterface.test.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 业务单元测试基类
 * 
 * @author maurice
 *
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-core.xml")
public abstract class ServiceTestCaseSupport {
	
	private DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 通过表名计算出表中的总记录数
	 * 
	 * @param tableName 表名
	 * 
	 * @return int
	 */
	protected int countRowsInTable(String tableName) {
		return jdbcTemplate.queryForObject("SELECT COUNT(0) FROM " + tableName,Integer.class);
	}
	
	/**
	 * 
	 * 每个单元测试用例开始先把模拟数据加载到 dataSource 中
	 * 
	 * @throws Exception
	 */
	@Before
	public void install() throws SQLException {
		executeScript(dataSource.getConnection(), "classpath:data/cleanup-data.sql", "classpath:data/insert-data.sql");
	}
	
	/**
	 * 批量执行sql文件
	 * 
	 * @param connection jdbc 链接对象
	 * @param sqlResourcePaths sql 文件路径
	 * 
	 * @throws java.sql.SQLException
	 */
	private void executeScript(Connection connection, String... sqlResourcePaths) throws SQLException {

		for (String sqlResourcePath : sqlResourcePaths) {
            Resource resource = resourceLoader.getResource(sqlResourcePath);
            ScriptUtils.executeSqlScript(connection, resource);
		}
        connection.close();
	}

    @Test
    public void empty() {
        Assert.assertTrue(true);
    }
	
}
