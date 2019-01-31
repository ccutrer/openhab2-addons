/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.homekit.internal.accessories;

import java.util.concurrent.CompletableFuture;

import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.library.items.RollershutterItem;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.openhab.io.homekit.internal.HomekitAccessoryUpdater;
import org.openhab.io.homekit.internal.HomekitTaggedItem;

import com.beowulfe.hap.HomekitCharacteristicChangeCallback;
import com.beowulfe.hap.accessories.WindowCovering;
import com.beowulfe.hap.accessories.properties.WindowCoveringPositionState;

/**
 *
 * @author epike - Initial contribution
 */
public class HomekitWindowCoveringImpl extends AbstractHomekitAccessoryImpl<RollershutterItem>
        implements WindowCovering {

    public HomekitWindowCoveringImpl(HomekitTaggedItem taggedItem, ItemRegistry itemRegistry,
            HomekitAccessoryUpdater updater) {
        super(taggedItem, itemRegistry, updater, RollershutterItem.class);
    }

    @Override
    public CompletableFuture<Integer> getCurrentPosition() {
        PercentType value = getItem().getStateAs(PercentType.class);
        if (value == null) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(100 - value.intValue());
    }

    @Override
    public CompletableFuture<WindowCoveringPositionState> getPositionState() {
        return CompletableFuture.completedFuture(WindowCoveringPositionState.STOPPED);
    }

    @Override
    public CompletableFuture<Integer> getTargetPosition() {
        return getCurrentPosition();
    }

    @Override
    public CompletableFuture<Void> setTargetPosition(int value) throws Exception {
        ((RollershutterItem) getItem()).send(new PercentType(100 - value));
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void subscribeCurrentPosition(HomekitCharacteristicChangeCallback callback) {
        getUpdater().subscribe(getItem(), callback);
    }

    @Override
    public void subscribePositionState(HomekitCharacteristicChangeCallback callback) {
        // Not implemented
    }

    @Override
    public void subscribeTargetPosition(HomekitCharacteristicChangeCallback callback) {
        getUpdater().subscribe(getItem(), "targetPosition", callback);
    }

    @Override
    public void unsubscribeCurrentPosition() {
        getUpdater().unsubscribe(getItem());
    }

    @Override
    public void unsubscribePositionState() {
        // Not implemented
    }

    @Override
    public void unsubscribeTargetPosition() {
        getUpdater().unsubscribe(getItem(), "targetPosition");
    }
}
