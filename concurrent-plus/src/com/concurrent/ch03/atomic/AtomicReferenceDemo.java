package com.concurrent.ch03.atomic;


import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference演示
 */
public class AtomicReferenceDemo {

    private static class User {
        private String name;

        private int age;

        public User(String name, int age) {
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
    }


    public static void main(String[] args) {



        AtomicReference<User> atomicReference = new AtomicReference();
        User user = new User("a1", 1);
        atomicReference.set(user);

        System.out.println(atomicReference.get());
        System.out.println(user);
        System.out.println(atomicReference.get().getName());
        System.out.println(atomicReference.get().getAge());

        User user2 = new User("a2", 2);

        System.out.println("----------------");
        System.out.println(atomicReference.compareAndSet(user,user2));
        System.out.println(atomicReference.get().getName());
        System.out.println(atomicReference.get().getAge());
        System.out.println(atomicReference.get());

        System.out.println("----------------");
        System.out.println(user.getName());
        System.out.println(user.getAge());
    }


}
