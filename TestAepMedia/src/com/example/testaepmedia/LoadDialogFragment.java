package com.example.testaepmedia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class LoadDialogFragment extends DialogFragment {

	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.load_dialog_layout, null);
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setView(view);
		return builder.create();

	}
}
