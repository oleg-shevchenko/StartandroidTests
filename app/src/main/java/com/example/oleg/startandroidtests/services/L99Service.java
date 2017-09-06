package com.example.oleg.startandroidtests.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.view.L99ServiceAndNotificationsActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/*
Для начала разберем уведомление на логические составляющие, чтобы проще было понять как его создавать и отправлять.
Первая часть – то, что видно в статус-баре, когда уведомление только приходит – иконка и текст. Текст потом исчезает и остается только иконка.
Вторая часть – то, что мы видим, когда открываем статус бар (тянем вниз). Там уже полноценный View с иконкой и двумя текстами, т.е. более подробная информация о событии.
Третья часть – то, что произойдет, если мы нажмем на View из второй части. Тут обычно идет вызов Activity, где мы можем просмотреть полную информацию и обработать событие.
*/

public class L99Service extends Service {

    NotificationManager nm;

    public L99Service() {}

    @Override
    public void onCreate() {
        super.onCreate();
        //Получаем экземпляр NotificationManager с помощью метода контекста getSystemService("notification")
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Имитируем загрузку файла
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendNotif();
        return START_STICKY;
    }

    void sendNotif() {
        //3-я часть - вызов Activity, где мы можем просмотреть полную информацию и обработать событие.

        //создаем Intent, который мы бы использовали для вызова нашего Activity
        Intent intent = new Intent(this, L99ServiceAndNotificationsActivity.class);
        intent.putExtra(L99ServiceAndNotificationsActivity.FILE_NAME, "somefile");
        //Далее мы оборачиваем этот Intent в PendingIntent, с помощью метода getActivity. На вход ему передаем контекст и Intent.
        //Второй параметр не используется (так написано в хелпе). А четвертый – это флаги, влияющие на поведение PendingIntent.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Notification.Builder builder = new Notification.Builder(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this); //рекомендуется использовать именно NotificationCompat, для лучшей совместимости
        builder
                .setSmallIcon(R.drawable.ic_sync_black_24dp)    //устанавливает маленький значок, который выводится в строке состояния, а также в правой части открытого уведомления.
                .setTicker("Text in status bar")                //выводит временную строку в строке состояния, которая затем исчезает. Остаётся только маленький значок
                //.setWhen(System.currentTimeMillis())
                .setWhen(new GregorianCalendar(2017, 0, 1).getTimeInMillis()) //установить поле время для увеломления (события)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.google_drive_icon)) // устанавливает большой значок, который выводится в открытом уведомлении слева.
                .setContentTitle("Notification's title")        // Заголовок уведомления
                .setContentText("Notification's text")          // Текст уведомления
                .setAutoCancel(true)                            //устанавливает флаг FLAG_AUTO_CANCEL, чтобы уведомление исчезло из статус-бара после нажатия. По умолчанию оно не исчезает и продолжает висеть.
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //или флаг Notification.DEFAULT_SOUND
                .setVibrate(new long[] {0L, 1000L, 200L, 1000L})
                .setSubText("Notification's subtext")
                .setOngoing(true)     //уведомление появляется не в обычной секции, а в ongoing (постоянные). Уведомления из этой секции не удаляются при нажатии кнопки очистки уведомлений. Используются, допустим, когда выводим статус загрузки в уведомлении.
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();

        //Далее вызываем метод notify для менеджера уведомлений и передаем туда ID и созданное уведомление. ID используется, если мы хотим изменить или удалить уведомление.
        nm.notify(1, notification);
        /*
        Обновление старого или новое уведомление
            Если вы создадите новое уведомление и отправите его (notify) с тем же ID, что и у уже существующего уведомления, то новое заменит старое.
            Таким образом, вы можете уведомления обновлять. Если же надо показать новое уведомление, то используйте другой ID.
        Удаление
            Чтобы убрать уведомление из статус-бара, используется метод cancel у менеджера уведомлений.
            На вход подается ID. Либо используйте метод cancelAll, чтобы удалить все уведомления.
        Звук
            Если хотите, чтобы уведомление появилось со стандартным звуком, добавьте флаг Notification.DEFAULT_SOUND в поле уведомления defaults.
            А для использования своих звуков используется поле sound.
            Чтобы проиграть файл с SD: notif.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
            Чтобы использовать какую-либо из стандартных мелодий, используем Content Provider: notif.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
        Вибра (<uses-permission android:name="android.permission.VIBRATE"/>)
            Если хотите, чтобы уведомление появилось со стандартной виброй, добавьте флаг Notification.DEFAULT_VIBRATE в поле уведомления defaults.
            А для использования своей комбинации вибры используется поле vibrate. В это поле помещается массив long-чисел.
            Первое – длительность паузы (в миллисекундах) перед началом вибрирования, второе – длительность вибрирования, третье – длительность паузы, четвертое – длительность вибрирования … и т.д. Т.е. создаете свою комбинацию пауз и вибрирования. И мобила при получении уведомления вам ее провибрирует.
        Индикатор
            Если хотите, чтобы уведомление появилось с миганием индикатора, добавьте флаг Notification.DEFAULT_LIGHTS в поле уведомления defaults.
            А для использования своей комбинации мигания индикатора используются поля
            ledARGB – здесь задается цвет
            ledOnMS – время «горения»
            ledOffMS – время «не горения»
            И в поле flags надо добавить флаг Notification.FLAG_SHOW_LIGHTS.
            В итоге индикатор будет мигать с заданными значениями и с заданным цветом. В хелпе написано, что не все девайсы поддерживают разные цвета.
            Поэтому выбранный вами цвет не гарантируется.
         */
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
