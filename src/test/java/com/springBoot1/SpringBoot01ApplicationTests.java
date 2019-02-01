package com.springBoot1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.springBoot1.bean.Dog;
import com.springBoot1.bean.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot01ApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(SpringBoot01ApplicationTests.class);

	@Autowired
	Person person;
	@Autowired
	ApplicationContext ctx;
	
	@Test
	public void contextLoads() {
		System.out.println(person);
		Person person2 = (Person)ctx.getBean("psn");
		System.out.println("person2:"+person2);
		
		Dog dog1 = (Dog)ctx.getBean("dog1");
		System.out.println("dog1   :"+ dog1);
		
		log.trace("1");
		log.debug("2");
		log.info("3 ...");
		log.warn("4 ...");
		log.error("5");
	}

}

