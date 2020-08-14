package com.concurrent.ch03.rw;

/**
 * 测试隐士锁和读写锁在读多写少的情况的效率
 */
public class TestApp {

    private static GoodsInfo goodsInfo = new GoodsInfo("苹果", 5, 10);

    //读写的比率
    private static final int READ_WRITE_RATIO = 10;


    private static class Read extends Thread {
        private GoodsService goodsService;

        public Read(GoodsService goodsService) {
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                goodsService.getNum();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("读花费的时间：" + (endTime - startTime));
        }
    }

    private static class Write extends Thread {
        private GoodsService goodsService;

        private int num;

        public Write(int num, GoodsService goodsService) {
            this.num = num;
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                goodsService.setNum(num);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("写花费的时间：" + (endTime - startTime));
        }
    }


    public static void main(String[] args) {


        //测试使用隐士锁
        GoodsService goodsService = new UseSyn(goodsInfo);
        test(goodsService);


        //测试使用读写锁
        GoodsService goodsService2 = new UseReentRW(goodsInfo);
        test(goodsService2);

    }

    private static void test(GoodsService goodsService) {
        for (int i = 0; i < 5; i++) {
            Write write = new Write(i, goodsService);
            for (int j = 0; j < READ_WRITE_RATIO; j++) {
                new Read(goodsService).start();
            }
            write.start();
        }
    }

}
