/**
 * Copyright (c) 2015, Blackboard Inc. All Rights Reserved.
 */
package com.xpay.pay;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

/**
 * ClassName: BaseMapperTest
 *
 * @Author: lhjiang
 * @Date: Aug 14, 2015 9:42:09 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContextConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public abstract class BaseSpringJunitTest {

}
