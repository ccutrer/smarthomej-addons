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
package org.smarthomej.binding.amazonechocontrol.internal;

import static org.smarthomej.binding.amazonechocontrol.internal.AmazonEchoControlBindingConstants.*;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.openhab.core.storage.Storage;
import org.openhab.core.storage.StorageService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpService;
import org.smarthomej.binding.amazonechocontrol.internal.handler.AccountHandler;
import org.smarthomej.binding.amazonechocontrol.internal.handler.EchoHandler;
import org.smarthomej.binding.amazonechocontrol.internal.handler.FlashBriefingProfileHandler;
import org.smarthomej.binding.amazonechocontrol.internal.handler.SmartHomeDeviceHandler;
import org.smarthomej.commons.SimpleDynamicCommandDescriptionProvider;
import org.smarthomej.commons.SimpleDynamicStateDescriptionProvider;

import com.google.gson.Gson;

/**
 * The {@link AmazonEchoControlHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Michael Geramb - Initial contribution
 */
@Component(service = { ThingHandlerFactory.class,
        AmazonEchoControlHandlerFactory.class }, configurationPid = "binding.amazonechocontrol")
@NonNullByDefault
public class AmazonEchoControlHandlerFactory extends BaseThingHandlerFactory {
    private final Set<AccountHandler> accountHandlers = new HashSet<>();
    private final HttpService httpService;
    private final StorageService storageService;

    private final BindingServlet bindingServlet;
    private final Gson gson;
    private final HttpClient httpClient;
    private final SimpleDynamicCommandDescriptionProvider dynamicCommandDescriptionProvider;
    private final SimpleDynamicStateDescriptionProvider dynamicStateDescriptionProvider;

    @Activate
    public AmazonEchoControlHandlerFactory(@Reference HttpService httpService, @Reference StorageService storageService,
            @Reference SimpleDynamicCommandDescriptionProvider dynamicCommandDescriptionProvider,
            @Reference SimpleDynamicStateDescriptionProvider dynamicStateDescriptionProvider) throws Exception {
        this.storageService = storageService;
        this.httpService = httpService;
        this.gson = new Gson();
        this.dynamicCommandDescriptionProvider = dynamicCommandDescriptionProvider;
        this.dynamicStateDescriptionProvider = dynamicStateDescriptionProvider;
        this.httpClient = new HttpClient(new SslContextFactory.Client());
        this.bindingServlet = new BindingServlet(httpService);

        httpClient.start();
    }

    @Deactivate
    @SuppressWarnings("unused")
    public void deactivate() throws Exception {
        httpClient.stop();
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_ECHO_THING_TYPES_UIDS.contains(thingTypeUID)
                || SUPPORTED_SMART_HOME_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected void deactivate(ComponentContext componentContext) {
        bindingServlet.dispose();
        super.deactivate(componentContext);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_ACCOUNT)) {
            Storage<String> storage = storageService.getStorage(thing.getUID().toString(),
                    String.class.getClassLoader());
            AccountHandler bridgeHandler = new AccountHandler((Bridge) thing, httpService, storage, gson, httpClient);
            accountHandlers.add(bridgeHandler);
            bindingServlet.addAccountThing(thing);
            return bridgeHandler;
        } else if (thingTypeUID.equals(THING_TYPE_FLASH_BRIEFING_PROFILE)) {
            Storage<String> storage = storageService.getStorage(thing.getUID().toString(),
                    String.class.getClassLoader());
            return new FlashBriefingProfileHandler(thing, storage, dynamicCommandDescriptionProvider);
        } else if (SUPPORTED_ECHO_THING_TYPES_UIDS.contains(thingTypeUID)) {
            return new EchoHandler(thing, gson, dynamicCommandDescriptionProvider, dynamicStateDescriptionProvider);
        } else if (SUPPORTED_SMART_HOME_THING_TYPES_UIDS.contains(thingTypeUID)) {
            return new SmartHomeDeviceHandler(thing, gson, dynamicCommandDescriptionProvider,
                    dynamicStateDescriptionProvider);
        }
        return null;
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof AccountHandler) {
            accountHandlers.remove(thingHandler);
            BindingServlet bindingServlet = this.bindingServlet;
            bindingServlet.removeAccountThing(thingHandler.getThing());
        }
    }

    public Set<AccountHandler> getAccountHandlers() {
        return accountHandlers;
    }
}
