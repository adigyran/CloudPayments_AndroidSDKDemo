package ru.cloudpayments.sdk.demo;

import java.lang.String; /**
 * Created by Nastya on 29.09.2014.
 */
public interface CardDataViewListener {

    void makePayment(String cardNumber, String expDate, String cvv, String holderName, double amount, String desc);

}
