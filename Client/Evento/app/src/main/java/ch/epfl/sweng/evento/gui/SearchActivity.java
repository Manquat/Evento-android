package ch.epfl.sweng.evento.gui;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.User;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.callback.GetEventListCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;

public class SearchActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SearchActivity";
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private TextView mStartDateView;
    private TextView mEndDateView;
    private Calendar startDate;
    private Calendar endDate;
    private boolean mStartOrEndDate;
    private DatePickerDialogFragment mDateFragment;
    private TextView mPlaceDetailsAttribution;
    private double latitude = 46.8;
    private double longitude = 7.1;
    private double radius = 1500;
    private TextView mPlaceDetailsText;
    private PlaceAutocompleteAdapter mAdapter;
    private RestApi mRestApi;
    private boolean mFilterPersonalEvent;
    private GoogleApiClient mGoogleApiClient;
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // set longitude and latitude
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;

            // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

//            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
//                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRestApi = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
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

        // listener for address place picker (googlePlaceAPI)
        setPlacePickerField();

        setValidateButtonAndSend(validateButton);
    }

    public void onCheckboxClicked(View view) {
        mFilterPersonalEvent = ((CheckBox) view).isChecked();
    }

    private void setValidateButtonAndSend(Button validateButton) {
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, Boolean.toString(mFilterPersonalEvent));

                if (startDate == null) {
                    startDate = new GregorianCalendar(1990, 1, 1, 0, 0);
                }
                if (endDate == null) {
                    endDate = new GregorianCalendar(2020, 1, 1, 0, 0);
                }

                GregorianCalendar startTime = new GregorianCalendar(startDate.get(Calendar.YEAR),
                        startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

                GregorianCalendar endTime = new GregorianCalendar(endDate.get(Calendar.YEAR),

                        endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

                mRestApi.getWithFilter(startTime, endTime, latitude, longitude, radius, new GetEventListCallback() {
                    @Override
                    public void onEventListReceived(List<Event> eventArrayList) {
                        EventDatabase.INSTANCE.clear();
                        if (!mFilterPersonalEvent) {
                            EventDatabase.INSTANCE.addAll(eventArrayList);
                            finish();
                        } else {
                            List<Event> personalEvent = new ArrayList<Event>();
                            Log.d(TAG, Integer.toString(personalEvent.size()));
                            selectPersonalEvent(personalEvent, eventArrayList);
                            //selectPersonalEvent(eventArrayList, personalEvent);

                        }

                    }

                    public void onUserListReceived(List<User> userArrayList) {

                    }
                });

                Toast.makeText(getApplicationContext(), "Load events by search parameters", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPersonalEvent(List<Event> event, List<Event> input) {
        final List<Event> innerEvent = event;
        final List<Event> innerInput = input;
        mRestApi.getHostedEvent(new GetEventListCallback() {
            public void onEventListReceived(List<Event> eventArrayList) {
                if (eventArrayList != null) {
                    innerEvent.addAll(eventArrayList);
                    mRestApi.getMatchedEvent(new GetEventListCallback() {
                        public void onEventListReceived(List<Event> eventArrayList) {
                            if (eventArrayList != null) {
                                innerEvent.addAll(eventArrayList);
                                Log.d(TAG, Integer.toString(innerEvent.size()));
                                Log.d(TAG, Integer.toString(innerInput.size()));
                                //innerInput.retainAll(innerEvent);
                                Log.d(TAG, Integer.toString(innerInput.size()));
                                List<Event> res = new ArrayList<Event>();
                                retainAll(innerInput, innerEvent, res);
                                EventDatabase.INSTANCE.addAll(res);
                                finish();
                            }
                        }

                    }, Settings.getUser().getUserId());
                }
            }

        }, Settings.getUser().getUserId());
    }

    private void retainAll(List<Event> input, List<Event> filter, List<Event> res) {
        boolean isInside = false;
        for (Event event : input) {
            for (Event filterIter : filter) {
                if (event.getID() == filterIter.getID()) isInside = true;
            }
            if (isInside) res.add(event);
            isInside = false;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        GregorianCalendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0);
        String s = dateFormat.format(date.getTime());

        if (!mStartOrEndDate) {
            startDate = date;
            mStartDateView.setText(s);
        } else {
            endDate = date;
            mEndDateView.setText(s);
        }
    }

    // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
    // functionality, which automatically sets up the API client to handle Activity lifecycle
    // events. If your activity does not extend FragmentActivity, make sure to call connect()
    // and disconnect() explicitly.
    private void setPlacePickerField() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        AutoCompleteTextView mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.searchAddress);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceDetailsText.setVisibility(View.GONE);
        mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);
        mPlaceDetailsAttribution.setVisibility(View.GONE);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        mAutocompleteView.setAdapter(mAdapter);
    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


}
