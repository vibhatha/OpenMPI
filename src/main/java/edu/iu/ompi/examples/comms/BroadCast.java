package edu.iu.ompi.examples.comms;

import edu.iu.ompi.constants.Constants;
import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public class BroadCast {

    static{
        System.setProperty(Constants.LOG_TYPE, Constants.LOG_FORMAT);
    }

    public final static Logger LOG = Logger.getLogger(BroadCast.class.getName());

    public static void main(String[] args) throws MPIException {

        MPI.Init(args);
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize() ;
        int message[] = new int [1];

        if(world_size < 2){
            LOG.info("World Size must be greater than 1");
        }

        int noOfElements = 10;

        int sendData [] = genRandomInt(10);
        MPI.COMM_WORLD.barrier();
        double bcastTime = 0;
        bcastTime -= MPI.wtime();

        MPI.COMM_WORLD.bcast(sendData, noOfElements, MPI.INT, 0);

        MPI.COMM_WORLD.barrier();
        bcastTime += MPI.wtime();

        if (world_rank != 0) {
            System.out.println("Rank : " + world_rank + ", BroadCasted Values : " + Arrays.toString(sendData));
        }

        if (world_rank == 0) {
            System.out.println(" Time Ta,lken : " + bcastTime);
        }

        MPI.Finalize();

    }

    public static int [] genRandomInt(int noOfElements) {
        int [] rands = new int [noOfElements];
        Random random = new Random();
        for (int i = 0; i < noOfElements; i++) {
            rands[i] = random.nextInt();
        }

        return rands;
    }

}
