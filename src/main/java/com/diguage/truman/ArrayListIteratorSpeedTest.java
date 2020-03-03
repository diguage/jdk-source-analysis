package com.diguage.truman;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-03-03 13:09
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@State(Scope.Benchmark)
@Threads(8)
public class ArrayListIteratorSpeedTest {

    private ArrayList<Integer> arrayList = null;

    @Setup(Level.Iteration)
    public void setup() {
        int capacity = 1_000_000;
        arrayList = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            arrayList.add(i);
        }
    }

    @Benchmark
    public void testIterator() {
        Integer iteratorValue = null;
        Iterator<Integer> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            iteratorValue = iterator.next();
        }
    }

    @Benchmark
    public void testRandomAccess() {
        Integer randomAccessValue = null;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            randomAccessValue = arrayList.get(i);
        }
    }

    @Benchmark
    public void testForEachLambda() {
        arrayList.forEach(this::devnull);
    }

    public void devnull(Integer value) {
        Integer forEachLambdaValue = value;
    }

    @Benchmark
    public void testForEach() {
        Integer forEachValue = null;
        for (Integer integer : arrayList) {
            forEachValue = integer;
        }
    }
}
