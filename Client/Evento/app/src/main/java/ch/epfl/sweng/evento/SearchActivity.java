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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.RestApi.PostCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;

public class SearchActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "SearchActivity";
    private TextView mStartDateView;
    private TextView mEndDateView;
    private Event.Date startDate;
    private Event.Date endDate;
    private boolean mStartOrEndDate;
    private boolean mDisplayTimeFragment;
    private DatePickerDialogFragment mDateFragment;
    private DialogFragment mTimeFragment;
    private ExpendableList mListAdapter;
    private ExpandableListView mExpListView;
    private List<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView mAutocompleteView;
    private TextView mPlaceDetailsText;
    private TextView mPlaceDetailsAttribution;
    private PlaceAutocompleteAdapter mAdapter;

    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    private static final String urlServer = ServerUrl.get();



    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        if(mStartOrEndDate == false) {
            startDate = new Event.Date(year, monthOfYear, dayOfMonth, 0, 0);
            String s = Integer.toString(startDate.getMonth()+1) + "/" + Integer.toString(startDate.getDay()) + "/" + Integer.toString(startDate.getYear()) ;
            mStartDateView.setText(s);
        }
        else {
            endDate = new Event.Date(year, monthOfYear, dayOfMonth, 0, 0);
            String s = Integer.toString(endDate.getMonth()+1) + "/" + Integer.toString(endDate.getDay()) + "/" + Integer.toString(endDate.getYear()) ;
            mEndDateView.setText(s);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisplayTimeFragment = false;
        setContentView(R.layout.activity_search);
        Button validateButton = (Button) findViewById(R.id.validate_search);
        mDateFragment = new DatePickerDialogFragment();
        mDateFragment.setListener(this);

        //START DATE
        mStartDateView = (TextView) findViewById(R.id.startDate_search);
        mStartDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartOrEndDate = false;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //END DATE
        mEndDateView = (TextView) findViewById(R.id.endDate_search);
        mEndDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartOrEndDate = true;
                mDateFragment.show(getFragmentManager(), "datePicker");
            }
        });


        validateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextView title = (TextView) findViewById(R.id.title);
                TextView description = (TextView) findViewById(R.id.eventDescription);
                TextView address = (TextView) findViewById(R.id.eventAddress);
                String titleString = title.getText().toString();
                String descriptionString = description.getText().toString();
                String addressString = address.getText().toString();

                // just in case you haven't put any date ;)
                if (startDate == null) {
                    startDate = new Event.Date(0, 0, 0, 0, 0);
                }
                if (endDate == null) {
                    endDate = new Event.Date(0, 0, 0, 0, 0);
                }
                if (titleString.isEmpty()) {
                    titleString = "No title";
                }
                if (descriptionString.isEmpty()) {
                    descriptionString = "No description";
                }
                if (addressString.isEmpty()) {
                    addressString = "No address";
                }


                String creator = "Jack Henri";
                Random rand = new Random();
                int id = rand.nextInt(10000);

//                Event e = new Event(id, titleString, descriptionString, latitude,
//                        longitude, addressString, creator,
//                        new HashSet<String>(),startDate, endDate);
                RestApi restApi = new RestApi(networkProvider, urlServer);

//                restApi.postEvent(e, new PostCallback() {
//                    @Override
//                    public void onPostSuccess(String response) {
//                        // nothing
//                    }
//                });
                Toast.makeText(getApplicationContext(), "Submitting " + titleString, Toast.LENGTH_SHORT).show();

                finish();

            }
        });


    }


}