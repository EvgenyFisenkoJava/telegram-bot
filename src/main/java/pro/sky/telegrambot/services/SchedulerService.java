package pro.sky.telegrambot.services;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.util.Collection;

@Service
public class SchedulerService {
    private final NotificationTaskRepository notificationTaskRepository;

    public SchedulerService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public Collection<NotificationTask> checkNotifications() {
        return notificationTaskRepository.findAllByNotificationSendTime();
    }
}

