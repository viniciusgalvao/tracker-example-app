package com.frevocomunicacao.tracker.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.frevocomunicacao.tracker.Constants;
import com.frevocomunicacao.tracker.R;
import com.frevocomunicacao.tracker.adapters.ImageAdapter;
import com.frevocomunicacao.tracker.database.contracts.ImageContract;
import com.frevocomunicacao.tracker.database.contracts.OcurrenceContract;
import com.frevocomunicacao.tracker.database.datasources.CheckinsDataSource;
import com.frevocomunicacao.tracker.database.datasources.ImagesDataSource;
import com.frevocomunicacao.tracker.database.datasources.OcurrencesDataSource;
import com.frevocomunicacao.tracker.database.datasources.VisitsDataSource;
import com.frevocomunicacao.tracker.database.models.Ocurrence;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // activity view mode
        viewMode = getIntent().getExtras().getString("mode");

        // actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // float button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSave();
            }
        });

        // form view
        edtAddressCep           = ((EditText) findViewById(R.id.edt_address_cep));
        edtAddress              = ((EditText) findViewById(R.id.edt_address));
        edtAddressNumber        = ((EditText) findViewById(R.id.edt_address_number));
        edtAddressComplement    = ((EditText) findViewById(R.id.edt_address_complement));
        edtAddressNeighborhood  = ((EditText) findViewById(R.id.edt_address_neighborhood));
        edtAddressCity          = ((EditText) findViewById(R.id.edt_address_city));
        edtAddressState         = ((EditText) findViewById(R.id.edt_address_state));
        edtAddressReferencePoint= ((EditText) findViewById(R.id.edt_address_reference_point));
        edtObservation          = ((EditText) findViewById(R.id.edt_observation));

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
            visit = new Visit(); // create empty object
        }

        // gallery
        galleryContainer = (GridView) findViewById(R.id.galley_container);
        buildImageGrid();


        // Make sure that GPS is enabled on the device
        tracker = new GpsTracker(this, mLocationListener);


        // spinner
        spinner = (MultiSelectSpinner) findViewById(R.id.situations_spinner);
        fillSpinner();

        if (visit.getVisitStatusId() == 3) {
            fab.setVisibility(View.GONE);
            edtAddressCep.setEnabled(false);
            edtAddressNumber.setEnabled(false);
            edtAddressComplement.setEnabled(false);
            edtAddressReferencePoint.setEnabled(false);
            edtObservation.setVisibility(View.GONE);
            spinner.setEnabled(false);
        } else {
            isGpsEnabled();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (visit.getVisitStatusId() != 3) {
            getMenuInflater().inflate(R.menu.form, menu);
        }

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

        if (visit.getVisitStatusId() != 3) {
            isGpsEnabled();
        }
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
            if (location != null) {
                // store location data
                mLocation = location;

                // hide dialog
                dialog.dismiss();

                // stop gps tracking
                //tracker.stopUsingGps();
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

    private void confirmSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(viewMode.equals("edit") ? "Salvar Visita" : "Criar Visita");
        builder.setMessage("Você tem certeza?");

        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                save();
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void save() {
        VisitsDataSource dsVisit     = new VisitsDataSource(this);
        ImagesDataSource dsImage     = new ImagesDataSource(this);
        CheckinsDataSource dsCheckin = new CheckinsDataSource(this);

        visit.setVisitStatusId(3); // 3 = Visited
        visit.setEmployeeId(prefs.getInt(Constants.PREFS_KEY_USER_EMPLOYEE_ID));
        visit.setCep(edtAddressCep.getText().toString());
        visit.setAddress(edtAddress.getText().toString());
        visit.setNumber(edtAddressNumber.getText().toString());
        visit.setComplement(edtAddressComplement.getText().toString());
        visit.setNeighborhood(edtAddressNeighborhood.getText().toString());
        visit.setCity(edtAddressCity.getText().toString());
        visit.setState(edtAddressState.getText().toString());
        visit.setReferencePoint(edtAddressReferencePoint.getText().toString());
//        visit.setObservation(edtObservation.getText().toString());

        if (viewMode.equals("edit")) {
            dsVisit.update(visit);
        } else {
            visit.setVisitTypeId(2); // set to extra visit type
            dsVisit.create(visit); // create visit
        }

        // VisitImages
        for (int x = 0; x < images.size(); x++) {
            VisitImage img = images.get(x);
            img.setVisitId((int) visit.getId());

            dsImage.create(img);
        }

        changeActivity(MainActivity.class, null, true);
    }

    private void buildImageGrid() {
        ImagesDataSource dsImage = new ImagesDataSource(this);

        String query    = ImageContract.ImageEntry.COLUMN_FIELD_VISIT_ID + " = ?";
        String[] args   = new String[]{String.valueOf((int) visit.getId())};

        images          = dsImage.findAll(query, args, null);

        images          = images == null ? new ArrayList<VisitImage>() : images;
        adapter         = new ImageAdapter(this, images);
        galleryContainer.setAdapter(adapter);
    }

    private void fillSpinner() {
        OcurrencesDataSource dsOcurrence    = new OcurrencesDataSource(getApplicationContext());
        List<Ocurrence> ocurrencesRecords   = dsOcurrence.findAll(null, null, OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_ID + " ASC");
        List<String> spinnerData            = new ArrayList<String>();

        for (int x = 0;x < ocurrencesRecords.size();x++) {
            spinnerData.add(ocurrencesRecords.get(x).getName());
        }

        // fill data in spinner
        spinner.setItems(spinnerData);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_form;
    }
}
