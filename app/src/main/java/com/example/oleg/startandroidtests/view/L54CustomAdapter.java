package com.example.oleg.startandroidtests.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.startandroidtests.R;

import java.util.ArrayList;

public class L54CustomAdapter extends AppCompatActivity {
    ArrayList<Product> products = new ArrayList<Product>();
    BoxAdapter boxAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l54_custom_adapter);

        // создаем адаптер
        fillData();
        boxAdapter = new BoxAdapter(this, products);

        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        //Урок 55 - добавляем header и footer к списку (!!!Обязательно перед присвоением адаптера)
        //false - для того, чтобы был не кликабельным
        lvMain.addHeaderView(createHeaderFooter("This is my header"), "some text for header", false);
        lvMain.addFooterView(createHeaderFooter("This is my footer"), "some text for footer", false);

        lvMain.setAdapter(boxAdapter);

        /*
        HeaderViewListAdapter hvlAdapter = (HeaderViewListAdapter) lvMain.getAdapter();
        obj = hvlAdapter.getItem(0); //obj.toString() - вернет "some text for header"
        ArrayAdapter<String> alAdapter = (ArrayAdapter<String>) hvlAdapter.getWrappedAdapter();
        obj = alAdapter.getItem(0); //obj.toString() - вернет "one"
         */
    }

    // генерируем данные для адаптера
    void fillData() {
        for (int i = 1; i <= 20; i++) {
            products.add(new Product("Product " + i, i * 1000, R.mipmap.ic_launcher, false));
        }
    }

    // выводим информацию о корзине
    public void showResult(View v) {
        String result = "Товары в корзине:";
        for (Product p : boxAdapter.getBox()) {
            if (p.box)
                result += "\n" + p.name;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    //Получаем view для header и footer используя LayoutInflater
    private View createHeaderFooter(String text) {
        View v = getLayoutInflater().inflate(R.layout.layout_listheader_l55, null);
        ((TextView) v.findViewById(R.id.tvText)).setText(text);
        return v;
    }
}

class Product {
    String name;
    int price;
    int image;
    boolean box;

    Product(String _describe, int _price, int _image, boolean _box) {
        name = _describe;
        price = _price;
        image = _image;
        box = _box;
    }
}

class BoxAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Product> objects;

    BoxAdapter(Context context, ArrayList<Product> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        //Метод getView должен возвращать View пункта списка. Для этого мы создавали layout-ресурс R.layout.item.
        // В этом методе мы должны из R.layout.item создать View, заполнить его данными и отдать списку.
        // Но перед тем как создавать, мы пробуем использовать convertView, который идет на вход метода.
        // Это уже созданное ранее View, но неиспользуемое в данный момент. Например, при прокрутке списка,
        // часть пунктов уходит за экран и их уже не надо прорисовывать. View из этих «невидимых» пунктов используются для новых пунктов.
        // Нам остается только заполнить их данными. Это значительно ускоряет работу приложения, т.к. не надо прогонять inflate лишний раз.
        //Если же convertView в этот раз нам не дали (null), то создаем сами view. Далее заполняем наименования, цену и картинку из данных по товарам.
        View view = convertView;
        if(view == null) {
            view = lInflater.inflate(R.layout.layout_item_l54, parent, false);
        }
        Product p = (Product) getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена и картинка
        ((TextView) view.findViewById(R.id.tvDescr)).setText(p.name);
        ((TextView) view.findViewById(R.id.tvPrice)).setText(p.price + "");
        ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);

        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        // присваиваем чекбоксу обработчик
        cbBuy.setOnCheckedChangeListener(myCheckChangeList);
        // пишем позицию
        /*Tag – это некое Object-хранилище у каждого View, куда вы можете поместить нужные вам данные.
        В нашем случае я для каждого чекбокса помещаю в его Tag номер позиции пункта списка.
        Далее в обработчике чекбокса я смогу этот номер позиции извлечь и определить, в каком пункте списка был нажат чекбокс.*/
        cbBuy.setTag(position);
        // заполняем данными из товаров: в корзине или нет
        cbBuy.setChecked(p.box);
        return view;
    }

    ArrayList<Product> getBox() {
        ArrayList<Product> box = new ArrayList<Product>();
        for (Product p : objects) {
            // если в корзине
            if (p.box)
                box.add(p);
        }
        return box;
    }

    // обработчик для чекбоксов
    //myCheckChangeList – обработчик для чекбоксов. Когда мы нажимаем на чекбокс в списке, он срабатывает,
    // читает из Tag позицию пункта списка и помечает соответствующий товар, как положенный в корзину.
    //Тут важно понимать, что без этого обработчика не работало бы помещение товаров в корзину.
    // Да и на экране - значения чекбоксов в списке терялись бы при прокрутке. Потому что пункты списка пересоздаются,
    // если они уйдут «за экран» и снова появятся. Это пересоздание обеспечивает метод getView,
    // а он для заполнения View берет данные из товаров. Значит при нажатии на чекбокс, обязательно надо сохранить в данных о товаре то, что он теперь в корзине.
    CompoundButton.OnCheckedChangeListener myCheckChangeList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // меняем данные товара (в корзине или нет)
            ((Product) getItem((Integer) buttonView.getTag())).box = isChecked;
        }
    };

//Можно также указать хедер некликабельным в адаптере
//    @Override
//    public boolean isEnabled(int position) {
//        if(position == 0) return false;
//        return super.isEnabled(position);
//    }
}
