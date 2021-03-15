package ru.coach2me.csv_parser.services;

import org.springframework.stereotype.Service;

@Service
public class ThreadsService {
    public void submit(Runnable task) {
        Thread thread = new Thread(task);
        thread.start();
    }
}
