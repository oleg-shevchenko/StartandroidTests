package com.example.matherial_tests.view;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matherial_tests.R;

public class TestModalBottomSheetFragment extends Fragment {

    public TestModalBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_modal_bottom_sheet, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openModalBottomSheet();
            }
        });

        return view;
    }

    private void openModalBottomSheet() {
        TestModalBottomSheet bottomSheet = new TestModalBottomSheet();
        bottomSheet.show(getFragmentManager(), "bottom sheet");
    }
}
