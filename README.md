#Использование

Приложение CloudPayments_AndroidSDKDemo демонстрирует работу SDK для платформы Android с платежным шлюзом CloudPayments
Полная информация об использовании на сайте http://cloudpayments.ru/docs/mobileSDK

##Установка

git clone https://github.com/cloudpayments/CloudPayments_AndroidSDKDemo.git

##Описание работы приложения с SDK CloudPayments

SDK CloudPayments (CloudPaymentsAPI.framework) позволяет:
* проводить проверку карточного номера на корректность  
```    
ru.cloudpayments.sdk.ICard card = ru.cloudpayments.sdk.CardFactory.create(java.lang.String number);
boolean card.isValidNumber();
```
* определять тип платежной системы
```
ru.cloudpayments.sdk.ICard card = ru.cloudpayments.sdk.CardFactory.create(java.lang.String number);
java.lang.String card.getType();
```
* шифровать карточные данные и создавать криптограмму для отправки на сервер
```
ru.cloudpayments.sdk.ICard card = ru.cloudpayments.sdk.CardFactory.create(java.lang.String number);
java.lang.String card.cardCryptogram(java.lang.String publicId) throws                                                      java.io.UnsupportedEncodingException, javax.crypto.NoSuchPaddingException, java.security.NoSuchAlgorithmException,                   java.security.NoSuchProviderException, javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException,                     java.security.InvalidKeyException;
```
##Проведение оплаты

###Пример использования SDK и API CloudPayments 

К проекту нужно подлючить следующие зависимости:

```
libs/CloudPayments_AndroidSDK.aar // CloudPayments Sdk
org.codehaus.jackson:jackson-core-asl:1.9.11
org.codehaus.jackson:jackson-jaxrs:1.9.11
org.codehaus.jackson:jackson-mapper-asl:1.9.11
```

подробнее см. build.gradle.

В примере publicId это тестовые реквизиты для подключения, Вам нужно получить их в личном кабинете на сайте CloudPayments.

####Пример отправки запроса на списание средств с банковской карты через 3ds, через встроенную форму:
```
        Intent intent = new Intent(Launcher.this, PaymentWidget.class);
        PaymentWidget.listener = chargeTaskListener;
        intent.putExtra("amount", amount);
        intent.putExtra("desc", desc);
        intent.putExtra("currency", currency);
        intent.putExtra("publicId", publicId);
        intent.putExtra("invoiceId", invoiceId);
        intent.putExtra("accountId", accountId);
        startActivity(intent);
```

где
```        
        private ChargeTaskListener chargeTaskListener = new ChargeTaskListener() {
                @Override
                public void success(BaseResponse baseResponse) {
                    // успешно 
                    // baseResponse instanceof CardsAuthConfirmResponse - оплата 3ds
                    // baseResponse instanceof CardsAuthResponse
                }
        
                @Override
                public void error(BaseResponse baseResponse) {
                    // ошибка
                }
        
                @Override
                public void cancel() {
                    // отменено пользователем
                }
            };
```

Описание успешного ответа
```
        public class CardsAuthConfirmResponse extends BaseResponse {
            public CardTransaction transaction;
        }
        
        public class CardsAuthResponse extends BaseResponse {
            public CardAuth auth;
        }
        
        public class CardTransaction {
            public int transactionId;        // Id операции
            public String cardHolderMessage; // Сообщение о операции
        }
        
        public class CardAuth extends CardTransaction {
            public int transactionId;        // Id операции
            public String cardHolderMessage; // Сообщение о операции
        }
```
Подробнее см. ru.cloudpayments.sdk.demo.buildIn.BuildInActivity.java

####Пример отправки запроса на списание средств с банковской карты через 3ds, через свою форму:

```
        ICard card = CardFactory.create(cardNumber, expDate, cvv);
        if (card.isValidNumber()) {
            ICharge charge = ChargeFactory.create(CustomActivity.this,
                            Constants.publicId, "accId", "invId",
                            card.cardCryptogram(Constants.publicId),
                            holderName, amount, "RUB", desc,
                            "http://example.ru");
            charge.run(chargeTaskListener);
        } else {
            //CardNumber is not valid
        }
```
Подробнее см. ru.cloudpayments.sdk.demo.custom.CustomActivity.java

##Ключевые моменты

В демо-проекте частично используется код из библиотеки https://github.com/LivotovLabs/3DSView. Все права на код этой библиотеки принадлежат авторам библиотеки.
