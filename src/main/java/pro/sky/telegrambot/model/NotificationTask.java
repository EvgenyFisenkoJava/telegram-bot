package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    private Integer id;
    private Long chatId;
    private String message;
    private LocalDateTime notificationSendTime;

    public NotificationTask() {
    }


    public NotificationTask(Integer id, Long chatId, String message, LocalDateTime notificationSendTime) {
        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.notificationSendTime = notificationSendTime;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getNotificationSendTime() {
        return notificationSendTime;
    }

    public void setNotificationSendTime(LocalDateTime notificationSendTime) {
        this.notificationSendTime = notificationSendTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(message, that.message) && Objects.equals(notificationSendTime, that.notificationSendTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, message, notificationSendTime);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", notificationSendTime=" + notificationSendTime +
                '}';
    }
}

