// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.settingsui.fragments;

import static com.hp.jetadvantage.link.services.settingsui.fragments.ScanConfigurationFragment.PREF_EMAIL_SMTP;

import androidx.annotation.Nullable;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hp.jetadvantage.link.api.scanner.SmtpAttributes;
import com.hp.jetadvantage.link.services.settingsui.R;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

public class EmailSmtpSettingFragment extends DialogFragment {
    private static final String TAG = "EmailSmtpSettingFragment";
    private static final int DEFAULT_PORT = 25;
    private static final int DEFAULT_TIMEOUT = 60;

    Button btnOK;
    Button btnCancel;
    EditText edtHostname;
    EditText edtPort;
    EditText edtConnectionTimeout;
    EditText edtReadTimeout;
    EditText edtUsername;
    EditText edtPassword;
    RadioGroup radioTransportMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_email_smtp_setting, container, false);
        getDialog().setTitle(R.string.pref_email_smtp_title);
        setCancelable(false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedIstanceState) {
        super.onViewCreated(view, savedIstanceState);
        initView(view);
        loadSmtpSettings();
    }

    private void initView(View view) {
        btnOK = view.findViewById(R.id.btn_ok);
        btnCancel = view.findViewById(R.id.btn_cancel);
        edtHostname = view.findViewById(R.id.edt_hostname);
        edtPort = view.findViewById(R.id.edt_port);
        edtConnectionTimeout = view.findViewById(R.id.edt_connection_timeout);
        edtReadTimeout = view.findViewById(R.id.edt_read_timeout);
        edtUsername = view.findViewById(R.id.edt_username);
        edtPassword = view.findViewById(R.id.edt_password);
        radioTransportMode = view.findViewById(R.id.radio_transport_mode);

        List<SmtpAttributes.TransportMode> transportModeList =
                new ArrayList<>(EnumSet.allOf(SmtpAttributes.TransportMode.class));

        for(SmtpAttributes.TransportMode transportMode: transportModeList){
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(transportMode.name());
            radioTransportMode.addView(radioButton);
        }


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveSmtpSettings();
                    setEnableSmtpPreference(true);
                    getDialog().dismiss();
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnableSmtpPreference(false);
                getDialog().cancel();
            }
        });
    }

    private void saveSmtpSettings() throws Exception{
        if(isValid()) {
            String hostname = edtHostname.getText().toString();
            String port = edtPort.getText().toString();
            String connectionTimeout = edtConnectionTimeout.getText().toString();
            String readTimeout = edtReadTimeout.getText().toString();
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            String transportMode = null;

            int radioButtonID = radioTransportMode.getCheckedRadioButtonId();
            View radioButton = radioTransportMode.findViewById(radioButtonID);
            if(radioButton instanceof RadioButton){
                transportMode = ((RadioButton) radioButton).getText().toString();
            }

            setSharedPreferenceData(getString(R.string.pref_email_hostname), hostname);
            setSharedPreferenceData(getString(R.string.pref_email_port), port);
            setSharedPreferenceData(getString(R.string.pref_email_connection_timeout), connectionTimeout);
            setSharedPreferenceData(getString(R.string.pref_email_read_timeout), readTimeout);
            setSharedPreferenceData(getString(R.string.pref_email_username), username);
            setSharedPreferenceData(getString(R.string.pref_email_password), password);
            setSharedPreferenceData(getString(R.string.pref_email_transport_mode), transportMode);
        }
    }

    private void loadSmtpSettings() {
        String hostname = getSharedPreferenceData(getString(R.string.pref_email_hostname), "");
        String port = getSharedPreferenceData(getString(R.string.pref_email_port), String.valueOf(DEFAULT_PORT));
        String connectionTimeout = getSharedPreferenceData(getString(R.string.pref_email_connection_timeout), String.valueOf(DEFAULT_TIMEOUT));
        String readTimeout = getSharedPreferenceData(getString(R.string.pref_email_read_timeout), String.valueOf(DEFAULT_TIMEOUT));
        String username = getSharedPreferenceData(getString(R.string.pref_email_username), "");
        String password = getSharedPreferenceData(getString(R.string.pref_email_password), "");
        String transportMode = getSharedPreferenceData(getString(R.string.pref_email_transport_mode), SmtpAttributes.TransportMode.PLAIN.name());

        edtHostname.setText(hostname);
        edtPort.setText(port);
        edtConnectionTimeout.setText(connectionTimeout);
        edtReadTimeout.setText(readTimeout);
        edtUsername.setText(username);
        edtPassword.setText(password);

        for(int i=0; i<radioTransportMode.getChildCount(); i++){
            View radio = radioTransportMode.getChildAt(i);
            if (radio instanceof RadioButton) {
                if(((RadioButton) radio).getText().equals(transportMode)){
                    ((RadioButton) radio).setChecked(true);
                }
            }
        }
    }

    private void setEnableSmtpPreference(boolean value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PREF_EMAIL_SMTP, value);
        editor.commit();
    }

    private void setSharedPreferenceData(String key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getSharedPreferenceData(String key, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return sharedPref.getString(key, defaultValue);
    }

    private boolean isValid() throws Exception{
        final int MINIMUM_PORT = 1;
        final int MAXIMUM_PORT = 65535;
        final int MINIMUM_TIMEOUT = 1;
        final int MAXIMUM_TIMEOUT = 300;

        String hostname = edtHostname.getText().toString();
        String port = edtPort.getText().toString();
        String connectionTimeout = edtConnectionTimeout.getText().toString();
        String readTimeout = edtReadTimeout.getText().toString();

        if(TextUtils.isEmpty(hostname)){
            throw new Exception("hostname is empty");
        }

        if(TextUtils.isEmpty(port)){
            throw new Exception("port is empty");
        } else {
            int portNum = Integer.parseInt(port);
            if(portNum < MINIMUM_PORT || portNum > MAXIMUM_PORT){
                throw new Exception(String.format(Locale.US, "SMTP port value must be in range [%d, %d] seconds",
                        MINIMUM_PORT, MAXIMUM_PORT));
            }
        }

        if(TextUtils.isEmpty(connectionTimeout)){
            throw new Exception("connectionTimeout is empty");
        } else {
            int connectionTimeoutNum = Integer.parseInt(connectionTimeout);
            if(connectionTimeoutNum < MINIMUM_TIMEOUT || connectionTimeoutNum > MAXIMUM_TIMEOUT){
                throw new Exception(String.format(Locale.US, "Connect Timeout value must be in range [%d, %d] seconds",
                        MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT));
            }
        }

        if(TextUtils.isEmpty(readTimeout)){
            throw new Exception("readTimeout is empty");
        } else {
            int readTimeoutNum = Integer.parseInt(readTimeout);
            if(readTimeoutNum < MINIMUM_TIMEOUT || readTimeoutNum > MAXIMUM_TIMEOUT){
                throw new Exception(String.format(Locale.US, "Connect Timeout value must be in range [%d, %d] seconds",
                        MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT));
            }
        }
        return true;
    }
}