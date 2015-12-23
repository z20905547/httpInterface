package vfh.httpInterface.test.automation.account;

import vfh.httpInterface.test.LaunchJetty;
import vfh.httpInterface.test.automation.AutomationTestCaseSupport;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 用户管理功能自动化单元测试类
 *
 * @author maurice
 */
public class UserFunctionAutomationTest extends AutomationTestCaseSupport{

    @Test
    public void test() {

        // 通过点击，进入功能模块
        click(By.xpath("//a[@id='1']"));
        click(By.xpath("//a[@id='2']"));

        // 获取table中的所有操作前的tr
        List<WebElement> beforeTrs = findElements(By.xpath("//table//tbody//tr"));
        // 断言所有tr是否等于期望值
        assertEquals(beforeTrs.size(), 3);

        //打开添加页面
        click(By.xpath("//a[@href='"+ LaunchJetty.DEFAULT_CONTEXT_PATH + "/account/user/add']"));

        //填写表单
        input(By.xpath("//form[@id='create-user-form']//input[@name='username']"), "admin");
        input(By.xpath("//form[@id='create-user-form']//input[@name='nickname']"), "maurice.chen");
        input(By.xpath("//form[@id='create-user-form']//input[@name='password']"), "123456");
        input(By.xpath("//form[@id='create-user-form']//input[@name='confirmPassword']"), "123456");
        select(By.xpath("//form[@id='create-user-form']//select[@name='state']")).selectByValue("1");
        input(By.xpath("//form[@id='create-user-form']//input[@name='email']"), "es.chenxiaobo@gmail.com");

        // 选中所有复选框
        check(By.xpath("//input[@id='selectAll']"));
        // 提交表单，页面验证不通过
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
        // 设置最后的一个值
        input(By.xpath("//form[@id='create-user-form']//input[@name='username']"), "test_user");
        // 验证通过，提交表单
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        // 返回成功信息
        String message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("新增成功"));

        // 获取table中的所有操作前的tr
        List<WebElement> aflterTrs = findElements(By.xpath("//table//tbody//tr"));
        // 添加成功后应该比开始的记录多一条
        assertEquals(aflterTrs.size(), beforeTrs.size() + 1);

        // 点击编辑功能
        click(By.xpath("//table//tbody//tr//*[text()='test_user']//..//a"));
        // 填写表单
        input(By.xpath("//form[@id='update-user-form']//input[@name='nickname']"), "modify");
        select(By.xpath("//form[@id='update-user-form']//select[@name='state']")).selectByValue("2");

        // 提交表单
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        // 返回成功信息
        message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("修改成功"));

        aflterTrs = findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
        // 添加成功后应该比开始的记录多一条
        assertEquals(aflterTrs.size(), beforeTrs.size() + 1);

        // 选中删除的记录
        check(By.xpath("//table//tbody//tr//*[text()='test_user']//..//input"));
        // 提交删除表单
        click(By.xpath("//div[@class='panel-footer']//*[@type='button']"));
        // 点击模态框的确定按钮
        click(By.xpath("//div[@class='bootbox modal fade bootbox-confirm in']//*[@data-bb-handler='confirm']"));

        // 返回成功信息
        message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("删除1条信息成功"));

        aflterTrs = findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
        // 删除成功后应该刚刚开始的记录一样
        assertEquals(aflterTrs.size(), beforeTrs.size());

        // 打开查询框
        click(By.xpath("//div[@class='panel-footer']//*[@data-toggle='modal']"));
        // 等待显示查询框
        wait(ExpectedConditions.visibilityOfElementLocated(By.id("search-modal")));

        // 设置查询条件值
        input(By.xpath("//form[@id='user-search-form']//input[@name='username']"), "admin");
        input(By.xpath("//form[@id='user-search-form']//input[@name='nickname']"), "超级");
        select(By.xpath("//form[@id='user-search-form']//select[@name='state']")).selectByValue("1");
        input(By.xpath("//form[@id='user-search-form']//input[@name='email']"), "administrator");

        // 查询
        click(By.xpath("//div[@class='modal-footer']//button[@type='submit']"));

        aflterTrs = findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
        // 断言查询后的记录数
        assertEquals(aflterTrs.size(), 1);
    }

}
