package ru.otus.cachehw;

import ru.otus.crm.model.Client;

public interface HwListener<K, V> {
    void notify(K key, V value, String action);

}
