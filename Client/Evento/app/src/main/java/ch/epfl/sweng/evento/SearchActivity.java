package ch.epfl.sweng.evento;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.RestApi.GetMultipleResponseCallback;
import ch.epfl.sweng.evento.RestApi.PostCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.tabsFragment.ContentFragment;

public class SearchActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "SearchActivity";
    private TextView mStartDateView;
    private TextView mEndDateView;
    private GregorianCalendar startDate;
    private GregorianCalendar endDate;
    private boolean mStartOrEndDate;
    private DatePickerDialogFragment mDateFragment;

    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();

    private static final String urlServer = Settings.getServerUrl();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button validateButton = (Button) findViewById(R.id.validate_search);
        mDateFragment = new DatePickerDialogFragment();
        mDateFragment.setListener(this);

        // set date picker for startDate
        mStartDateView = (TextView) findViewById(R.id.startDate_search);
        mStartDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartOrEndDate = false;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // set date picker for endDate
        mEndDateView = (TextView) findViewById(R.id.endDate_search);
        mEndDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartOrEndDate = true;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });


        setValidateButtonAndSend(validateButton);


    }

    private void setValidateButtonAndSend(Button validateButton) {
        validateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RestApi restApi = new RestApi(networkProvider, getResources().getString(R.string.url_server));

                if(startDate == null){
                    startDate = new GregorianCalendar(2000, 1, 1, 0, 0);
                }
                if(endDate == null){
                    endDate = new GregorianCalendar(2020, 1, 1, 0, 0);
                }

                GregorianCalendar startTime = new GregorianCalendar(startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
                GregorianCalendar endTime = new GregorianCalendar(endDate.get(Calendar.YEAR),
                        endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

                EventDatabase.INSTANCE.loadByDate(startTime, endTime);


                Toast.makeText(getApplicationContext(), "Load events...", Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        if(mStartOrEndDate == false) {
            startDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0);
            String s = Integer.toString(startDate.get(Calendar.MONTH)+1) + "/"
                    + Integer.toString(startDate.get(Calendar.DAY_OF_MONTH)) + "/"
                    + Integer.toString(startDate.get(Calendar.YEAR)) ;
            mStartDateView.setText(s);
        }
        else {
            endDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0);
            String s = Integer.toString(endDate.get(Calendar.MONTH)+1) + "/"
                    + Integer.toString(endDate.get(Calendar.DAY_OF_MONTH)) + "/"
                    + Integer.toString(endDate.get(Calendar.YEAR)) ;
            mEndDateView.setText(s);
        }
    }


}
