package ro.stellardynamics.gpstool;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ro.stellardynamics.gpstool.databinding.FragmentStartTraceBinding;

public class NewTraceBottomSheet extends BottomSheetDialogFragment {
    public static final String TAG = "NewTraceBottomSheet";

    private FragmentStartTraceBinding binding;
    private TraceDialogListener listener = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialogInterface -> {
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(true);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartTraceBinding.inflate(inflater, container, false);

        binding.btnCancel.setOnClickListener(view -> dismiss());
        binding.btnOk.setOnClickListener(view -> {
            if (listener != null) {
                assert binding.txtName.getEditText() != null;
                listener.onResult(binding.txtName.getEditText().getText().toString());
            }
            this.dismiss();
        });

        return binding.getRoot();
    }

    public void setResultListener(TraceDialogListener listener) {
        this.listener = listener;
    }

    interface TraceDialogListener {
        void onResult(String name);
    }
}
