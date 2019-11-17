package com.flash3388.flashview.deploy;

public class Remote {

    private final String mUsername;
    private final String mPassword;
    private final String mHostname;

    public Remote(String username, String password, String hostname) {
        mUsername = username;
        mPassword = password;
        mHostname = hostname;
    }

    public String getHostname() {
        return mHostname;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getUsername() {
        return mUsername;
    }
}
