package com.jt.order.job;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.jt.order.mapper.OrderMapper;

public class PaymentOrderJob extends QuartzJobBean {

	/**
	 * 1.从上下文中获取Spring容器
	 * 2.从Spring容器中获取OrderMapper对象
	 * 3.执行我的Mapper操作，将超时订单状态设置为6
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 获取Spring容器
		ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail()
				.getJobDataMap().get("applicationContext");
		// 获取具体Mapper
		OrderMapper orderMapper = applicationContext.getBean(OrderMapper.class);
		
		// 执行Mapper操作
		// 计算超时时间.2天前
		Date agoDate = new DateTime().minusDays(2).toDate();
		orderMapper.updateState(agoDate);
		System.out.println("定时完成");
	}

}
