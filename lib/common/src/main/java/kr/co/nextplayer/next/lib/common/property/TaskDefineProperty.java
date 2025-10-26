package kr.co.nextplayer.next.lib.common.property;

import lombok.*;
import org.quartz.Job;
import org.quartz.JobKey;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDefineProperty {
    private JobKey jobKey;

    private String description;

    private String cronExpression;

    private String misfireHandlingType;

    private Map<?, ?> jobDataMap;

    private Class<? extends Job> jobClass;

    public TaskDefineProperty(String jobName, String jobGroup, String description, String cronExpression, Map<?, ?> jobDataMap, Class<? extends Job> jobClass) {
        this.jobKey = getJobKey(jobName, jobGroup);
        this.description = description;
        this.cronExpression = cronExpression;
        this.jobDataMap = jobDataMap;
        this.jobClass = jobClass;
    }

    public TaskDefineProperty(String jobName, String jobGroup, String cronExpression, Map<?, ?> jobDataMap) {
        this.jobKey = getJobKey(jobName, jobGroup);
        this.cronExpression = cronExpression;
        this.jobDataMap = jobDataMap;
    }

    public JobKey getJobKey(String jobName, String jobGroup) {
        if (jobName != null && jobGroup != null) {
            return new JobKey(jobName, jobGroup);
        }
        return null;
    }
}
