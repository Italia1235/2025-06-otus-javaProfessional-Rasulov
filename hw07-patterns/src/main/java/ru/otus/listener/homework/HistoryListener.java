package ru.otus.listener.homework;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.listener.Listener;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long,Message> messagesRepo = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ListenerPrinterConsole.class);

    @Override
    public void onUpdated(Message msg) {
        Message copy = msg.toBuilder().build();
        messagesRepo.put(copy.getId(), copy);
        logger.info("oldMsg:{}", msg);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
       Message message=  messagesRepo.get(id);
       return Optional.of(message);
    }
}
