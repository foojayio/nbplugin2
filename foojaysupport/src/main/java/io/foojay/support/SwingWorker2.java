package io.foojay.support;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.SwingWorker;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SwingWorker2 {

    static class Builder<T> {

        private final @NonNull
        Supplier<T> task;
        private @MonotonicNonNull
        Consumer<T> consumer;
        private @Nullable
        Consumer<Exception> errors;

        @SuppressWarnings("initialization")
        public Builder(@NonNull Supplier<T> task) {
            this.task = task;
        }

        public Builder<T> then(@NonNull Consumer<T> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder<T> handle(@Nullable Consumer<Exception> errors) {
            this.errors = errors;
            return this;
        }

        @UIEffect
        public void execute() {
            new SwingWorker<T, Void>() {
                @Override
                protected T doInBackground() throws Exception {
                    return task.get();
                }

                @Override
                protected void done() {
                    try {
                        consumer.accept(get());
                    } catch (InterruptedException | ExecutionException ex) {
                        if (errors != null)
                            errors.accept(ex);
                    }
                }
            }.execute();
        }

    }

    public static <T> Builder<T> submit(Supplier<T> bg) {
        return new Builder(bg);
    }

    @UIEffect
    public static <T> void post(Supplier<T> bg, Consumer<T> success, Consumer<Exception> fail) {
        submit(bg).then(success).handle(fail).execute();
    }

}
