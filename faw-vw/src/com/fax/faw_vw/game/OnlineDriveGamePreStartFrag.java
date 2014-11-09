package com.fax.faw_vw.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;

public class OnlineDriveGamePreStartFrag extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.online_drive_game_pre_start, container, false);
		view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
				startActivity(new Intent(context, OnlineDriveGameActivity.class));
			}
		});
		return view;
	}

}
