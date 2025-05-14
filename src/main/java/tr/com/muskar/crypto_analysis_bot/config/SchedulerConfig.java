package tr.com.muskar.crypto_analysis_bot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig implements SchedulingConfigurer {

    /**
     * <p>Task yapılarını scheduled operasyonu
     * register oluşturulur.</p>
     *
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        int corePoolSize = 5;
        scheduler.setPoolSize(corePoolSize);
        scheduler.setThreadNamePrefix("Scheduler_");
        scheduler.initialize();
        scheduledTaskRegistrar.setTaskScheduler(scheduler);
    }
}