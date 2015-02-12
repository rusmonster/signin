package com.example.dkalita.signin.signin;

import android.app.Fragment;
import android.os.Bundle;

public class SignInWorkerFragment extends Fragment {
	private final SignInModel mSignInModel;

	public SignInWorkerFragment() {
		mSignInModel = new SignInModel();
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public SignInModel getSignInModel() {
		return mSignInModel;
	}
}
