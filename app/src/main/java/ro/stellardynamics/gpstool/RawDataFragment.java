package ro.stellardynamics.gpstool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ro.stellardynamics.gpstool.databinding.FragmentRawDataBinding;

public class RawDataFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRawDataBinding binding = FragmentRawDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
