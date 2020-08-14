package cap8;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Thread test = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "     test1 run......");
            }
        });

        Thread test2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "     test2 run......");
            }
        });

        System.out.println(Thread.currentThread().getName() + "     main run.....");
        test.start();
        test.join();
        test2.start();
        test2.join();

        System.out.println(Thread.currentThread().getName() + "     main end.....");
    }

}
