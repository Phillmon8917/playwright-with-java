package hooks.errors;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestStepFinished;

public class ErrorCapturePlugin implements ConcurrentEventListener {

    /**
     * Sets the event publisher and registers a handler for TestStepFinished
     * events.
     *
     * @param publisher the event publisher to register handlers with
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinished);
    }

    /**
     * Handles test step finished events. If the step failed, stores the error
     * in ErrorStore.
     *
     * @param event the test step finished event containing result details
     */
    private void onStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() == Status.FAILED) {
            Throwable error = event.getResult().getError();
            if (error != null) {
                ErrorStore.set(error);
            }
        }
    }
}
