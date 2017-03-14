package com.inuker.bluetooth.library.model;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

import com.inuker.bluetooth.library.utils.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by dingjikerbo on 2016/9/5.
 */
public class BleGattProfile implements Parcelable {

    private List<BleGattService> services;

    public BleGattProfile() {

    }

    public BleGattProfile(Parcel in) {
        in.readTypedList(getServices(), BleGattService.CREATOR);
    }

    public void addServices(List<BleGattService> services) {
        Collections.sort(services);
        getServices().addAll(services);
    }

    public List<BleGattService> getServices() {
        if (services == null) {
            services = new ArrayList<BleGattService>();
        }
        return services;
    }

    public BleGattService getService(UUID serviceId) {
        if (serviceId == null) {
            return null;
        }

        List<BleGattService> services = getServices();
        for (BleGattService service : services) {
            if (service.getUUID().equals(serviceId)) {
                return service;
            }
        }

        return null;
    }

    public boolean containsCharacter(UUID serviceId, UUID characterId) {
        if (serviceId == null || characterId == null) {
            return false;
        }

        BleGattService service = getService(serviceId);
        if (service != null) {
            List<ParcelUuid> characters = service.getCharacters();
            if (!ListUtils.isEmpty(characters)) {
                for (ParcelUuid uuid : characters) {
                    if (characterId.equals(uuid.getUuid())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(getServices());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BleGattProfile> CREATOR = new Creator<BleGattProfile>() {
        @Override
        public BleGattProfile createFromParcel(Parcel in) {
            return new BleGattProfile(in);
        }

        @Override
        public BleGattProfile[] newArray(int size) {
            return new BleGattProfile[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BleGattService service : services) {
            sb.append(service).append("\n");
        }
        return sb.toString();
    }
}
