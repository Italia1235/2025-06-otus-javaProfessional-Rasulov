package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorException implements Processor {
    TimeProvider localDateTime;

    public ProcessorException() {
        this.localDateTime = new SystemTimeProvider();
    }

    public ProcessorException(TimeProvider timeProvider) {
        this.localDateTime = timeProvider;
    }

    @Override
    public Message process(Message message) {

        if (localDateTime.now().getSecond() % 2 == 0) {

            throw new RuntimeException("COLLAPSE!");
        } else return message;
    }


}
