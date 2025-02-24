/**
 * Copyright (c) 2021-2022 Contributors to the SmartHome/J project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.smarthomej.binding.tuya.internal.local.dto;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link DiscoveryMessage} represents the UDP discovery messages sent by Tuya devices
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
public class DiscoveryMessage {
    public String ip = "";
    @SerializedName("gwId")
    public String deviceId = "";
    public int active = 0;
    @SerializedName(value = "ability", alternate = { "ablilty" })
    public int ability = 0;
    public int mode = 0;
    public boolean encrypt = true;
    public String productKey = "";
    public String version = "";

    @Override
    public String toString() {
        return "DiscoveryMessage{ip='" + ip + "', deviceId='" + deviceId + "', active=" + active + ", ability="
                + ability + ", mode=" + mode + ", encrypt=" + encrypt + ", productKey='" + productKey + "', version='"
                + version + "'}";
    }
}
