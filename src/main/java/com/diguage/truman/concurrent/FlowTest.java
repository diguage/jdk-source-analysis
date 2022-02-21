package com.diguage.truman.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Flow.Subscriber;
import static java.util.concurrent.Flow.Subscription;

public class FlowTest {

  @Test
  public void test() throws InterruptedException {
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
    publisher.subscribe(new PrintSubscriber());

    System.out.println("Submitting items...");
    for (int i = 0; i < 10; i++) {
      publisher.submit(i);
    }

    TimeUnit.SECONDS.sleep(1);
    publisher.close();
  }

  public static class PrintSubscriber implements Subscriber<Integer> {
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
      this.subscription = subscription;
      subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
      System.out.println("Received item: " + item);
      subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
      System.out.println("Error occurred: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
      System.out.println("PrintSubscriber is complete");
    }
  }
}
