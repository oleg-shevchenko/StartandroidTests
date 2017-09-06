package com.example.matherial_tests.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matherial_tests.R;

/**
 * Created by Oleg on 20.07.2017.
 */
//Этот класс описывает сам Modal BottomSheet, а не вызывает его
//BottomSheetDialogFragment в отличии от обычного ModalSheet похож на диалог-фрагмент
public class TestModalBottomSheet extends BottomSheetDialogFragment {
    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_modal, container, false);
        setupButton(v);
        return v;
    }

    //На кнопку просто будем закрывать диалог
    private void setupButton(View v) {
        v.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

//Второй вариант инициализации View
//    @Override
//    public void setupDialog(final Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
//        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_modal, null);
//        dialog.setContentView(contentView);
//    }
}
