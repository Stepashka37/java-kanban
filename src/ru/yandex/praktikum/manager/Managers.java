/**
 * Утилитарный класс, в котором реализована логика по созданию менеджера задач и менеджера истории
 */
package ru.yandex.praktikum.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.praktikum.LocalDateTimeAdapter;
import ru.yandex.praktikum.LocalDateTimeDeserializer;
import ru.yandex.praktikum.LocalDateTimeSerializer;
import ru.yandex.praktikum.http.HttpTaskManager;
import ru.yandex.praktikum.server.KVServer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    /** Метод для создания нового объекта класса InMemoryTaskManager */
    public static TaskManager getDefault() throws IOException {
        TaskManager manager = new HttpTaskManager(KVServer.PORT);
        return manager;
    }

    /** Метод для создания нового объекта класса InMemoryHistoryManager */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static KVServer getDefaultKVServer() throws IOException {
        final KVServer kvServer = new KVServer();
        kvServer.start();
        return kvServer;
    }

    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        return gsonBuilder.create();
    }

}
