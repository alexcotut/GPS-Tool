package ro.stellardynamics.gpstool;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import java.util.Map;

import ro.stellardynamics.gpstool.databinding.ActivityTraceToolBinding;

public class TraceActivity extends AppCompatActivity {

    private boolean locationPermitted;
    private ActivityTraceToolBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityTraceToolBinding.inflate(getLayoutInflater());
        ViewCompat.setOnApplyWindowInsetsListener(binding.topPanel, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            MarginLayoutParams mlp = (MarginLayoutParams) v.getLayoutParams();
            mlp.topMargin = insets.top;
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomSheetInclude.bottomSheet, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            MarginLayoutParams mlp = (MarginLayoutParams) v.getLayoutParams();
            binding.bottomSheetInclude.bottomSheet.setPadding(
                    binding.bottomSheetInclude.bottomSheet.getPaddingStart(),
                    binding.bottomSheetInclude.bottomSheet.getPaddingTop(),
                    binding.bottomSheetInclude.bottomSheet.getPaddingEnd(),
                    binding.bottomSheetInclude.bottomSheet.getPaddingBottom() + insets.bottom
            );
            return WindowInsetsCompat.CONSUMED;
        });
        setContentView(binding.getRoot());

        binding.btnNavBack.setOnClickListener(view -> finish());

        binding.btnAdd.setOnClickListener(view -> {

        });


        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetInclude.bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.bottomAppBar.performShow();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        binding.bottomSheetInclude.btnClose.setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });

        binding.bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.mnu_config) {
                binding.bottomAppBar.performHide();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            }
            return false;
        });

        initMap();
        checkPermissions();
    }

    private void initMap() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        MapView mMapView = binding.map;

        mMapView.setMultiTouchControls(true);
        mMapView.setTilesScaledToDpi(true);

        ITileSource osmSource = new XYTileSource("HOT", 5, 20, 256, ".png",
                new String[]{"https://gpscom.ro/tiles/"},
                "© OpenStreetMap contributors");
        mMapView.setTileSource(osmSource);

        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        IMapController mapController = mMapView.getController();
        GeoPoint startPoint;
        startPoint = new GeoPoint(44.00, 26.00, 0);
        mapController.setCenter(startPoint);
        mapController.setZoom(18d);


    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private final ActivityResultLauncher<String[]> requestLocationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                for (Map.Entry<String, Boolean> e : result.entrySet()) {
                    if (!e.getValue()) locationPermitted = false;
                    return;
                }
                locationPermitted = true;
            });

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        binding.map.onPause();
    }
}
