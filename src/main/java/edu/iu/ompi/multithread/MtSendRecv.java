package edu.iu.ompi.multithread;

import edu.iu.ompi.examples.comms.SendRecv;
import mpi.Datatype;
import mpi.MPI;
import mpi.MPIException;
import mpi.Op;

import java.util.Arrays;
import java.util.logging.Logger;

public class MtSendRecv {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    public final static Logger LOG = Logger.getLogger(SendRecv.class.getName());

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
        int message[] = new int[1];
        int chunk_id = 0;

        message[0] = 10;
        SendWorker sendWorker1 = new SendWorker(message, world_rank, world_size, 0);
        Thread t = new Thread(sendWorker1);
        SendWorker sendWorker2 = new SendWorker(message, world_rank, world_size, 1);
        Thread t2 = new Thread(sendWorker2);
        SendWorker sendWorker3 = new SendWorker(message, world_rank, world_size, 2);
        Thread t3 = new Thread(sendWorker3);
        SendWorker sendWorker4 = new SendWorker(message, world_rank, world_size, 3);
        Thread t4 = new Thread(sendWorker4);
        t.start();
        t.join();
        t2.start();
        t2.join();
        t3.start();
        t3.join();
        t4.start();
        t4.join();

        int a [] = {sendWorker1.getSum(), sendWorker2.getSum(), sendWorker3.getSum(), sendWorker4.getSum()};

        //MPI.COMM_WORLD.allReduce(a, a.length, MPI.INT, MPI.SUM);
        AllReduceWorker allReduceWorker = new AllReduceWorker(a, MPI.SUM);
        Thread t5 = new Thread(allReduceWorker);
        t5.start();
        t5.join();
        int [] data = allReduceWorker.getArr();
        LOG.info(String.format("World Rank : %d, Sum of T1 : %d, Sum of T2 : %d, Sum of T3 : %d, Sum of T4 : %d, Total Sum : %s ", world_rank, sendWorker1.getSum(), sendWorker2.getSum(), sendWorker3.getSum(), sendWorker4.getSum(), Arrays.toString(data)));

        MPI.Finalize();

    }

    private static class AllReduceWorker implements Runnable {

        private int [] arr;
        private Op op;

        public AllReduceWorker(int[] arr, Op op) {
            this.arr = arr;
            this.op = op;
        }

        @Override
        public void run() {
            try {
                MPI.COMM_WORLD.allReduce(this.arr, this.arr.length, MPI.INT, this.op);
            } catch (MPIException e) {
                e.printStackTrace();
            }
        }

        public int[] getArr() {
            return arr;
        }
    }

    private static class SendWorker implements Runnable {

        private int dest = 0;
        private int world_size = 1;
        private int world_rank = 1;
        private int[] data;
        private String[] args;
        private int chunkId = 0;
        private int sum = 0;

        SendWorker(int[] data, int world_size, int world_rank, int chunkId) {
            this.data = data;
            this.world_size = world_size;
            this.world_rank = world_rank;
            this.chunkId = chunkId;
        }

        @Override
        public void run() {
            //LOG.info(String.format("Data :  %s , World Rank : %d, World Size : %d => Chunk Id : %d ", Arrays.toString(data), world_rank, world_size, chunkId));
            sum = (world_rank + world_size + chunkId);
        }

        public int getSum() {
            return sum;
        }
    }
}
