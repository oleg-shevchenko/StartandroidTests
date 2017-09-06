package com.example.oleg.startandroidtests.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.oleg.startandroidtests.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        findViewById(R.id.tvWebBrowser).setOnClickListener(this);
        findViewById(R.id.tvMainTEST).setOnClickListener(this);
        findViewById(R.id.tvMainL14).setOnClickListener(this);
        findViewById(R.id.tvMainL14xml).setOnClickListener(this);
        findViewById(R.id.tvMainSnackbar).setOnClickListener(this);
        findViewById(R.id.tvMainContextMenu).setOnClickListener(this);
        findViewById(R.id.tvMainProgLayout).setOnClickListener(this);
        findViewById(R.id.tvMainProgView).setOnClickListener(this);
        findViewById(R.id.tvMainSeekLayParams).setOnClickListener(this);
        findViewById(R.id.tvMainAnimation).setOnClickListener(this);
        findViewById(R.id.tvMainTime).setOnClickListener(this);
        findViewById(R.id.tvMainIntentTests).setOnClickListener(this);
        findViewById(R.id.tvMainSQLiteTest1).setOnClickListener(this);
        findViewById(R.id.tvMainSQLiteTest2).setOnClickListener(this);
        findViewById(R.id.tvMainSQLiteTest3).setOnClickListener(this);
        findViewById(R.id.tvMainLayoutInflater).setOnClickListener(this);
        findViewById(R.id.tvMainListView).setOnClickListener(this);
        findViewById(R.id.tvMainExListView).setOnClickListener(this);
        findViewById(R.id.tvMainSimpleAdapter).setOnClickListener(this);
        findViewById(R.id.tvMainLoginTest).setOnClickListener(this);
        findViewById(R.id.tvMainSimpleCursorTreeAdapter).setOnClickListener(this);
        findViewById(R.id.tvMainCustomAdapter).setOnClickListener(this);
        findViewById(R.id.tvMainSpinner).setOnClickListener(this);
        findViewById(R.id.tvMainGridView).setOnClickListener(this);
        findViewById(R.id.tvMainDrawable).setOnClickListener(this);
        findViewById(R.id.tvMainDialog).setOnClickListener(this);
        findViewById(R.id.tvMainDialogNew).setOnClickListener(this);
        findViewById(R.id.tvMainSaveInstanceState).setOnClickListener(this);
        findViewById(R.id.tvMainSettings).setOnClickListener(this);
        findViewById(R.id.tvMainSettings1).setOnClickListener(this);
        findViewById(R.id.tvMainFile).setOnClickListener(this);
        findViewById(R.id.tvMainConstraintLayout).setOnClickListener(this);
        findViewById(R.id.tvMainCoordinatorLayout).setOnClickListener(this);
        findViewById(R.id.tvMainTab).setOnClickListener(this);
        findViewById(R.id.tvMainHandler1).setOnClickListener(this);
        findViewById(R.id.tvMainAsyncTask1).setOnClickListener(this);
        findViewById(R.id.tvMainAsyncTask2).setOnClickListener(this);
        findViewById(R.id.tvMainLoader).setOnClickListener(this);
        findViewById(R.id.tvMainCursorLoader).setOnClickListener(this);
        findViewById(R.id.tvMainAppList).setOnClickListener(this);
        findViewById(R.id.tvMainService1).setOnClickListener(this);
        findViewById(R.id.tvMainService2).setOnClickListener(this);
        findViewById(R.id.tvMainService3).setOnClickListener(this);
        findViewById(R.id.tvMainService4).setOnClickListener(this);
        findViewById(R.id.tvMainService5).setOnClickListener(this);
        findViewById(R.id.tvMainNotifications).setOnClickListener(this);
        findViewById(R.id.tvMainContentProvider).setOnClickListener(this);
        findViewById(R.id.tvMainTouch).setOnClickListener(this);
        findViewById(R.id.tvMainMultiTouch).setOnClickListener(this);
        findViewById(R.id.tvMainFragment1).setOnClickListener(this);
        findViewById(R.id.tvMainFragment2).setOnClickListener(this);
        findViewById(R.id.tvMainFragment3).setOnClickListener(this);
        findViewById(R.id.tvMainFragment4).setOnClickListener(this);
        findViewById(R.id.tvMainActionMode).setOnClickListener(this);
        findViewById(R.id.tvMainActionMode1).setOnClickListener(this);
        findViewById(R.id.tvMainDiffScreens).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startNextActivity(v.getId());
    }

    private void startNextActivity(int tvID) {
        Intent intent = null;
        switch (tvID) {
            case R.id.tvWebBrowser: {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com.ua"));
                break;
            }

            case R.id.tvMainTEST: {
                intent = new Intent(this, TestActivity.class);
                break;
            }

            case R.id.tvMainL14: {intent = new Intent(MainActivity.this, L14MenuActivityCode.class); break;}
            case R.id.tvMainL14xml: {intent = new Intent(MainActivity.this, L14MenuActivityXML.class); break;}
            case R.id.tvMainSnackbar: {intent = new Intent(MainActivity.this, SnackbarTestActivity.class); break;}
            case R.id.tvMainContextMenu: {intent = new Intent(MainActivity.this, L15ContextMenuActivity.class); break;}
            case R.id.tvMainProgLayout: {intent = new Intent(MainActivity.this, L16ProgramLayoutActivity.class); break;}
            case R.id.tvMainProgView: {intent = new Intent(MainActivity.this, L17ProgramViewComponents.class); break;}
            case R.id.tvMainSeekLayParams: {intent = new Intent(MainActivity.this, L18SeekbarAndLayoutParamsActivity.class); break;}
            case R.id.tvMainAnimation: {intent = new Intent(MainActivity.this, L20_Animation_Activity.class); break;}
            //Intent filter in Android manifest
            case R.id.tvMainTime: {intent = new Intent("com.example.oleg.action.showtime"); break;}
            case R.id.tvMainIntentTests: {intent = new Intent(this, IntentTests.class); break;}
            case R.id.tvMainSQLiteTest1: {intent = new Intent(this, L34SqlTetsActivity.class); break;}
            case R.id.tvMainSQLiteTest2: {intent = new Intent(this, L36SqlTestActivity.class); break;}
            case R.id.tvMainSQLiteTest3: {intent = new Intent(this, L37SqlRawQueryAndInnerJoinActivity.class); break;}
            case R.id.tvMainLayoutInflater: {intent = new Intent(this, L40LayoutInflaterActivity.class); break;}
            case R.id.tvMainListView: {intent = new Intent(this, L42ListViewActivity.class); break;}
            case R.id.tvMainExListView: {intent = new Intent(this, L45ExpandableListViewActivity.class); break;}
            case R.id.tvMainSimpleAdapter: {intent = new Intent(this, L48SimpleAdapterActivity.class); break;}
            case R.id.tvMainLoginTest: {intent = new Intent(this, TestLoginActivity.class); break;}
            case R.id.tvMainSimpleCursorTreeAdapter: {intent = new Intent(this, L53SimpleCursorTreeAdapterActivity.class); break;}
            case R.id.tvMainCustomAdapter: {intent = new Intent(this, L54CustomAdapter.class); break;}
            case R.id.tvMainSpinner: {intent = new Intent(this, L56SpinnerTest.class); break;}
            case R.id.tvMainGridView: {intent = new Intent(this, L57GridView.class); break;}
            case R.id.tvMainDrawable: {intent = new Intent(this, TestDrawables.class); break;}
            case R.id.tvMainDialog: {intent = new Intent(this, L59DialogTestActivity.class); break;}
            case R.id.tvMainDialogNew: {intent = new Intent(this, OnL62DialogTestActivity.class); break;}
            case R.id.tvMainSaveInstanceState: {intent = new Intent(this, L70onSaveInstanceStateTest.class); break;}
            case R.id.tvMainSettings: {intent = new Intent(this, SettingsActivity.class); break;}
            case R.id.tvMainSettings1: {intent = new Intent(this, L71MainActivity.class); break;}
            case R.id.tvMainFile: {intent = new Intent(this, L75FileReadWrite.class); break;}
            case R.id.tvMainConstraintLayout: {intent = new Intent(this, L180ConstraintLayout.class); break;}
            case R.id.tvMainCoordinatorLayout: {intent = new Intent(this, TestCoordinatorLayout.class); break;}
            case R.id.tvMainTab: {intent = new Intent(this, L76TabActivity.class); break;}
            case R.id.tvMainHandler1: {intent = new Intent(this, L80HandlerActivity.class); break;}
            case R.id.tvMainAsyncTask1: {intent = new Intent(this, L86AsyncTask1.class); break;}
            case R.id.tvMainAsyncTask2: {intent = new Intent(this, L87AsyncTask2.class); break;}
            case R.id.tvMainLoader: {intent = new Intent(this, L135LoaderActivity.class); break;}
            case R.id.tvMainCursorLoader: {intent = new Intent(this, L136CursorLoaderActivity.class); break;}
            case R.id.tvMainAppList: {intent = new Intent(this, AppListLoaderActivity.class); break;}
            case R.id.tvMainService1: {intent = new Intent(this, L92ServiceActivity.class); break;}
            case R.id.tvMainService2: {intent = new Intent(this, L95ServicePendingIntentActivity.class); break;}
            case R.id.tvMainService3: {intent = new Intent(this, L96ServiceBroadcastReciverActivity.class); break;}
            case R.id.tvMainService4: {intent = new Intent(this, L97ServiceBindActivity.class); break;}
            case R.id.tvMainService5: {intent = new Intent(this, L99ServiceAndNotificationsActivity.class); break;}
            case R.id.tvMainNotifications: {intent = new Intent(this, TestNotificationsActivity.class); break;}
            case R.id.tvMainContentProvider: {intent = new Intent(this, L101ContentProviderActivity.class); break;}
            case R.id.tvMainTouch: {intent = new Intent(this, L102TouchActivity.class); break;}
            case R.id.tvMainMultiTouch: {intent = new Intent(this, L103MultitouchActivity.class); break;}
            case R.id.tvMainFragment1: {intent = new Intent(this, L104FragmentLifecycleActivity.class); break;}
            case R.id.tvMainFragment2: {intent = new Intent(this, L105ChangeFragmentActivity.class); break;}
            case R.id.tvMainFragment3: {intent = new Intent(this, L106FragmentAccessActivity.class); break;}
            case R.id.tvMainFragment4: {intent = new Intent(this, L109ListFragmentActivity.class); break;}
            case R.id.tvMainActionMode: {intent = new Intent(this, L113ActionModeActivity.class); break;}
            case R.id.tvMainActionMode1: {intent = new Intent(this, L113ActionModeListActivity.class); break;}
            case R.id.tvMainDiffScreens: {intent = new Intent(this, L115DifferentScreenActivity.class); break;}
            default: return;
        }
        if(intent != null) startActivity(intent);
    }
}
