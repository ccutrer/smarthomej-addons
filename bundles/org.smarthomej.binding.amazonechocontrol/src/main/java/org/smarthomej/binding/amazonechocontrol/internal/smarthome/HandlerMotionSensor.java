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
package org.smarthomej.binding.amazonechocontrol.internal.smarthome;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.UnDefType;
import org.smarthomej.binding.amazonechocontrol.internal.connection.Connection;
import org.smarthomej.binding.amazonechocontrol.internal.handler.SmartHomeDeviceHandler;
import org.smarthomej.binding.amazonechocontrol.internal.jsons.JsonSmartHomeCapability;
import org.smarthomej.binding.amazonechocontrol.internal.jsons.JsonSmartHomeDevice;

import com.google.gson.JsonObject;

/**
 * The {@link HandlerMotionSensor} is responsible for the Alexa.MotionSensor interface
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
public class HandlerMotionSensor extends AbstractInterfaceHandler {
    public static final String INTERFACE = "Alexa.MotionSensor";

    private static final ChannelInfo MOTION_DETECTED_STATE = new ChannelInfo("detectionState", "detectionState",
            Constants.CHANNEL_TYPE_MOTION_DETECTED);

    public HandlerMotionSensor(SmartHomeDeviceHandler smartHomeDeviceHandler) {
        super(smartHomeDeviceHandler, List.of(INTERFACE));
    }

    @Override
    protected Set<ChannelInfo> findChannelInfos(JsonSmartHomeCapability capability, @Nullable String property) {
        if (MOTION_DETECTED_STATE.propertyName.equals(property)) {
            return Set.of(MOTION_DETECTED_STATE);
        }
        return Set.of();
    }

    @Override
    public void updateChannels(String interfaceName, List<JsonObject> stateList, UpdateChannelResult result) {
        OnOffType motionDetectedValue = null;
        for (JsonObject state : stateList) {
            if (MOTION_DETECTED_STATE.propertyName.equals(state.get("name").getAsString())) {
                String value = state.get("value").getAsString();
                switch (value) {
                    case "NOT_DETECTED":
                        motionDetectedValue = OnOffType.OFF;
                        break;
                    case "DETECTED":
                        motionDetectedValue = OnOffType.ON;
                        break;
                }
            }
        }
        smartHomeDeviceHandler.updateState(MOTION_DETECTED_STATE.channelId,
                motionDetectedValue == null ? UnDefType.UNDEF : motionDetectedValue);
    }

    @Override
    public boolean handleCommand(Connection connection, JsonSmartHomeDevice shd, String entityId,
            List<JsonSmartHomeCapability> capabilities, String channelId, Command command)
            throws IOException, InterruptedException {
        return false;
    }
}
