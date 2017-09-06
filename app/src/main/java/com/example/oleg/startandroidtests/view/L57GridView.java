package com.example.oleg.startandroidtests.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.oleg.startandroidtests.R;

public class L57GridView extends AppCompatActivity {

    String[] data = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};

    GridView gvMain;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l57_grid_view);

        adapter = new ArrayAdapter<String>(this, R.layout.layout_l57_item, R.id.tvText, data);
        gvMain = (GridView) findViewById(R.id.gvMain);
        gvMain.setAdapter(adapter);
        adjustGridView();
    }


    private void adjustGridView() {
        //По умолчанию все будет в один столбец, если не прописать в XML, поэтому задаем кол-во
        gvMain.setNumColumns(3);
        //Это свойство также может иметь значение AUTO_FIT. В этом случае проверяется значение поля атрибута columnWidth (ширина столбца).
        //- если ширина столбца явно указана, то кол-во столбцов рассчитывается исходя из ширины, доступной GridView, и ширины столбцов.
        //- иначе, кол-во столбцов считается равным 2
        //gvMain.setNumColumns(GridView.AUTO_FIT);

        //Теперь укажем явно ширину столбцов, пусть будет 50.
        gvMain.setNumColumns(GridView.AUTO_FIT);
        gvMain.setColumnWidth(100);

        //Это горизонтальный и вертикальный отступы между ячейками. Пусть будет 5.
        gvMain.setVerticalSpacing(5);
        gvMain.setHorizontalSpacing(5);

        //stretchMode - Этот параметр определяет, как будет использовано свободное пространство, если оно есть.
        // Используется в случае, когда вы указываете ширину столбца и кол-во ставите в режим AUTO_FIT. Изменим наш метод, добавим туда настройку stretch-параметра.
        //NO_STRETCH – свободное пространство не используется, столбцы выровнены по левому краю. Все свободное пространство справа.
        //STRETCH_COLUMN_WIDTH – свободное пространство используется столбцами, это режим по умолчанию, столбцы растянуты по ширине. Она уже может не соответствовать той, что указана в setColumnWidth.
        //STRETCH_SPACING – свободное пространство равномерно распределяется между столбцами. Ширина столбцов неизменна. Увеличены интервалы между ними.
        //STRETCH_SPACING_UNIFORM – свободное пространство равномерно распределяется не только между столбцами, но и справа и слева. Ширина столбцов неизменна.
        gvMain.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);

        /*Разумеется, все эти параметры можно задавать не только программно, но и через атрибуты в layout-файлах.
        Вместо ArrayAdapter можно использовать любой другой. Можно прикрутить обработчик setOnItemClickListener
        и получать позицию или id нажатого элемента. Все как в обычных списках.*/
    }
}
