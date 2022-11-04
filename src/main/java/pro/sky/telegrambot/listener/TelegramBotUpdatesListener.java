package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.services.SchedulerService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final NotificationTaskRepository notificationTaskRepository;

    private final TelegramBot telegramBot;
    private final SchedulerService schedulerService;

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot, SchedulerService schedulerService) {

        this.notificationTaskRepository = notificationTaskRepository;

        this.telegramBot = telegramBot;

        this.schedulerService = schedulerService;
    }

    public static Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


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


            Matcher matcher = pattern.matcher(message.text());

            if (message.text().equals("/start")) {
                SendMessage msg = new SendMessage(chatId,
                        "Hi there!");
                telegramBot.execute(msg);
            }

            if (matcher.matches()) {
                String date = matcher.group(1);
                String text = matcher.group(3);
                LocalDateTime startMessage = LocalDateTime.parse(date, dateTimeFormatter);
                NotificationTask notificationTask = new NotificationTask(message.messageId(), chatId, text, startMessage);
                notificationTaskRepository.save(notificationTask);
            }

        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotification() {
        logger.info("sending notifications for the current time");

        for (NotificationTask notificationTask : schedulerService.checkNotifications()) {
            Long messageId = notificationTask.getChatId();
            String messageText = notificationTask.getMessage();
            SendMessage sendMessage = new SendMessage(messageId, messageText);

            telegramBot.execute(sendMessage);
        }
    }
}






