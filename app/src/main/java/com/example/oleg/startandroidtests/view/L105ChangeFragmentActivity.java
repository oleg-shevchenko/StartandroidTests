package com.example.oleg.startandroidtests.view;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.example.oleg.startandroidtests.R;
import com.example.oleg.startandroidtests.view.fragments.L104Fragment1;
import com.example.oleg.startandroidtests.view.fragments.L104Fragment2;

public class L105ChangeFragmentActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    L104Fragment1 frag1;
    L104Fragment2 frag2;

    //Этот объект является основным для работы с фрагментами. Далее, чтобы добавить/удалить/заменить фрагмент, нам необходимо использовать транзакции.
    //Они аналогичны транзакциям в БД, где мы открываем транзакцию, производим операции с БД, выполняем commit.
    //Здесь мы открываем транзакцию, производим операции с фрагментами (добавляем, удаляем, заменяем), выполняем commit.
    FragmentTransaction fTrans;

    CheckBox chbStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l105_change_fragment);

        frag1 = new L104Fragment1();
        frag2 = new L104Fragment2();

        chbStack = (CheckBox)findViewById(R.id.chbStack);
    }

    public void onClick(View v) {
        //получили FragmentManager и открыли транзакцию методом beginTransaction
        fTrans = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.btnAdd:
                //передаем id контейнера (тот самый FrameLayout из main.xml) и объект фрагмента. В итоге, в контейнер будет помещен Fragment1
                //!!!Если добавить фрагмент, который уже добавлен, то вылетит IllegalStateException, лучше использовать replace
                fTrans.add(R.id.frgmCont, frag1);
                break;
            case R.id.btnRemove:
                // передаем объект фрагмента, который хотим убрать. В итоге, фрагмент удалится с экрана.
                fTrans.remove(frag1);
                break;
            case R.id.btnReplace:
                //передаем id контейнера и объект фрагмента. В итоге, из контейнера удалится его текущий фрагмент (если он там есть) и добавится фрагмент, указанный нами.
                fTrans.replace(R.id.frgmCont, frag2);
            default:
                break;
        }
        //Далее проверяем чекбокс. Если он включен, то добавляем транзакцию в BackStack. Для этого используем метод addToBackStack.
        //На вход можно подать строку-тэг. Я передаю null.
        if (chbStack.isChecked())
            //В бекстек добавляются транзакции, т.е. если потом жать кнопку назад, то все будет происходить в обратном порядке.
            //!!!Когда мы удаляем фрагмент и не добавляем транзакцию в BackStack, то фрагмент уничтожается.
            //!!!Если же транзакция добавляется в BackStack, то, при удалении, фрагмент не уничтожается (onDestroy не вызывается), а останавливается (onStop).
            fTrans.addToBackStack(null);
        //Также можно
        //снимать фрагменты со стека переходов назад методом popBackStack() (имитируя нажатие кнопки Назад пользователем);
        //регистрировать процесс-слушатель изменений в стеке переходов назад при помощи метода addOnBackStackChangedListener().

        //К каждой транзакции с фрагментом можно применить анимацию перехода
        fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        //Ну и вызываем commit, транзакция завершена.
        fTrans.commit();
    }
}
