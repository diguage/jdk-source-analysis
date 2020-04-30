package com.diguage.truman;

import org.junit.Test;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author D瓜哥, https://www.diguage.com/
 * @since 2020-04-22 17:26
 */
public class TreeMapTest {

    // tag::question[]
    @Test
    public void testQuestion() {
        TreeMap<Pair, Pair> data = new TreeMap<>();
        Pair pair = new Pair(1, System.currentTimeMillis());

        data.put(pair, pair);
        Pair value = data.get(new Pair(1));
        // 请问，这里会输出 true ？还是 false ？
        System.out.println(pair.equals(value));
    }

    class Pair implements Comparable<Pair> {
        int key;
        long time;

        public Pair(int key) {
            this.key = key;
        }

        public Pair(int key, long time) {
            this.key = key;
            this.time = time;
        }

        @Override
        public int compareTo(Pair o) {
            return Long.compare(this.time, o.time);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pair pair = (Pair) o;
            return key == pair.key;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
    // end::question[]

    // tag::classPerson[]
    public static class Person {
        long id;
        int sortFactor;

        public Person(long id) {
            this(id, (int) id);
        }

        public Person(long id, int sortFactor) {
            this.id = id;
            this.sortFactor = sortFactor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Person person = (Person) o;
            return id == person.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", age=" + sortFactor +
                    '}';
        }
    }
    // end::classPerson[]

    // tag::resort[]
    @Test
    public void testSort() {
        Comparator<Person> comparator
                = Comparator.comparingInt(a -> a.sortFactor);
        TreeMap<Person, Person> map = new TreeMap<>(comparator);
        for (int i = 0; i < 10; i++) {
            if ((i & 1) == 1) {
                Person person = new Person(i);
                map.put(person, person);
            } else {
                Person param = new Person(i / 2);
                Person person = map.get(param);
                if (Objects.nonNull(person)) {
                    person.sortFactor = new Random().nextInt();
                }
            }
        }
        //
        map.forEach((k, v) -> {
            System.out.println(k);
        });
        System.out.println("-------------");
        for (Person person : map.navigableKeySet()) {
            System.out.println(person);
        }
        System.out.println("-------------");
        for (Person person : map.descendingKeySet()) {
            System.out.println(person);
        }
    }
    // end::resort[]

    // tag::duplicateSortFactor[]
    @Test
    public void testDuplicateSortFactor() {
        Comparator<Person> comparator
                = Comparator.comparingInt(a -> a.sortFactor);
        TreeMap<Person, Person> treeMap = new TreeMap<>(comparator);
        Person p1 = new Person(1, 0);
        Person p2 = new Person(2, 0);
        assert !p1.equals(p2);
        System.out.println(p1.equals(p2));

        for (int i = 0; i < 10; i++) {
            Person person = new Person(i, 0);
            treeMap.put(person, person);
        }

        assert (treeMap.size() == 1);
        treeMap.forEach((k, v) -> {
            System.out.println("-----------------------");
            System.out.printf("kid= %-4d kfactor= %-8d%n", k.id, k.sortFactor);
            System.out.printf("vid= %-4d vfactor= %-8d%n", v.id, v.sortFactor);
        });
    }
    // end::duplicateSortFactor[]

    // tag::commonCase[]
    @Test
    public void testPut() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        for (int i = 0; i < 10; i++) {
            treeMap.put(i, i * 100);
        }
    }
    // end::commonCase[]

}