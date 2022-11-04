package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.NotificationTask;

import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    @Query(value = "SELECT * FROM notification_Task WHERE notification_send_time = date_trunc('minute', current_timestamp)", nativeQuery = true)
    List<NotificationTask> findAllByNotificationSendTime();

}
