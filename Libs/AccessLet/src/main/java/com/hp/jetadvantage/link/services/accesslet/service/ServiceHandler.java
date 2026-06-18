package com.hp.jetadvantage.link.services.accesslet.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.hp.jetadvantage.link.api.access.AbstractAuthenticationService;
import com.hp.jetadvantage.link.api.access.Accesslet;
import com.hp.jetadvantage.link.api.access.Principal;
import com.hp.jetadvantage.link.common.utils.SLog;
import java.util.ArrayList;

public class ServiceHandler extends Handler {
    private static final String TAG = "ServiceHandler";
    private final Context mContext;
    private final AuthenticationServiceManager mManager;

    public static final int MSG_START = 1;
    public static final int MSG_SEND_SIGNIN = 2;
    public static final int MSG_SEND_SIGNOUT = 3;
    public static final int MSG_PRE_PROMPT = 4;
    public static final String MSG_DATA_INTENT = "dataIntent";
    public static final String MSG_DATA_PRINCIPAL = "dataPrincipal";
    public static final String MSG_DATA_AUTHPKG = "authPackage";

    public ServiceHandler(Looper looper, Context context, AuthenticationServiceManager manager) {
        super(looper);
        mContext = context;
        mManager = manager;
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case MSG_START:
                    SLog.d(TAG, "Start auth svc for event monitoring.");
                    mManager.registerNotificationCallback();
                    break;
                case MSG_SEND_SIGNIN:
                    Bundle data = (Bundle) msg.obj;
                    Intent intent = data.getParcelable(MSG_DATA_INTENT);
                    Principal principal = data.getParcelable(MSG_DATA_PRINCIPAL);
                    mContext.bindService(intent, new AuthServiceConnection(msg.arg1, principal), Context.BIND_AUTO_CREATE);
                    break;
                case MSG_SEND_SIGNOUT:
                    Bundle signoutData = (Bundle) msg.obj;
                    Intent signoutIntent = signoutData.getParcelable(MSG_DATA_INTENT);
                    if (signoutData.containsKey(MSG_DATA_AUTHPKG)) {
                        ArrayList<String> authPkgs = signoutData.getStringArrayList(MSG_DATA_AUTHPKG);
                        for (String pkg : authPkgs) {
                            SLog.d(TAG, "Send signout for authagent.");
                            signoutIntent.setPackage(pkg);
                            mContext.bindService(signoutIntent, new AuthServiceConnection(msg.arg1, null), Context.BIND_AUTO_CREATE);
                        }
                    } else {
                        SLog.d(TAG, "Send signout to all.");
                        mContext.bindService(signoutIntent, new AuthServiceConnection(msg.arg1, null), Context.BIND_AUTO_CREATE);
                    }
                    break;
                case MSG_PRE_PROMPT:
                    Bundle promptData = (Bundle) msg.obj;
                    Intent promptIntent = promptData.getParcelable(MSG_DATA_INTENT);
                    mContext.bindService(promptIntent, new AuthServiceConnection(msg.arg1, null), Context.BIND_AUTO_CREATE);
                    break;
                default:
            }
        } catch (Exception e) {
            SLog.e(TAG, "Failed to handle message " + msg.what, e);
        }
    }

    private class AuthServiceConnection implements ServiceConnection {
        private final int messageCode;
        private final Principal principal;

        AuthServiceConnection(int messageCode, Principal principal) {
            this.messageCode = messageCode;
            this.principal = principal;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger mMessenger = new Messenger(service);
            Message msg = Message.obtain(null, messageCode, 0, 0);

            if (principal != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Accesslet.PRINCIPAL_EXTRA, principal);
                msg.setData(bundle);
            }

            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                mContext.unbindService(this);
            } catch (Exception e) {
                SLog.e(TAG, "Failed to request unbind svc " + e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                mContext.unbindService(this);
            } catch (Exception e) {
                SLog.e(TAG, "Failed to request unbind svc " + e.getMessage());
            }
        }
    }
}

