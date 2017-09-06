package com.example.oleg.startandroidtests.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.example.oleg.startandroidtests.R;

import java.util.concurrent.TimeUnit;

//!!!Также работа с нотификациями из сервиса описана в L99ServiceAndNotifications и L99Service

public class TestNotificationsActivity extends AppCompatActivity {

    //Идентификатор уведомления. Он нужен, чтобы можно было различать уведомления друг от друга.
    //Если у вас будет один идентификатор, то каждое новое уведомление затрёт предыдущее.
    private static final int NOTIF_ID1 = 1;
    private static final int NOTIF_ID2 = 2;
    private static final int NOTIF_ID3 = 3;
    private static final int NOTIF_ID4 = 4;
    private static final int NOTIF_ID5 = 5;
    private static final int NOTIF_ID6 = 6;
    private static final int NOTIF_ID7 = 7;
    private static final int NOTIF_ID8 = 8;
    private static final int NOTIF_ID9 = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notifications);
    }

    public void test1(View view) {
        //Нам нужны объекты Intent и PendingIntent, которые описывают намерения и целевые действия.
        //В нашем случае мы хотим запустить нашу активность, когда пользователь среагирует на уведомление.
        Intent intent = new Intent(this, TestNotificationsActivity.class);

        //Не сразу бывает заметно, но на самом деле, когда при нажатии на уведомлении у вас запускается активность, то запускается не старая активность, которая была на экране до этого, а новая.
        //Это можно увидеть в примере, если, например, есть текстовое поле с текстом. Введите какой-нибудь текст в активности, а потом создайте уведомление, вызывающее активность.
        //Вы увидите, что запустится новая активность с пустыми текстовым полем, хотя мы ожидали увидеть запущенную активность. Если вам нужен именно этот вариант, то используйте флаги для намерения.
        //Либо вы можете прописать в манифесте для нужной активности атрибут android:launchMode="singleTop".
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //Варианты интент:
        //Открыть сайт - new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.alexanderklimov.ru/android/"));
        //Ничего не делать - new Intent();

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Далее формируется внешний вид и поведение уведомления через построитель Notification.Builder.
        //Обязательные: setSmallIcon(), setContentTitle() и setContentText()
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder
                //устанавливаем PendingIntent для обработки нажатия на уведомление
                .setContentIntent(pIntent)

                //устанавливаем маленький значок, который выводится в строке состояния, а также в правой части открытого уведомления.
                //в андроид 5.0 и выше значек будет белым
                //на андроид 4 при использовании векторного значка R.drawable.ic_sync_black_24dp почему-то вылет
                .setSmallIcon(R.drawable.google_drive_icon)

                //устанавливаем большой значок, который выводится в открытом уведомлении слева.
                //Если не указать, то используется маленький значок, растягивая его до нужных размеров.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.google_drive_icon))

                //выводит временную строку в строке состояния, которая появится, а затем исчезает. Остаётся только маленький значок. С андроид 5.0 не срабатывает.
                //Внимание, если этот метод не использовать, то также не будет анимации setSmallIcon, если она анимирована
                .setTicker("This is TICKER.")

                //Установить заголовок уведомления
                .setContentTitle("This is TITLE.")

                //Установить текст уведомления
                .setContentText("This is notification TEXT.")

                //Установить subtext
                .setSubText("This is notification SUBTEXT.")

                //установить поле время, для уведомления, т.е. в уведомлении появится строчка со временем этого сообщения
                .setWhen(System.currentTimeMillis())

                //установить звук уведомления
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                //установить вибрацию (задержка-вибро-задержка-вибро и т.д. в миллисекундах)
                //Прежде чем использовать виброзвонок в своем приложении, необходимо получить нужные полномочия,
                //прописав их в манифесте: <uses-permission android:name="android.permission.VIBRATE"/>
                .setVibrate(new long[] {0L, 1000L, 200L, 1000L})

                //Включить LED-индикатор (если есть)
                .setLights(Color.RED, 1, 1)

                //true - поместить уведомление в секцию постоянных (текущее уведомление), т.е. его нельзя смахнуть или очистить. Нужно, напр., если показываем статус загрузки и т.п.
                //то же самое можно сделать вручную добавив флаг notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
                .setOngoing(false)

                //Чем выше приоритет уведомления, тем выше он находится среди остальных уведомлений. Таким образом, важные сообщения всегда будут наверху, даже если поступили позже других менее важных сообщений.
                //Не злоупотребляйте этой возможностью и трезво оцените важность вашего уведомления.
                .setPriority(Notification.PRIORITY_HIGH)

                //информирует систему о том, как обрабатывать уведомления вашего приложения, когда устройство находится в режиме приоритета
                //(например, если ваше уведомление сообщает о входящем вызове, мгновенном сообщении или сигнале будильника)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)

                //позволяет добавить к уведомлению список людей. С его помощью ваше уведомление может сигнализировать системе о том,
                //что она должна сгруппировать уведомления от указанных людей или считать уведомления от этих людей более важными.
                //.addPerson("")        //до конца непонятно что этот метод делает

                //Настройка видимости на экране блокировки. Ваше приложение может определять объем информации, отображаемой в уведомлениях, которые выводятся на экране блокировки.
                //VISIBILITY_PUBLIC показывает полное содержимое уведомления;
                //VISIBILITY_SECRET не отображает какую-либо часть этого уведомления на экране блокировки;
                //VISIBILITY_PRIVATE показывает базовую информацию, такую как значок уведомления и заголовок его содержимого, но скрывает полное содержимое уведомления.
                //  Когда задается значение VISIBILITY_PRIVATE, вы также можете предоставить альтернативную версию содержимого уведомления, в который скрыты некоторые сведения.
                //  Чтобы предоставить возможность такого альтернативного уведомления, сначала создайте его с помощью NotificationCompat.Builder.
                //  При создании частного объекта уведомления прикрепите к нему альтернативное уведомление, воспользовавшись методом setPublicVersion() .
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)

                //скрыть уведомление при нажатии на него (Notification.FLAG_AUTO_CANCEL)
                .setAutoCancel(true);

        //Генерируем уведомление с помощью билдера
        Notification notification = builder.build();

        //Далее надо сформировать уведомление с помощью специального менеджера. Ссылку на NotificationManager можно получить через вызов метода getSystemService(),
        //передав ему в качестве параметра строковую константу NOTIFICATION_SERVICE, определённую в классе Context
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Обратите также внимание на упрощённый вариант доступа к менеджеру уведомлений через метод NotificationManagerCompat.from(this).
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        //Выводится уведомление с помощью метода notify() - своеобразный аналог метода show() у Toast из предыдущего урока.
        //Здесь указываем ID уведомления и само уведомление. Если вам нужно обновить уведомление, то просто ещё раз отправьте его устройству под этим же ID.
        notificationManager.notify(NOTIF_ID1, notification);
    }




    //***************Прогресс бар в уведомлениях*******************************
    public void test2(View view) {
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        //создаем базовую часть уведомления
        final Notification.Builder builder = new Notification.Builder(this)
                //Для анимированого значка используем соответствующий drawable (animation-list). В этом примере используем стандартный.
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("Download")      //если не задать этот метод, то анимация не работает
                .setContentTitle("TestNotificationActivity")
                .setOngoing(true)           //поскольку мы показываем статус выполнения, то поместим уведомление в постоянные, чтобы его нельзя было очистить
                .setContentText("Current progress:");

        //через промежутки времени будем повышать прогресс и обновлять уведомление с нашим ID
        new Thread(new Runnable() {
            final int PROGRESS_MAX = 10;

            @Override
            public void run() {
                for(int i = 0; i <= PROGRESS_MAX; i++) {
                    builder.setProgress(PROGRESS_MAX, i, i == 0 ? true : false);
                    //обновляем уведомление с нашим ID
                    notificationManager.notify(NOTIF_ID2, builder.build());
                    //Задержка
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //по окончании скроем наше уведомление
                notificationManager.cancel(NOTIF_ID2);
            }
        }).start();
    }




    //**************************Defaults**********************************
    public void test3(View view) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_sync_black_24dp)
                .setContentText("This is notification with default vibrate, sound and led-lights")
                //Устанавливаем системные значения звука, вибрации и LED-индикатора (для задания всех, как здесь, лучше использовать Notification.DEFAULT_ALL)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        notificationManager.notify(NOTIF_ID3, builder.build());
    }




    //***********Установка значений через поля**********************
    public void test4(View view) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_sync_black_24dp)
                .setContentText("Set notification fields");
        Notification notification = builder.build();

        //Вибро (<uses-permission android:name="android.permission.VIBRATE"/>)
        notification.vibrate = new long[] {0L, 1000L, 200L, 1000L};
        //Звук. Пример: Uri.parse("file:///sdcard/cat.mp3");
        //Чтобы использовать какую-либо из стандартных мелодий, используем Content Provider:
        notification.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6");
        //Светодиод (если есть)
        notification.ledARGB = Color.RED;       //цвет
        notification.ledOffMS = 0;              //The number of milliseconds for the LED to be off while it's flashing.
        notification.ledOnMS = 1;               //The number of milliseconds for the LED to be on while it's flashing.
        //Настроив работу со светодиодами, необходимо также добавить флаг FLAG_SHOW_LIGHTS к свойству flags объекта Notification.
        notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;
        notificationManager.notify(NOTIF_ID4, notification);

        //Другое:

        //поместить в секцию постоянных
        //notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;

        //Настойчивые уведомления непрерывно повторяют звуковые сигналы, вибрируют и мерцают светодиодами, пока не будут остановлены.
        //Подобные уведомления, как правило, используются для событий, которые требуют немедленного и своевременного внимания, таких как входящий звонок, срабатывание будильника
        //notification.flags = notification.flags | Notification.FLAG_INSISTENT;
    }




    //***************Уведомление с кнопками********************************************************
    //В уведомлениях можно размещать до трёх кнопок. Это может быть удобным, если приложение состоит из нескольких активностей или нужно предложить три разных варианта развития сценария.
    //За появление кнопок в уведомлении отвечает метод setAction().
    public void test5(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Намерение для запуска второй активности
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Строим уведомление
        Notification builder = new Notification.Builder(this)
                .setTicker("Пришла посылка!")
                .setContentTitle("Посылка")
                .setContentText("Это я, почтальон Печкин. Принес для вас посылку")
                .setSmallIcon(R.drawable.google_drive_icon)
                .setContentIntent(pIntent)
                .addAction(android.R.drawable.ic_menu_save, "Открыть", pIntent)
                .addAction(android.R.drawable.ic_menu_save, "Отказаться", pIntent)
                .addAction(android.R.drawable.ic_menu_save, "Другой вариант", pIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(NOTIF_ID5, builder);
    }




    //*****************Уведомление с длинным текстом*****************************
    //Если вы внимательно смотрели на уведомление, то могли увидеть, что длинный текст, помещённый в метод setContentText(), вывелся на экран не полностью.
    //Если информация слишком важная и вам хочется её показать в уведомлении полностью, то вам подойдёт следующий вариант:
    public void test6(View view) {
        String bigText = "Здесь следует обратить внимание на следующий момент. В первом примере переменная builder была объектом типа Notification. Во втором примере мы разбили построение уведомления на две части. Настройка самого уведомления происходит в объекте типа Notification.Builder (сам код остался без изменений), а стиль уведомления задаётся уже для объекта типа Notification. В данном случае мы используем стиль BigTextStyle().bigText(). В этом случае текст в setContentText() игнорируется, а вместо него используется отдельно заданный нами текст в строковой переменной bigText.";

        Notification.Builder builder = new Notification.Builder(this)
                .setTicker("Пришла посылка!")
                .setContentTitle("Уведомление с большим текстом")
                .setContentText("Это я, почтальон Печкин. Принес для вас посылку")
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setAutoCancel(true);

        //Настройка самого уведомления происходит в объекте типа Notification.Builder (сам код остался без изменений),
        //а стиль уведомления задаётся уже для объекта типа Notification. В данном случае мы используем стиль BigTextStyle().bigText().
        //В этом случае текст в setContentText() игнорируется, а вместо него используется отдельно заданный нами текст в строковой переменной bigText.

        //!!!В хелпе указан немного другой вариант задания стиля, указал ниже в InboxStyle
        Notification notification = new Notification.BigTextStyle(builder)
                .bigText(bigText)
                .build();

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIF_ID6, notification);
    }




    //**********************Уведомление с большой картинкой*****************************************
    //Пример с большой картинкой аналогичен с предыдущим примером. Только мы задаём уже другой стиль для уведомления. Вместо стиля длинного текста используется стиль BigPictureStyle().bigPicture():
    public void test7(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Большая посылка")
                .setTicker("Пришла посылка!")
                .setContentText("Уведомление с большой картинкой")
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_save, "Запустить активность", pIntent);

        // Подготовим большую картинку
        Notification notification = new Notification.BigPictureStyle(builder)
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.nature))
                .build();

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIF_ID7, notification);
    }




    //*************************Уведомление в стиле Inbox*******************************************
    //Есть ещё один стиль InboxStyle, напоминающий стиль писем в папке Входящие.
    //Чтобы уведомление отображалось в расширенном виде, сначала создайте объект NotificationCompat.Builder с требуемыми параметрами обычного представления.
    //Затем вызовите метод Builder.setStyle(), первым аргументом которого должен быть объект расширенного макета.
    //Помните, что расширенные уведомления не поддерживаются на платформах версии более ранней, чем Android 4.1.
    public void test8(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Пришла посылка!")
                .setContentTitle("Уведомление в стиле Inbox")
                .setContentText("Inbox Style notification!!")
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .addAction(android.R.drawable.ic_menu_save, "Запустить активность", pIntent)
                .setAutoCancel(true);

        //Определяем стиль InboxStyle (этот способ, в отличии от предыдущих, указан в хелпе)
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Big content title")
                .addLine("Первое сообщение").addLine("Второе сообщение")
                .addLine("Третье сообщение").addLine("Четвертое сообщение")
                .setSummaryText("+ еще 5 сообщений");

        //Присваиваем стиль в билдер
        builder.setStyle(inboxStyle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIF_ID8, builder.build());
    }



    //https://developer.android.com/guide/topics/ui/notifiers/notifications.html
    //!!!Back stack не работает непонятно почему.

    //Можно выделить два типа активити, при запуске из уведомлений:
    //Обычная - вы запускаете операцию Activity, которая является частью обычного потока работы приложения.
    //  В этом случае настройте объект PendingIntent на запуск новой задачи и предоставьте объекту PendingIntent стек переходов назад,
    //  который воспроизводит обычную работу приложения в ситуации, когда пользователь нажимает "Назад".
    //  Т.е. как-будто мы попали в нужное активити не из уведомления, а заходили по порядку вызовов в приложении.
    //Особая - Пользователь может увидеть эту операцию Activity, только если она запущена из уведомления.
    //  В некотором смысле операция Activity расширяет уведомление путем предоставления информации, которую было бы сложно отобразить в самом уведомлении.
    //  В этом случае настройте объект PendingIntent на запуск в новой задаче. При этом создавать стек переходов назад не требуется,
    //  поскольку запущенная операция Activity не является частью потока операций приложения. При нажатии "Назад" пользователь все же перейдет на главный экран.
    public void test9(View view) {
        //Настройка PendingIntent обычной операции

        //описываем интент для запуска нужной активити
        Intent resultIntent = new Intent(this, TestCoordinatorLayout.class);
        //Создаем обьект TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //Добавляем в TaskStackBuilder стек переходов назад
        stackBuilder.addParentStack(L92ServiceActivity.class);
        //Добавляем ранее созданый интент для запуска целевой активити
        stackBuilder.addNextIntent(resultIntent);
        //получаем PendingIntent из TaskStackBuilder методом getPendingIntent()
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Далее как обычно
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(resultPendingIntent)
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setContentTitle("Title")
                .setContentText("Notification text");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID9, builder.build());
    }


    //************************Нестандартные макеты уведомлений**************************************
    //Высота изображения, которую можно получить с использованием нестандартного макета уведомления, зависит от представления уведомления.
    //Обычные макеты представления ограничены по высоте 64dp, а расширенные — 256dp.
    public void test10(View view) {
        //Вобщем с RemoteViews нужно разбираться, что оно такое. Здесь я просто указал layout файл, а че с ним дальше делать пока не знаю
        RemoteViews rv = new RemoteViews(getResources().getResourcePackageName(R.layout.layout_custom_notification_1), R.layout.layout_custom_notification_1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //Поместите этот объект RemoteViews в свой NotificationCompat.Builder, вызвав метод setContent().
        //Не следует задавать фон Drawable для объекта RemoteViews, поскольку цвет текста может стать нечитаемым.
        builder.setContent(rv)
                .setSmallIcon(android.R.drawable.ic_menu_save);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIF_ID7, builder.build());
    }

}
/*
Подводя итоги, следует отметить, у уведомлений очень много методов, которые можно использовать в своём приложении. Вот как может выглядеть полный набор:
new Notification.Builder(this.getApplicationContext())
        .setAutoCancel(boolean autoCancel)
        .setContent(RemoteViews views)
        .setContentInfo(CharSequence info)
        .setContentIntent(PendingIntent intent)
        .setContentText(CharSequence text)
        .setContentTitle(CharSequence title)
        .setDefaults(int defaults)
        .setDeleteIntent(PendingIntent intent))
        .setFullScreenIntent(PendingIntent intent, boolean highPriority)
        .setLargeIcon(Bitmap icon)
        .setLights(int argb, int onMs, int offMs)
        .setNumber(int number)
        .setOngoing(boolean ongoing)
        .setOnlyAlertOnce(boolean onlyAlertOnce)
        .setPriority(int pri)
        .setProgress(int max, int progress, boolean indeterminate)
        .setShowWhen(boolean show)
        .setSmallIcon(int icon, int level)
        .setSmallIcon(int icon)
        .setSound(Uri sound)
        .setSound(Uri sound, int streamType)
        .setStyle(Notification.Style style)
        .setSubText(CharSequence text)
        .setTicker(CharSequence tickerText, RemoteViews views)
        .setTicker(CharSequence tickerText)
        .setUsesChronometer(boolean b)
        .setVibrate(long[] pattern)
        .setWhen(long when)
        .addAction(int icon, CharSequence title, PendingIntent intent)
        .build()
 */
