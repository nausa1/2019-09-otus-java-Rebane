package ru.otus.hw03;

import java.util.ArrayList;

class Benchmark {
    private final int loopCounter;
    public ArrayList<byte[]> obj = new ArrayList<>();

    public Benchmark(int loopCounter) {

        this.loopCounter = loopCounter;

    }

    void run() throws InterruptedException {

        for (int loop = 0; loop < loopCounter; loop++) {

            try {
                obj.add(new byte[1000]);
            }
            catch (OutOfMemoryError e)
            {
                System.out.println("Loop_num: " + loop);
            }

            if(loop % 10 == 0)
            Thread.sleep(2); //Label_1

            //for young gen
            if (loop % 2 == 0)
                obj.remove(obj.size()-1);

            //for old gen
            if(loop % 1000000 == 0) {
                //System.out.println("here");
                for (int i = 0; i < obj.size() / 2; i++)
                    obj.remove(0);
            }

        }



    }
}