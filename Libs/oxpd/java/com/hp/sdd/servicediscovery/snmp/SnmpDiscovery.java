// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.snmp;

import android.content.Context;

import com.hp.sdd.servicediscovery.IDiscovery;
import com.hp.sdd.servicediscovery.NetworkUtils;
import com.hp.sdd.servicediscovery.ServiceParser;

import java.net.DatagramPacket;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class SnmpDiscovery implements IDiscovery {

	public static final byte[] SNMP_PKT = {
			0x30, 0x39,
			0x02, 0x01, 0x00,       // version: version-1 (0)
			// community: public
			0x04, 0x06, 0x70, 0x75, 0x62, 0x6c, 0x69, 0x63,
			(byte)0xa0, 0x2c,
			0x02, 0x01, 0x01,       // request-id: 1
			0x02, 0x01, 0x00,       // error-status: noError (0)
			0x02, 0x01, 0x00,       // error-index: 0
			0x30, 0x21,
			// SNMPv2-MIB::sysName.0
			// 1.3.6.1.2.1.1.5.0
			0x30, 0x0c,
			0x06, 0x08, 0x2b, 0x06, 0x01, 0x02, 0x01, 0x01, 0x05, 0x00,
			0x05, 0x00,
			// SNMPv2-SMI::enterprises.11.2.3.9.1.1.7.0
			// 1.3.6.1.4.1.11.2.3.9.1.1.7.0
			0x30, 0x11,
			0x06, 0x0d, 0x2b, 0x06, 0x01, 0x04, 0x01, 0x0b, 0x02, 0x03, 0x09, 0x01, 0x01, 0x07, 0x00,
			0x05, 0x00
	};


	//	private WifiUtils wifi;
	private final Context mContext;

	private final boolean misFallback;

	public SnmpDiscovery(Context context, boolean fallbackDiscovery) {
		mContext = context;
		misFallback = fallbackDiscovery;
	}

	public SnmpDiscovery(Context context) {
		this(context, true);
	}

	@Override
	public void clear() {
	}

	@Override
	public DatagramPacket[] createQueryPackets() throws UnknownHostException {
		return new DatagramPacket[] {
			 new DatagramPacket(SNMP_PKT, SNMP_PKT.length, NetworkUtils.getBroadcastAddress(mContext), SnmpParser.SNMP_PORT) };
	}

	@Override
	public List<ServiceParser> parseResponse(DatagramPacket packet) {

		ArrayList<ServiceParser> networkDevices = new ArrayList<ServiceParser>(1);
		try {
			networkDevices.addAll(SnmpServiceParser.parse(packet));
		} catch (ProtocolException ignore) {
		}
		return networkDevices;
	}
    @Override
    public int getPort() {
        return SnmpParser.SNMP_PORT;
    }

	@Override
	public boolean isFallback() {
		return misFallback;
	}

}
