package com.teerenzo.socialMedia.helloWorld;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWroldController {
	
	@RequestMapping(path="/hell-world", method= RequestMethod.GET)
	public helloWorldBean helloWorld() {
		return new helloWorldBean("Hello world");
	}
	
	@RequestMapping(path="/hell-world/path/{name}", method= RequestMethod.GET)
	public helloWorldBean helloWorldPath(@PathVariable String name) {
		return new helloWorldBean(String.format("Hello %s", name));
	}

}
