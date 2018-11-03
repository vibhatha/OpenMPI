package edu.iu.ompi.examples.test.ring;

import edu.iu.ompi.examples.comms.SendRecv;
import mpi.*;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class DualSendRecv {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    public final static Logger LOG = Logger.getLogger(DualSendRecv.class.getName());

    public static void main(String[] args) throws MPIException, InterruptedException {
        MPI.Init(args);
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
        int message[] = new int[1];

        if (world_size < 2) {
            LOG.info("World Size must be greater than 1");
        }

        int number = 0;

        if (world_rank == 0) {
            LOG.info("World Size : " + world_size);
        }

        SendWorker sendWorker = new SendWorker(world_rank, message);
        RecvWorker recvWorker = new RecvWorker(world_rank, message);


        Thread t1 = new Thread(sendWorker);


        Thread t2 = new Thread(recvWorker);

        t1.start();
        t2.start();




        /*if (world_rank == 0) {
            message[0] = -1;
            MPI.COMM_WORLD.send(message, 1, MPI.INT, 1, 50 );
            LOG.info("Message Sent : "+ message[0] + " @ Rank : " + world_rank);
        }else if(world_rank == 1) {
            MPI.COMM_WORLD.recv(message, 1, MPI.INT, 0, 50);
            LOG.info("Message Received : "+ message[0] + " @ Rank : " + world_rank);
        }*/

        MPI.Finalize();
    }

    private static class SendWorker implements Runnable {

        private int message[] = new int[1];
        private int world_rank = 0;


        SendWorker(int world_rank, int[] message) {
            this.world_rank = world_rank;
            this.message = message;
        }

        @Override
        public void run() {
            if (world_rank == 0) {
                message[0] = -1;
                try {
                    MPI.COMM_WORLD.send(message, 1, MPI.INT, 1, 50);
                    LOG.info("Message Sent : " + message[0] + " @ Rank : " + world_rank);
                } catch (MPIException e) {
                    LOG.info("Exception : " + e.getMessage());
                }

            }
        }
    }

    private static class RecvWorker implements Runnable {

        private int message[] = new int[1];
        private int world_rank = 0;

        RecvWorker(int world_rank, int[] message) {
            this.world_rank = world_rank;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                if (world_rank == 1) {
                    MPI.COMM_WORLD.recv(message, 1, MPI.INT, 0, 50);
                    LOG.info("Message Received : "+ message[0] + " @ Rank : " + world_rank);
                }

            } catch (MPIException e) {
                LOG.info("Exception : " + e.getMessage());
            }

        }
    }

}
