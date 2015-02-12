package com.example.dkalita.signin.signin;

import android.database.Observable;
import android.os.AsyncTask;
import android.util.Log;
import com.example.dkalita.signin.communicator.BackendCommunicator;
import com.example.dkalita.signin.communicator.CommunicatorFactory;

public class SignInModel {
	private static final String TAG = "SignInModel";

	private final SignInObservable mObservable = new SignInObservable();
	private SignInTask mSignInTask;
	private boolean mIsWorking;

	public SignInModel() {
		Log.i(TAG, "new Instance");
	}

	public void signIn(final String userName, final String password) {
		if (mIsWorking) {
			return;
		}

		mObservable.notifyStarted();

		mIsWorking = true;
		mSignInTask = new SignInTask(userName, password);
		mSignInTask.execute();
	}

	public void stopSignIn() {
		if (mIsWorking) {
			mSignInTask.cancel(true);
			mIsWorking = false;
		}
	}

	public void registerObserver(final Observer observer) {
		mObservable.registerObserver(observer);
		if (mIsWorking) {
			observer.onSignInStarted(this);
		}
	}

	public void unregisterObserver(final Observer observer) {
		mObservable.unregisterObserver(observer);
	}

	private class SignInTask extends AsyncTask<Void, Void, Boolean> {
		private String mUserName;
		private String mPassword;

		public SignInTask(final String userName, final String password) {
			mUserName = userName;
			mPassword = password;
		}

		@Override
		protected Boolean doInBackground(final Void... params) {
			final BackendCommunicator communicator = CommunicatorFactory.createBackendCommunicator();

			try {
				return communicator.postSignIn(mUserName, mPassword);
			} catch (InterruptedException e) {
				Log.i(TAG, "Sign in interrupted");
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mIsWorking = false;

			if (success) {
				mObservable.notifySucceeded();
			} else {
				mObservable.notifyFailed();
			}
		}
	}

	public interface Observer {
		void onSignInStarted(SignInModel signInModel);

		void onSignInSucceeded(SignInModel signInModel);

		void onSignInFailed(SignInModel signInModel);
	}

	private class SignInObservable extends Observable<Observer> {
		public void notifyStarted() {
			for (final Observer observer : mObservers) {
				observer.onSignInStarted(SignInModel.this);
			}
		}

		public void notifySucceeded() {
			for (final Observer observer : mObservers) {
				observer.onSignInSucceeded(SignInModel.this);
			}
		}

		public void notifyFailed() {
			for (final Observer observer : mObservers) {
				observer.onSignInFailed(SignInModel.this);
			}
		}
	}
}
