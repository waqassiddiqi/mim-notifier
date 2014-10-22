package mim.notifier;

import java.util.ResourceBundle;

import mim.provgw.Client;
import mim.renewal.task.NotifyInActiveSubscriberTask;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class NotifierDaemon {

	private Logger log = Logger.getLogger(getClass().getName());
	private int cycleStart = 16;
	
	protected void readConfig() {
		
		log.info("Initializing NotifierDaemon ...");
		
		ResourceBundle myResources = ResourceBundle.getBundle("notify");
		
		cycleStart = Integer.parseInt(myResources.getString("notify.start_hour"));
		Client.provGwIP = myResources.getString("provgw.ip");
		Client.provGwPort = Integer.parseInt(myResources.getString("provgw.port"));
	}
	
	public void startDaemon() throws SchedulerException {
		
		readConfig();
		
		JobDetail jobNotify = JobBuilder.newJob(NotifyInActiveSubscriberTask.class)
				.withIdentity("notifyInActive", "group1")
				.build(); 
		
		Trigger dailyNotifyTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("notifyTrigger", "group1")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 " + cycleStart + " * * ?"))
				.startNow()
				.build();
		
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(jobNotify, dailyNotifyTrigger);
    	
    	scheduler.triggerJob(jobNotify.getKey());
    	
    	log.info("NotifierDaemon has started...");
    	    	
    	log.info("NotifyInActiveDeamon will next execute on: " + dailyNotifyTrigger.getNextFireTime());
	}
	
	public static void main(String[] args) throws SchedulerException {
		new NotifierDaemon().startDaemon();
	}
}