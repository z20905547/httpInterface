package vfh.httpInterface.web.httpjson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import vfh.httpInterface.service.account.AccountService;

@Controller
@RequestMapping("/jsondata/account")
public class AccountController {
	@Autowired
    private AccountService accountService;
	
    
}
