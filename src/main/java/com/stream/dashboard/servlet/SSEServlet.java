package com.stream.dashboard.servlet;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class SSEServlet extends HttpServlet {
    private static final int pausePollerThreadDelayInMs = Integer.parseInt(System.getProperty("poll.time"));
    private static volatile boolean isDestroyed = false;
    protected final Observable<String> sampleStream;
    private final Object responseWriteLock = new Object();

    public SSEServlet(Observable<String> sampleStream) {
        this.sampleStream = sampleStream;
    }

    public static void shutdown() {
        isDestroyed = true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (isDestroyed) {
            response.sendError(503, "Service has been shut down.");
        } else {
            handleRequest(request, response);
        }
    }

    @Override
    public void init() throws ServletException {
        isDestroyed = false;
    }

    @Override
    public void destroy() {
        isDestroyed = true;
        super.destroy();
    }

    private void handleRequest(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final AtomicBoolean moreDataWillBeSent = new AtomicBoolean(true);
        Disposable sampleSubscription = null;
        response.setHeader("Content-Type", "text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        final PrintWriter writer = response.getWriter();
        Consumer<String> onNextImpl = data -> {
            if (data != null) {
                try {
                    // avoid concurrent writes with ping
                    synchronized (responseWriteLock) {
                        writer.print("data: " + data + "\n\n");
                        // explicitly check for client disconnect - PrintWriter does not throw exceptions
                        if (writer.checkError()) {
                            moreDataWillBeSent.set(false);
                        }
                        writer.flush();
                    }
                } catch (Exception ex) {
                    moreDataWillBeSent.set(false);
                }
            }
        };
        Consumer<Throwable> errorImpl = error -> {
            moreDataWillBeSent.set(false);
        };
        Action onCompleteImpl = () -> {
            moreDataWillBeSent.set(false);
        };

        try {
            sampleSubscription = sampleStream
                    .observeOn(Schedulers.io())
                    .subscribe(onNextImpl, errorImpl, onCompleteImpl);

            while (moreDataWillBeSent.get() && !isDestroyed) {
                try {
                    Thread.sleep(pausePollerThreadDelayInMs);
                    synchronized (responseWriteLock) {
                        writer.print("ping: \n\n");
                        // explicitly check for client disconnect - PrintWriter does not throw exceptions
                        if (writer.checkError()) {
                            moreDataWillBeSent.set(false);
                        }
                        writer.flush();
                    }
                } catch (Exception ex) {
                    moreDataWillBeSent.set(false);
                }
            }
        } finally {
            if (sampleSubscription != null && !sampleSubscription.isDisposed()) {
                System.out.println("Disposing subscription");
                sampleSubscription.dispose();
            }
        }


    }
}
