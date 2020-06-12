/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.eventstream.internal;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.eventstream.Event;
import org.xwiki.eventstream.EventQuery;
import org.xwiki.eventstream.EventSearchResult;
import org.xwiki.eventstream.EventStatus;
import org.xwiki.eventstream.EventStore;
import org.xwiki.eventstream.EventStreamException;

/**
 * The default implementation of {@link EventStore} dispatching the event in the various enabled stores.
 * 
 * @version $Id$
 * @since 12.4RC1
 */
@Component
@Singleton
public class DefaultEventStore implements EventStore, Initializable
{
    private static final String NO_STORE = "No event store available";

    @Inject
    private EventStreamConfiguration configuration;

    @Inject
    private ComponentManager componentManager;

    private EventStore legacyStore;

    private EventStore store;

    @Override
    public void initialize() throws InitializationException
    {
        if (this.configuration.isEventStoreEnabled()) {
            String hint = this.configuration.getEventStore();

            if (StringUtils.isNotEmpty(hint)) {
                try {
                    this.store =
                        this.componentManager.getInstance(EventStore.class, this.configuration.getEventStore());
                } catch (ComponentLookupException e) {
                    throw new InitializationException("Failed to get the configured store", e);
                }
            }
        }

        // retro compatibility: make sure to synchronize the old storage until the new store covers everything we
        // want it to cover
        String legacyHint = this.store != null ? "legacy" : "legacy/verbose";
        if (this.componentManager.hasComponent(EventStore.class, legacyHint)) {
            try {
                this.legacyStore = this.componentManager.getInstance(EventStore.class, legacyHint);
            } catch (ComponentLookupException e) {
                throw new InitializationException("Failed to get the legacy event stream", e);
            }
        }
    }

    @Override
    public CompletableFuture<Event> saveEvent(Event event)
    {
        CompletableFuture<Event> future = null;

        if (this.legacyStore != null) {
            future = this.legacyStore.saveEvent(event);
        }

        if (this.store != null) {
            // Forget about legacy store result if new store is enabled
            future = this.store.saveEvent(event);
        }

        if (future == null) {
            future = new CompletableFuture<>();
            future.completeExceptionally(new EventStreamException(NO_STORE));
        }

        return future;
    }

    @Override
    public CompletableFuture<Optional<Event>> deleteEvent(String eventId)
    {
        CompletableFuture<Optional<Event>> future = null;

        if (this.legacyStore != null) {
            future = this.legacyStore.deleteEvent(eventId);
        }

        if (this.store != null) {
            // Forget about legacy store result if new store is enabled
            future = this.store.deleteEvent(eventId);
        }

        if (future == null) {
            future = new CompletableFuture<>();
            future.completeExceptionally(new EventStreamException(NO_STORE));
        }

        return future;
    }

    @Override
    public CompletableFuture<Optional<Event>> deleteEvent(Event event)
    {
        CompletableFuture<Optional<Event>> future = null;

        if (this.legacyStore != null) {
            future = this.legacyStore.deleteEvent(event);
        }

        if (this.store != null) {
            // Forget about legacy store result if new store is enabled
            future = this.store.deleteEvent(event);
        }

        if (future == null) {
            future = new CompletableFuture<>();
            future.completeExceptionally(new EventStreamException(NO_STORE));
        }

        return future;
    }

    @Override
    public CompletableFuture<EventStatus> saveEventStatus(EventStatus status)
    {
        CompletableFuture<EventStatus> future = null;

        if (this.legacyStore != null) {
            future = this.legacyStore.saveEventStatus(status);
        }

        if (this.store != null) {
            // Forget about legacy store result if new store is enabled
            future = this.store.saveEventStatus(status);
        }

        if (future == null) {
            future = new CompletableFuture<>();
            future.completeExceptionally(new EventStreamException(NO_STORE));
        }

        return future;
    }

    @Override
    public CompletableFuture<Optional<EventStatus>> deleteEventStatus(EventStatus status)
    {
        CompletableFuture<Optional<EventStatus>> future = null;

        if (this.legacyStore != null) {
            future = this.legacyStore.deleteEventStatus(status);
        }

        if (this.store != null) {
            // Forget about legacy store result if new store is enabled
            future = this.store.deleteEventStatus(status);
        }

        if (future == null) {
            future = new CompletableFuture<>();
            future.completeExceptionally(new EventStreamException(NO_STORE));
        }

        return future;
    }

    @Override
    public Optional<Event> getEvent(String eventId) throws EventStreamException
    {
        Optional<Event> event = Optional.empty();

        // Try the new store
        if (this.store != null) {
            event = this.store.getEvent(eventId);
        } else if (this.legacyStore != null) {
            event = this.legacyStore.getEvent(eventId);
        }

        return event;
    }

    @Override
    public EventSearchResult search(EventQuery query) throws EventStreamException
    {
        if (this.store != null) {
            return this.store.search(query);
        }

        if (this.legacyStore != null) {
            return this.legacyStore.search(query);
        }

        return EventSearchResult.EMPTY;
    }
}
