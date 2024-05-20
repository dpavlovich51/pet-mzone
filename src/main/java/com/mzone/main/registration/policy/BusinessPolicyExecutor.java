package com.mzone.main.registration.policy;

//// TODO: 07/06/2022 refactor it to abstract class and add logger for executed method

/**
 * @param <T> - response (response event)
 * @param <E> - request (request event)
 */
public interface BusinessPolicyExecutor<T, E> {
    T tryExecute(E event);
}
