package com.inuker.bluetooth.library.connect.request;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.inuker.bluetooth.library.connect.listener.WriteCharacterListener;
import com.inuker.bluetooth.library.connect.response.BleGeneralResponse;
import static com.inuker.bluetooth.library.Constants.*;

import java.util.UUID;

public class BleWriteRequest extends BleRequest implements WriteCharacterListener {

    public BleWriteRequest(String mac, UUID service, UUID character, byte[] bytes,
                           BleGeneralResponse response) {
        super(mac, response);
        mServiceUUID = service;
        mCharacterUUID = character;
        mBytes = bytes;
    }

    @Override
    void processRequest() {
        switch (getConnectStatus()) {
            case STATUS_DEVICE_SERVICE_READY:
                if (write(mServiceUUID, mCharacterUUID, mBytes)) {
                    registerGattResponseListener(this);
                } else {
                    onRequestFinished(REQUEST_FAILED);
                }
                break;

            default:
                onRequestFinished(REQUEST_FAILED);
                break;
        }
    }

    @Override
    int getGattResponseListenerId() {
        return GATT_RESP_CHARACTER_WRITE;
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
        if (!checkCharacteristic(characteristic)) {
            return;
        }

        if (status == BluetoothGatt.GATT_SUCCESS) {
            onRequestFinished(REQUEST_SUCCESS);
        } else {
            onRequestFinished(REQUEST_FAILED);
        }
    }

    boolean write(UUID service, UUID character, byte[] value) {
        return writeCharacteristic(service, character, value);
    }

    private boolean checkCharacteristic(BluetoothGattCharacteristic characteristic) {
        return characteristic != null && mCharacterUUID.equals(characteristic.getUuid())
                && characteristic.getService() != null && mServiceUUID.equals(characteristic.getService().getUuid());
    }
}
