package com.example.dkalita.signin.communicator;

public interface BackendCommunicator {
	boolean postSignIn(String userName, String password) throws InterruptedException;
}
