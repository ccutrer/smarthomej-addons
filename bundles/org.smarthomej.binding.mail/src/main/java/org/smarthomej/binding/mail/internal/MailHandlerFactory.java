/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.smarthomej.binding.mail.internal;

import static org.smarthomej.binding.mail.internal.MailBindingConstants.*;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.smarthomej.commons.transform.ValueTransformationProvider;

/**
 * The {@link MailHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.mail", service = ThingHandlerFactory.class)
public class MailHandlerFactory extends BaseThingHandlerFactory {

    private final ValueTransformationProvider valueTransformationProvider;

    @Activate
    public MailHandlerFactory(@Reference ValueTransformationProvider valueTransformationProvider) {
        this.valueTransformationProvider = valueTransformationProvider;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_SMTPSERVER.equals(thingTypeUID)) {
            return new SMTPHandler(thing);
        } else if (THING_TYPE_IMAPSERVER.equals(thingTypeUID) || THING_TYPE_POP3SERVER.equals(thingTypeUID)) {
            return new POP3IMAPHandler(thing, valueTransformationProvider);
        }

        return null;
    }
}
