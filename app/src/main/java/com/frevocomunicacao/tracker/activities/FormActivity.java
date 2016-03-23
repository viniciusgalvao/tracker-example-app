package com.frevocomunicacao.tracker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.adapters.ImageAdapter;
import com.frevocomunicacao.tracker.database.DbHelper;
import com.frevocomunicacao.tracker.database.models.Visit;
import com.frevocomunicacao.tracker.database.models.VisitImage;
import com.frevocomunicacao.tracker.utils.GpsTracker;
import com.frevocomunicacao.tracker.utils.ImageUtils;
import com.frevocomunicacao.tracker.widget.MultiSelectSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FormActivity extends BaseActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Visit visit;
    private String mCurrentPhotoPath;
    private String viewMode;

    private MultiSelectSpinner spinner;
    private ProgressDialog dialog;

    private GpsTracker tracker;
    private Location mLocation;

    private EditText edtAddressCep, edtAddress, edtAddressNumber, edtAddressComplement, edtAddressNeighborhood, edtAddressCity,
            edtAddressState, edtAddressReferencePoint, edtObservation;

    private GridView galleryContainer;
    private List<VisitImage> images;
    private ImageAdapter adapter;

    // database
    private DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // form view
        edtAddressCep           = ((EditText) findViewById(R.id.edt_address_cep));
        edtAddress              = ((EditText) findViewById(R.id.edt_address));
        edtAddressNumber        = ((EditText) findViewById(R.id.edt_address_number));
        edtAddressComplement    = ((EditText) findViewById(R.id.edt_address_complement));
        edtAddressNeighborhood  = ((EditText) findViewById(R.id.edt_address_neighborhood));
        edtAddressCity          = ((EditText) findViewById(R.id.edt_address_city));
        edtAddressState         = ((EditText) findViewById(R.id.edt_address_state));
        edtAddressReferencePoint= ((EditText) findViewById(R.id.edt_address_reference_point));

        // activity view mode
        viewMode = getIntent().getExtras().getString("mode");

        if (viewMode.equals("edit")) {
            setTitle("Check-in");
            visit = (Visit) getIntent().getSerializableExtra("record");
            edtAddress.setText(visit.getAddress());
            edtAddress.setEnabled(false);
            edtAddressCep.setText(visit.getCep());
            edtAddressNumber.setText(visit.getNumber());
            edtAddressComplement.setText(visit.getComplement());
            edtAddressNeighborhood.setText(visit.getNeighborhood());
            edtAddressNeighborhood.setEnabled(false);
            edtAddressCity.setText(visit.getCity());
            edtAddressCity.setEnabled(false);
            edtAddressState.setText(visit.getState());
            edtAddressState.setEnabled(false);
            edtAddressReferencePoint.setText(visit.getReferencePoint());
        } else {

        }

        // gallery
        galleryContainer = (GridView) findViewById(R.id.galley_container);
        images  = new ArrayList<VisitImage>();
        adapter = new ImageAdapter(this, images);
        galleryContainer.setAdapter(adapter);

        // actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // float button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Em desenvolvimento.", Toast.LENGTH_SHORT).show();

            }
        });

        // Make sure that GPS is enabled on the device
        tracker = new GpsTracker(this, mLocationListener);
        isGpsEnabled();

        // spinner
        spinner = (MultiSelectSpinner) findViewById(R.id.situations_spinner);

        // database
        mDbHelper = new DbHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isGpsEnabled();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras           = data.getExtras();
            File imageFile          = ImageUtils.saveImage((Bitmap) extras.get("data"));
            VisitImage visitImage  = new VisitImage(imageFile.getName(), imageFile.getAbsolutePath());

            images.add(visitImage);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * checking if GPS is enabled to getting location
     *
     * @return void
     */
    private void isGpsEnabled() {
        if (mLocation == null) {
            if (tracker.canGetLocation()) {
                tracker.getLocation(); // get gps location
                showLocationProgressDialog(); // show location dialog
            } else {
                tracker.showSettingsAlert(); // show request gps enable dialog
            }
        }
    }

    /**
     * build and show progress dialog
     *
     * @return void
     */
    private void showLocationProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("localizando sua posição...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * Listeners for gps tracking
     */
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if (location != null && location.getLatitude() != 0) {
                // store location data
                mLocation = location;

                // hide dialog
                dialog.dismiss();

                // stop gps tracking
                tracker.stopUsingGps();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            //your code here
        }

        @Override
        public void onProviderDisabled(String provider) {
            //your code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //your code here
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_form;
    }
}
