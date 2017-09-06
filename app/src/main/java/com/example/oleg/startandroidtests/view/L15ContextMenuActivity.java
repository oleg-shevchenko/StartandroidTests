package com.example.oleg.startandroidtests.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.oleg.startandroidtests.R;

//Не забывайте, что для контекстного меню вы также можете использовать XML-способ,
// который был рассмотрен в конце прошлого урока. Попробуйте сделать этот же урок, но уже с использованием XML-меню.

public class L15ContextMenuActivity extends AppCompatActivity {

    //Теперь опишем создание контекстных меню. Используем константы для хранения ID пунктов меню.
    final int MENU_COLOR_RED = 1;
    final int MENU_COLOR_GREEN = 2;
    final int MENU_COLOR_BLUE = 3;
    final int MENU_SIZE_22 = 4;
    final int MENU_SIZE_26 = 5;
    final int MENU_SIZE_30 = 6;

    TextView tvColor, tvSize;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l15_context_menu);

        tvColor = (TextView) findViewById(R.id.tvL15Color);
        tvSize = (TextView) findViewById(R.id.tvL15Size);

        // для tvColor и tvSize необходимо создавать контекстное меню
        registerForContextMenu(tvColor);
        registerForContextMenu(tvSize);
        //tvColor.setOnCreateContextMenuListener(this); - то же самое
        //tvSize.setOnCreateContextMenuListener(this); - то же самое
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            //В качестве ID пунктов мы использовали константы. Группировку и сортировку не используем,
            // поэтому используем нули в качестве соответствующих параметров.
            case R.id.tvL15Color:
                menu.add(0, MENU_COLOR_RED, 0, "Red");
                menu.add(0, MENU_COLOR_GREEN, 0, "Green");
                menu.add(0, MENU_COLOR_BLUE, 0, "Blue");
                break;
            case R.id.tvL15Size:
                menu.add(0, MENU_SIZE_22, 0, "22");
                menu.add(0, MENU_SIZE_26, 0, "26");
                menu.add(0, MENU_SIZE_30, 0, "30");
                break;
        }
    }

    //В этом методе мы определяем по ID, какой пункт меню был нажат.
    // И выполняем соответствующие действия: меняем цвет текста для tvColor или размер шрифта для tvSize.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            // пункты меню для tvColor
            case MENU_COLOR_RED:
                tvColor.setTextColor(Color.RED);
                tvColor.setText("Text color = red");
                break;
            case MENU_COLOR_GREEN:
                tvColor.setTextColor(Color.GREEN);
                tvColor.setText("Text color = green");
                break;
            case MENU_COLOR_BLUE:
                tvColor.setTextColor(Color.BLUE);
                tvColor.setText("Text color = blue");
                break;
// пункты меню для tvSize
            case MENU_SIZE_22:
                tvSize.setTextSize(22);
                tvSize.setText("Text size = 22");
                break;
            case MENU_SIZE_26:
                tvSize.setTextSize(26);
                tvSize.setText("Text size = 26");
                break;
            case MENU_SIZE_30:
                tvSize.setTextSize(30);
                tvSize.setText("Text size = 30");
                break;
        }
        return super.onContextItemSelected(item);
    }
}
