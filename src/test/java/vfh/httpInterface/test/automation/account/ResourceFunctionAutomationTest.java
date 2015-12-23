package vfh.httpInterface.test.automation.account;

import vfh.httpInterface.test.LaunchJetty;
import vfh.httpInterface.test.automation.AutomationTestCaseSupport;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 资源管理功能自动化单元测试类
 *
 * @author maurice
 */
public class ResourceFunctionAutomationTest extends AutomationTestCaseSupport{

    @Test
    public void test() {

        //通过点击，进入功能模块
        click(By.id("1"));
        click(By.id("11"));

        //获取tree中的所有操作前的li节点
        List<WebElement> beforeLi = findElements(By.xpath("//div[@class='tree']//*//li"));
        //断言所有li是否等于期望值
        assertEquals(beforeLi.size(), 23);

        //打开添加页面
        click(By.xpath("//a[@href='" + LaunchJetty.DEFAULT_CONTEXT_PATH + "/account/resource/edit']"));
        //填写表单
        select(By.xpath("//form[@id='edit-resource-form']//select[@name='type']")).selectByValue("1");
        input(By.xpath("//form[@id='edit-resource-form']//input[@name='value']"), "/test/**");
        input(By.xpath("//form[@id='edit-resource-form']//input[@name='permission']"), "perms[test:test]");
        input(By.xpath("//form[@id='edit-resource-form']//textarea[@name='remark']"), "这是一个测试的资源");

        //提交表单，页面验证不通过
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
        //设置最后的一个值
        input(By.xpath("//form[@id='edit-resource-form']//input[@name='name']"), "test");
        //验证通过，提交表单
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        //返回成功信息
        String message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("保存成功"));

        //获取tree中的所有操作后的li节点
        List<WebElement> afterLi = findElements(By.xpath("//div[@class='tree']//*//li"));
        //断言所有li是否等于期望值
        assertEquals(afterLi.size(), beforeLi.size() + 1);

        //点击编辑功能
        findElement(By.xpath("//*//div//*[contains(text(),'test')]//..//..//..//a[@href!='']")).click();
        //填写表单
        input(By.xpath("//form[@id='edit-resource-form']//input[@name='name']"), "modify");
        select(By.xpath("//form[@id='edit-resource-form']//select[@name='type']")).selectByValue("2");

        //提交表单
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        //返回成功信息
        message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("保存成功"));

        afterLi = findElements(By.xpath("//div[@class='tree']//*//li"));
        //断言所有li是否等于期望值
        assertEquals(afterLi.size(), beforeLi.size() + 1);

        //选中删除的记录
        check(By.xpath("//*//div//*[contains(text(),'modify')]//..//input"));
        //提交删除表单
        click(By.xpath("//div[@class='panel-footer']//*[@type='button']"));
        click(By.xpath("//div[@class='bootbox modal fade bootbox-confirm in']//*[@data-bb-handler='confirm']"));

        //返回成功信息
        message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("删除1条信息成功"));

        afterLi = findElements(By.xpath("//div[@class='tree']//*//li"));
        //断言所有li是否等于期望值
        assertEquals(afterLi.size(), beforeLi.size());
    }
}
