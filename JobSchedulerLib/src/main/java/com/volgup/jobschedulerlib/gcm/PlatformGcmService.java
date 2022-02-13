/*
 * Copyright (C) 2018 Evernote Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.volgup.jobschedulerlib.gcm;

import androidx.annotation.RestrictTo;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.volgup.jobschedulerlib.Job;
import com.volgup.jobschedulerlib.JobManager;
import com.volgup.jobschedulerlib.JobManagerCreateException;
import com.volgup.jobschedulerlib.JobProxy;
import com.volgup.jobschedulerlib.JobRequest;
import com.volgup.jobschedulerlib.util.JobCat;
//import com.google.android.gms.gcm.GcmNetworkManager;
//import com.google.android.gms.gcm.GcmTaskService;
//import com.google.android.gms.gcm.TaskParams;

/**
 * @author VolgUpTech
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class PlatformGcmService extends GcmTaskService {

    private static final JobCat CAT = new JobCat("PlatformGcmService");

    @Override
    public int onRunTask(TaskParams taskParams) {
        int jobId = Integer.parseInt(taskParams.getTag());
        JobProxy.Common common = new JobProxy.Common(this, CAT, jobId);

        JobRequest request = common.getPendingRequest(true, true);
        if (request == null) {
            return GcmNetworkManager.RESULT_FAILURE;
        }

        Job.Result result = common.executeJobRequest(request, taskParams.getExtras());
        if (Job.Result.SUCCESS.equals(result)) {
            return GcmNetworkManager.RESULT_SUCCESS;
        } else {
            return GcmNetworkManager.RESULT_FAILURE;
        }
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();

        /*
         * When the app is being updated, then all jobs are cleared in the GcmNetworkManager. The manager
         * calls this method to reschedule. Let's initialize the JobManager here, which will reschedule
         * jobs manually.
         */
        try {
            JobManager.create(getApplicationContext());
        } catch (JobManagerCreateException ignored) {
        }
    }
}
