package cap5.controller;

import cap5.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class TestController {
	@Autowired
	private TestService testService;
}
