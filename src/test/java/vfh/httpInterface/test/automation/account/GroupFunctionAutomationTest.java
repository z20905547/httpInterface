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
 * 组管理功能自动化单元测试类
 *
 * @author maurice
 */
public class GroupFunctionAutomationTest extends AutomationTestCaseSupport{

    @Test
    public void test() {

        // 通过点击，进入功能模块
        click(By.xpath("//a[@id='1']"));
        click(By.xpath("//a[@id='7']"));

        // 获取table中的所有操作前的tr
        List<WebElement> beforeTrs = findElements(By.xpath("//table//tbody//tr"));
        // 断言所有tr是否等于期望值
        assertEquals(beforeTrs.size(), 3);

        //打开添加页面
        click(By.xpath("//a[@href='" + LaunchJetty.DEFAULT_CONTEXT_PATH + "/account/group/edit']"));
        //填写表单
        input(By.xpath("//form[@id='edit-group-form']//input[@name='name']"), "test");
        //选中所有复选框
        check(By.xpath("//button[@data-toggle='checkAll']"));

        input(By.xpath("//form[@id='edit-group-form']//textarea[@name='remark']"),"这是一个测试添加的组记录");
        //提交表单，页面验证不通过
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        //返回成功信息
        String message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("保存成功"));

        // 获取table中的所有操作前的tr
        List<WebElement> aflterTrs = findElements(By.xpath("//table//tbody//tr"));
        // 添加成功后应该比开始的记录多一条
        assertEquals(aflterTrs.size(), beforeTrs.size() + 1);

        // 点击编辑功能
        click(By.xpath("//table//tbody//tr//*[text()='test']//..//a"));
        // 填写表单
        input(By.xpath("//form[@id='edit-group-form']//input[@name='name']"), "modify");

        // 提交表单
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        // 返回成功信息
        message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("保存成功"));

        // 选中删除的记录
        check(By.xpath("//table//tbody//tr//*[text()='modify']//..//input"));
        // 提交删除表单
        click(By.xpath("//div[@class='panel-footer']//*[@type='button']"));
        // 点击模态框的确定按钮
        click(By.xpath("//div[@class='bootbox modal fade bootbox-confirm in']//*[@data-bb-handler='confirm']"));

        // 打开查询框
        click(By.xpath("//div[@class='panel-footer']//*[@data-toggle='modal']"));
        // 等待显示查询框
        wait(ExpectedConditions.visibilityOfElementLocated(By.id("search-modal")));

        // 设置查询条件值
        input(By.xpath("//form[@id='group-search-form']//input[@name='name']"), "超级");

        // 查询
        click(By.xpath("//div[@class='modal-footer']//button[@type='submit']"));

        aflterTrs = findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
        // 断言查询后的记录数
        assertEquals(aflterTrs.size(), 1);
    }

}
