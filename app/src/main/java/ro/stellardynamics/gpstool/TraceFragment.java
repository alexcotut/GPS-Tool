package ro.stellardynamics.gpstool;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import ro.stellardynamics.gpstool.databinding.FragmentTraceListBinding;

public class TraceFragment extends Fragment {

    private FragmentTraceListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTraceListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnNewTrace.setOnClickListener(v -> {
            NewTraceBottomSheet sheet = new NewTraceBottomSheet();
            sheet.setResultListener(name -> {
                if (name == null || name.length() == 0) {
                    Snackbar.make(binding.btnNewTrace, "Please enter a name", BaseTransientBottomBar.LENGTH_LONG)
                            .show();
                    return;
                }
                Intent intent = new Intent(requireActivity(), TraceActivity.class);
                startActivity(intent);
            });
            sheet.show(getParentFragmentManager(), NewTraceBottomSheet.TAG);
        });
    }


}
