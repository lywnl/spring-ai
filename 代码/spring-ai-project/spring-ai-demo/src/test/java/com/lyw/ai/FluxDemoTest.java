package com.lyw.ai;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxDemoTest {
    public static void main(String[] args) throws InterruptedException {
        Flux<String> stringFlux = Flux.just("apple", "banana", "orange", "pear", "grape").delayElements(Duration.ofSeconds(1));

        stringFlux.map(s -> s.toUpperCase()).map(s -> s + "111").subscribe(s -> System.out.println(s));

        Thread.sleep(7000);
    }
}
