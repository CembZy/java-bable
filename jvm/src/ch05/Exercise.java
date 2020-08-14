package ch05;


public class Exercise {


    public static void main(String[] args) throws InterruptedException {
        final Exercise exercise=new Exercise();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    exercise.test2();
                    exercise.test3();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        exercise.test1();

    }


    public synchronized void test1() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("test1 run....");
    }

    public synchronized void test2() throws InterruptedException {
        System.out.println("test2 run....");
    }

    public synchronized void test3() throws InterruptedException {
        System.out.println("test3 run....");
    }

}
