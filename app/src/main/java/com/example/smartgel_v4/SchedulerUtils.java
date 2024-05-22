package com.example.smartgel_v4;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

public class SchedulerUtils {
    private static final int JOB_ID = 1;
   // private static final long INTERVAL = 10 * 60 * 1000; // 10 minutes
   private static final long INTERVAL = 15 * 60 * 1000; // 15 minutes
   // private static final long INTERVAL = 5 * 60 * 1000; // 5 minutes

    public static void scheduleJob(Context context) {
        ComponentName componentName = new ComponentName(context, BornePollingJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(INTERVAL)
                .setPersisted(true) // To persist across reboots
                .build();

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }
}
