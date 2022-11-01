package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final NotificationTaskRepository notificationTaskRepository;
    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository) {

        this.notificationTaskRepository = notificationTaskRepository;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            Message message = update.message();
            Long chatId = update.message().chat().id();

            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            Matcher matcher = pattern.matcher(message.text());

            if (message.text().equals("/start")) {
                SendMessage msg = new SendMessage(chatId,
                        "Hi there!");
                telegramBot.execute(msg);

            }
            if (matcher.matches()) {
                String date = matcher.group(1);
                String text = matcher.group(3);
                LocalDateTime startMessage = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                NotificationTask notificationTask = new NotificationTask(message.messageId(), chatId, text, startMessage);
                notificationTaskRepository.save(notificationTask);
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void checkNotification() {
        Collection<NotificationTask> messages = notificationTaskRepository.findAll()
                .stream().filter(n -> n.getNotificationSendTime()
                        .equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
                .collect(Collectors.toList());

        for (NotificationTask notificationTask : messages) {
            Long messageId = notificationTask.getChatId();
            String messageText = notificationTask.getMessage();
            SendMessage sendMessage = new SendMessage(messageId, messageText);


            telegramBot.execute(sendMessage);
        }

    }
}






