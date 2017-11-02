package com.xiaopo.flying.stickerview.junit;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Administrator on 2017/11/2.
 */

public class AtomicReferenceTest {

    class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "[name: " + this.name + ", age: " + this.age + "]";
        }
    }


    // 普通引用
    private static Person person;
    private static AtomicReference<Person> aRperson;

    @Test
    public void testPublic() throws InterruptedException {
        person = new Person("Tom", 18);
        aRperson = new AtomicReference<Person>();
        aRperson.set(person);
        for (int i = 0; i < 100; i++) {


            System.out.println("\n");

//            System.out.println("Atomic Person is " + aRperson.get().toString());

            Thread t1 = new Thread(new Task1());
            Thread t2 = new Thread(new Task2());

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("Now Atomic Person is " + aRperson.get().toString());

        }


    }

    class Task1 implements Runnable {
        public void run() {
            aRperson.getAndSet(new Person("Tom1", 18 + 1));

            System.out.println("Thread1 Atomic References "
                    + aRperson.get().toString());
        }
    }

    class Task2 implements Runnable {
        public void run() {
            aRperson.getAndSet(new Person("Tom2", 19 + 2));

            System.out.println("Thread2 Atomic References "
                    + aRperson.get().toString());
        }
    }


}
