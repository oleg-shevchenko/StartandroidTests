package com.example.oleg.startandroidtests.view;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

//Поскольку никакие потоки, кроме основного не имеют доступ к UI, стоит использовать Handler

/*В чем разница между обычным Handler и static? Чтобы понять это, необходимо знать две вещи:
1) Не static Handler содержит скрытую ссылку на Activity.
2) Сообщения предназначенные для Handler содержат ссылку на сам Handler (а следовательно и на Activity).

Два этих факта могут вызвать утечку памяти. Например, вы в системную очередь на выполнение помещаете (с помощью Handler) сообщение,
которое должно выполниться через час. Затем закрываете приложение. Система хочет освободить память и поудалять из нее неиспользуемые объекты.
И она удалила бы закрытое Activity и все его содержимое, но не может. Потому что сообщение, которое будет висеть еще час,
хранит ссылку на Handler, а Handler хранит ссылку на Activity. В итоге одно сообщение (которое, скорее всего, уже никому не нужно)
держит зря целое Activity и не дает системе удалить его из памяти.
Объявляя Handler как static, мы разрубаем цепь ссылок и Handler больше не хранит ссылку на Activity.
Но! Handler обычно используется чтобы взаимодействовать с UI, и он должен уметь работать с Activity и его методами. А для этого он должен иметь ссылку на Activity.
Получается легкое противоречие. Handler должен хранить ссылку на Activity, чтобы работать с ним.
Но не должен хранить ссылку на Activity, чтобы не вызвать утечку памяти.

Тут выручает Java-механизм слабых ссылок - WeakReference. Слабая ссылка не учитывается системой при очистке памяти.
Если система видит, что Activity больше не нужно, но есть Handler, который хранит слабую ссылку на Activity, - слабая ссылка будет проигнорена,
Activity будет удалено и утечек памяти не будет.
Мы в конструктор Handler-а передаем Activity и для хранения используем контейнер WeakReference.
Метод get позволяет нам получить ссылку обратно из контейнера. А если WeakReference возвращает null, значит объект был удален из памяти, т.е. Activity было закрыто.*/

public class L80HandlerActivity extends AppCompatActivity implements View.OnClickListener {

    //Хендлер делаем статическим, для того, чтобы при повороте экрана он у нас сохранялся
    //(По идее можно пересоздавать handler в новом активити и у Message вызываем setTarget(Handler h) и sendToTarget())
    private static MyHandler mHandler;

    private static final String LOG_TAG = "L80HandlerActivity";
    private TextView tvInfo;
    private Button btnStart;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l80_handler);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        btnStart = (Button) findViewById(R.id.btnStart);
        pb = (ProgressBar) findViewById(R.id.pbLoad);
        btnStart.setOnClickListener(this);

        //Проверяем ссылку на хендлер, если null, то создаем новый экземпляр,
        //если экземпляр существует (напр, экран перевернули), то сохраняем ссылку новый активити для доступа к полям и методам
        if(mHandler == null)
            mHandler = new MyHandler(this);
        else
            mHandler.setActivity(this);
    }

    /*Instances of static inner classes do not hold an implicit reference to their outer class.
    Т.е. для хендлера нужно использовать отдельный статический класс, для предотвращения утечек памяти, т.к. во внутреннем классе
    будет храниться ссылка на активити и сборщик мусора не сможет ее очистить, пока есть сообщения (например отложеные), в которых тоже ссылка на Handler
     1) Если бы я класс MyHandler сделал не static, то он бы содержал в себе скрытую ссылку на Activity.
     2) Сообщения предназначенные для Handler содержат ссылку на сам Handler. */
    private static class MyHandler extends Handler {
        //Используем мягкую ссылку на активити, чтобы если в хендлере останутся сообщения, сборщик мусора смог
        //убрать активити, несмотря на ссылку на нее
        private WeakReference<L80HandlerActivity> wrActivity;

        //Конструктор со ссылкой на активити, для первичного создания
        public MyHandler(L80HandlerActivity activity) {
            wrActivity = new WeakReference<L80HandlerActivity>(activity);
        }

        //Метод используется для установки нового активити, при перерисовке
        public void setActivity(L80HandlerActivity activity) {
            wrActivity.clear();
            wrActivity = new WeakReference<L80HandlerActivity>(activity);
        }

        //Обработка сообщений
        @Override
        public void handleMessage(Message msg) {
            L80HandlerActivity activity = wrActivity.get();
            //Проверяем существует ли еще активити, на которую у нас мягкая ссылка
            if (activity != null) {
                switch (msg.what) {
                    case 1: {
                        activity.pb.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2: {
                        activity.pb.setIndeterminate(false);
                        // обновляем TextView
                        activity.tvInfo.setText((String) msg.obj + msg.arg1 + " из " + msg.arg2);
                        activity.pb.setMax(msg.arg2);
                        activity.pb.setProgress(msg.arg1);
                        if (msg.arg1 == msg.arg2) activity.btnStart.setEnabled(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);

                new Thread(sRunnable).start();
                break;
            default:
                break;
        }
    }

    /*Non-static instances of anonymous classes hold an implicit reference to their outer class, so the context will be leaked.
    Instances of anonymous classes do not hold an implicit reference to their outer class when they are "static".
    !!!Т.е. не следует использовать нестатические вложенные классы в активити, если их обьекты могут пережить активити*/
    private static final Runnable sRunnable = new Runnable() {
        final int MAX_FILES = 10;
        @Override
        public void run() {
            //Можно просто посылать сообщения маячки, т.е. используя метод sendEmptyMessage(int what)
            mHandler.sendEmptyMessage(1);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= MAX_FILES; i++) {
                // долгий процесс
                downloadFile(i);
                //А можно передавать два int аргумента и обьект (здесь для примера передаем один и тот же стринг, но можно Bundle например)
                //Рекомендуется создавать Message именно с помощью obtainMessage() т.к. это эффективней и быстрее, хотя можно и через конструктор
                Message msg = mHandler.obtainMessage(2, i, MAX_FILES, "Закачано файлов: ");
                mHandler.sendMessage(msg);
            }
        }
    };

    //Через каждую секунду посылаем сообщение
    private static void downloadFile(int i) {
        // пауза - 1 секунда
        try {
            TimeUnit.SECONDS.sleep(1);
            Log.d(LOG_TAG, "i = " + i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.getInt("pb_visibility", pb.getVisibility());
        outState.putInt("pb_max", pb.getMax());
        outState.putInt("pb_progress", pb.getProgress());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pb.setVisibility(savedInstanceState.getInt("pb_visibility"));
        pb.setMax(savedInstanceState.getInt("pb_max"));
        pb.setProgress(savedInstanceState.getInt("pb_progress"));
    }


    //*******************************************************************************************//



    //    Для примера. Отправка сообщений, в нужный handler.
    private static void sendMsg(int what, int arg1, int arg2, Object obj, Handler handler) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        msg.setTarget(handler);
        msg.sendToTarget();
    }

    //Другие примеры:
    private void someTests() {
     /*Hanler.Callback
    * Кроме стандартного способа Handler также может использовать для обработки сообщений объект, реализующий интерфейс Handler.Callback.
    * У интерфейса всего один метод handleMessage – в нем и прописываем всю логику обработки сообщений.
    * Я пока не встречал практической пользы от этой штуки, но все же разберемся, как ее можно использовать. Может когда и пригодится.
    * вариант использования - Handler.Callback - это интерфейс, значит мы можем прикрутить его к нашему Activity*/
        Handler.Callback hc = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d(LOG_TAG, "Message what=" + msg.what);
                return true;
            }
        };
        final Handler handler = new Handler(hc);

        //Это аналог знакомого нам метода sendEmptyMessage с прошлого урока. Он тоже заполняет в сообщении только атрибут what,
        //но при этом он позволяет указать задержку в обработке сообщения. Т.е. сообщение будет извлечено из очереди и отправлено на обработку
        //через указанное количество миллисекунд. Отсчет задержки начинается после помещения в очередь, а не после обработки предыдущего сообщения.
        handler.sendEmptyMessageDelayed(1, 1000L);
        handler.sendMessageDelayed(handler.obtainMessage(1, null), 2000L);

        //Удалить все сообщения с нужным what
        handler.removeMessages(1);

        //Есть еще методы sendEmptyMessageAtTime и sendMessageAtTime. Они тоже позволяют указать задержку обработки.
        //Но эта задержка будет отсчитана от времени последнего старта системы, а не от времени помещения в очередь.
        //Если сообщение окажется просроченным на момент помещения в очередь, оно выполняется сразу.
        handler.sendEmptyMessageAtTime(1, 60000L);

        //Очистить все сообщения (возможно это вариант для предотвращения утечек памяти?)
        handler.removeCallbacksAndMessages(null);

        //Выполнить Runnable в UI потоке
        handler.post(sRunnable);

        //Выполнить Runnable с задержкой
        handler.postDelayed(sRunnable, 1000L);

        //Удалить Runnable из очереди
        handler.removeCallbacks(sRunnable);
    }

    //Runnable, который перезапускает себя каждую секунду
    Runnable repeatRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(repeatRunnable, 1000);
        }
    };

    //Еще способы выполнения кода в UI потоке:
    private void otherTests() {
        //Первые два похожи и отправляют Runnable на немедленную обработку. Я не знаю в чем их принципиальное отличие.
        runOnUiThread(sRunnable);       //Activity.runOnUiThread(Runnable)
        tvInfo.post(sRunnable);         //View.post(Runnable)

        tvInfo.postDelayed(sRunnable, 1000L);       //View.postDelayed(Runnable, long)
    }

    //Пример. Хендлер в отдельном потоке (напр для службы) https://developer.android.com/guide/components/services.html?hl=ru
    private void testHandler() {
        class TestHandler extends Handler {
            public TestHandler(Looper looper) {
                super(looper);
            }
        }
        HandlerThread thread = new HandlerThread("MyHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Looper mLooper = thread.getLooper();
        TestHandler handler = new TestHandler(mLooper);
    }


}
