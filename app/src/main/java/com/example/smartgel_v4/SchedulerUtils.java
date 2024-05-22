package com.example.smartgel_v4;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

public class SchedulerUtils {

    private static final int JOB_ID = 123;

    public static void scheduleJob(Context context) {
        ComponentName componentName = new ComponentName(context, BornePollingJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(15 * 60 * 1000); // 15 minutes
            builder.setOverrideDeadline(15 * 60 * 1000); // 15 minutes
        } else {
            builder.setPeriodic(15 * 60 * 1000); // 15 minutes
        }

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
       int result= jobScheduler.schedule(builder.build());
        Log.d("SchedulerUtils", "Job scheduled with result: " + result);
    }
}
