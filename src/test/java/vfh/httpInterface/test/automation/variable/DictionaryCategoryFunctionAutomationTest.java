package vfh.httpInterface.test.automation.variable;

import vfh.httpInterface.test.LaunchJetty;
import vfh.httpInterface.test.automation.AutomationTestCaseSupport;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.Assert.*;
/**
 * 字典类别管理功能自动化单元测试类
 * 
 * @author maurice
 */
public class DictionaryCategoryFunctionAutomationTest extends AutomationTestCaseSupport{
    
    @Test
    public void test() {
        // 通过点击，进入功能模块
        click(By.xpath("//a[@id='15']"));
        click(By.xpath("//a[@id='20']"));

        //获取table中的所有操作前的tr
        List<WebElement> beforeTrs = findElements(By.xpath("//table//tbody//tr"));
        //断言所有tr是否等于期望值
        assertEquals(beforeTrs.size(), 2);

        //打开添加页面
        click(By.xpath("//a[@href='"+ LaunchJetty.DEFAULT_CONTEXT_PATH + "/variable/dictionary-category/edit']"));
        //填写表单
        input(By.xpath("//form[@id='edit-dictionary-category-form']//input[@name='code']"), "test");
        input(By.xpath("//form[@id='edit-dictionary-category-form']//textarea[@name='remark']"),"这是一个测试保存的字典类别的记录");
        //提交表单，页面验证不通过
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
        //设置最后的值
        input(By.xpath("//form[@id='edit-dictionary-category-form']//input[@name='name']"), "test");
        //验证通过，提交表单
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        //返回成功信息
        String message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("保存成功"));

        //点击编辑功能
        findElement(By.xpath("//table//tbody//tr//*[text()='test']//..//a")).click();
        //填写表单
        input(By.xpath("//form[@id='edit-dictionary-category-form']//input[@name='name']"), "modify");
        input(By.xpath("//form[@id='edit-dictionary-category-form']//input[@name='code']"), "modify");
        input(By.name("remark"),"");
        //提交表单
        click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));

        //返回成功信息
        message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("保存成功"));

        //选中删除的记录
        check(By.xpath("//table//tbody//tr//*[text()='modify']//..//input"));
        //提交删除表单
        click(By.xpath("//div[@class='panel-footer']//*[@type='button']"));
        click(By.xpath("//div[@class='bootbox modal fade bootbox-confirm in']//*[@data-bb-handler='confirm']"));

        //返回成功信息
        message = findElement(By.className("alert")).getText();
        assertTrue(message.contains("删除1条信息成功"));

        //打开查询框
        click(By.xpath("//div[@class='panel-footer']//*[@data-toggle='modal']"));
        // 等待显示查询框
        wait(ExpectedConditions.visibilityOfElementLocated(By.id("search-modal")));

        //设置查询条件值
        input(By.id("name"), "状态");
        input(By.id("code"), "state");

        //查询
        click(By.xpath("//div[@class='modal-footer']//button[@type='submit']"));

        List<WebElement> aflterTrs = findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
        //断言查询后的记录数
        assertEquals(aflterTrs.size(), 1);
    }
    
}
