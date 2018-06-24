package edu.iu.ompi.examples.comms;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;

public class Scatter {

    public static void main(String[] args) throws MPIException {

        MPI.Init(args);

        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();

        int noPerProcs = 4;


        int [] scatterData = new int[noPerProcs * world_size];

        int [] recvScatterData = new int[noPerProcs];

        scatterData = genRandomInt(noPerProcs * world_size);

        MPI.COMM_WORLD.scatter(scatterData, noPerProcs, MPI.INT, recvScatterData, noPerProcs, MPI.INT, 0);


        System.out.println("Rank : " + world_rank + ", Scatter Data : " + Arrays.toString(recvScatterData) + "\n");

        if(world_rank == 0) {
            System.out.println("All Data : " + Arrays.toString(scatterData));
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
