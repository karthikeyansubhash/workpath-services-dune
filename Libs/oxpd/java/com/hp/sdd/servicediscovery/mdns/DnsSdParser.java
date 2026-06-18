// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.mdns;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DnsSdParser {
    private static final String TAG = DnsSdParser.class.getSimpleName();

    private static final char SEPARATOR = '=';

    private DnsPacket packet;

    public DnsService[] parse(DnsPacket aPacket) throws DnsException {
        this.packet = aPacket;
        return this.parseServices();
    }

    private DnsService[] parseServices() throws DnsException {
        ArrayList<DnsService> serviceList = new ArrayList<DnsService>();

        boolean hasPTREntries = false;
        for (DnsPacket.Entry answerEntry : this.packet.getAnswers()) {
            if (DnsPacket.ResourceType.PTR == answerEntry.getType()) {
                hasPTREntries = true;
                DnsPacket.Ptr ptr = (DnsPacket.Ptr) answerEntry;

                try {
                    serviceList.add(this.buildService(ptr));
                } catch (DnsSdException ignore) {
                    Log.d(TAG,String.format("Just got a PTR for %s but not SRV/TXT. Try to resolve service.",ptr.getPointedName().toString()));
                    DnsService service = this.buildToResolveService(ptr);
                    serviceList.add(service);
                }
            }



        }
        if(!hasPTREntries && this.packet.getQuestions().length==1) {
            serviceList.add(getResolvedService());
        }
        return serviceList.toArray(new DnsService[serviceList.size()]);
    }

    private DnsService getResolvedService() throws DnsException{
        //we are getting a Resolve packet instead of a discover one.
        DnsPacket.Name serviceName = this.packet.getQuestions()[0].getName();
        DnsPacket.Srv srvEntry=null;
        DnsPacket.Txt txtEntry = null;

        //get the srv and txt records for the resolve
        for (DnsPacket.Entry answer: this.packet.getAnswers()) {
            if (DnsPacket.ResourceType.SRV == answer.getType()) {
                DnsPacket.Srv srv = (DnsPacket.Srv) answer;
                if (srv.getName().equals(serviceName)) {
                    srvEntry = srv;
                }
            }
            if (DnsPacket.ResourceType.TXT == answer.getType()) {
                DnsPacket.Txt txt = (DnsPacket.Txt) answer;
                if (txt.getName().equals(serviceName)) {
                    txtEntry = txt;
                }
            }
        }

        if (srvEntry != null && txtEntry != null ){
            DnsPacket.Name hostname = srvEntry.getTarget();
            int port = srvEntry.getPort();
            DnsPacket.Address[] addressEntries = findAddresses(hostname);
            Map<String, byte[]> attributes = parseAttributes(txtEntry.getText());
            byte[][] addresses = new byte[addressEntries.length][];

            for (int i = 0; i < addressEntries.length; i++) {
                addresses[i] = addressEntries[i].getAddress();
            }
            return new DnsService(serviceName, serviceName, hostname, addresses, port, attributes);
        }
        throw new DnsSdException("Service does not contain correspondent srv and txt entry.");
    }

    private DnsService buildToResolveService(DnsPacket.Ptr ptr){

        DnsPacket.Name serviceNameSuffix = ptr.getName();
        DnsPacket.Name serviceName = ptr.getPointedName();
        byte[][] addresses = {};
        Map<String, byte[]> attributes = Collections.emptyMap();
        DnsPacket.Name hostname = new DnsPacket.Name() {
            @Override
            public String[] getLabels() {
                return new String[0];
            }

            @Override
            public byte[] getBytes() {
                return new byte[0];
            }
        };

        return new DnsService(serviceName, serviceNameSuffix, hostname, addresses, 0, attributes);


    }

    private DnsService buildService(DnsPacket.Ptr ptr) throws DnsSdException {
        DnsPacket.Name serviceNameSuffix = ptr.getName();
        DnsPacket.Name serviceName = ptr.getPointedName();
        DnsPacket.Srv srvEntry = this.findSrv(serviceName);
        DnsPacket.Txt txtEntry = this.findTxt(serviceName);
        DnsPacket.Name hostname = srvEntry.getTarget();
        DnsPacket.Address[] addressEntries = this.findAddresses(hostname);
        int port = srvEntry.getPort();
        Map<String, byte[]> attributes = parseAttributes(txtEntry.getText());
        byte[][] addresses = new byte[addressEntries.length][];

        for (int i = 0; i < addressEntries.length; i++) {
            addresses[i] = addressEntries[i].getAddress();
        }
        return new DnsService(serviceName, serviceNameSuffix, hostname, addresses, port, attributes);
    }

    private DnsPacket.Srv findSrv(DnsPacket.Name serviceName) throws DnsSdException {
        for (DnsPacket.Entry additionalEntry : this.packet.getAdditionals()) {
            if (DnsPacket.ResourceType.SRV == additionalEntry.getType()) {
                DnsPacket.Srv srv = (DnsPacket.Srv) additionalEntry;

                if (srv.getName().equals(serviceName)) {
                    return srv;
                }
            }
        }
        throw new DnsSdException("Service does not contain correspondent srv entry.");
    }

    private DnsPacket.Txt findTxt(DnsPacket.Name serviceName) throws DnsSdException {
        for (DnsPacket.Entry additionalEntry : this.packet.getAdditionals()) {
            if (DnsPacket.ResourceType.TXT == additionalEntry.getType()) {
                DnsPacket.Txt txt = (DnsPacket.Txt) additionalEntry;

                if (txt.getName().equals(serviceName)) {
                    return txt;
                }
            }
        }
        throw new DnsSdException("Service does not contain correspondent txt entry.");
    }

    private DnsPacket.Address[] findAddresses(DnsPacket.Name hostname) throws DnsSdException {
        ArrayList<DnsPacket.Address> addressEntries = new ArrayList<DnsPacket.Address>();

        for (DnsPacket.Entry additionalEntry : this.packet.getAdditionals()) {
            if ((DnsPacket.ResourceType.A == additionalEntry.getType())
                    || (DnsPacket.ResourceType.AAAA == additionalEntry.getType())) {
                DnsPacket.Address address = (DnsPacket.Address) additionalEntry;

                if (address.getName().equals(hostname)) {
                    addressEntries.add(address);
                }
            }
        }
        if (addressEntries.isEmpty()) {
            throw new DnsSdException("Service does not contain correspondent address entry.");
        }
        return addressEntries.toArray(new DnsPacket.Address[addressEntries.size()]);
    }

    public static Map<String, byte[]> parseAttributes(byte[] txtData) {
        Map<String, byte[]> attributes = new HashMap<String,byte[]>();
        int offset = 0;

        while (offset < txtData.length) {
            int attrLength = (int)txtData[offset++] & 0xff;
            int sepIndex;
            int keyLength;
            String key;
            byte[] value = null;

            if ((attrLength < 0) || ((offset + attrLength) > txtData.length)) {
                return attributes;
            }
            sepIndex = findSeparator(txtData, offset, attrLength);
            keyLength = (sepIndex > 0) ? (sepIndex - offset) : attrLength;
            if (keyLength == 0) {
                return attributes;
            }
            try {
                key = new String(txtData, offset, keyLength, "US-ASCII");
            } catch (Exception e) {
                Log.e(TAG, "Exception: parsing attribute: " + e);
                e.printStackTrace();
                continue;
            }

            if (sepIndex > 0) {
                int valueLength = (attrLength - keyLength - 1);

                value = new byte[valueLength];
                System.arraycopy(txtData, sepIndex + 1, value, 0, valueLength);
            }
            attributes.put(key, value);
            offset += attrLength;
        }
        return attributes;
    }

    private static int findSeparator(byte[] txtData, int offset, int length) {
        for (int i = offset; i < (offset + length); i++) {
            if (txtData[i] == (byte) SEPARATOR) {
                return i;
            }
        }
        return -1;
    }
}
