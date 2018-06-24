package edu.iu.ompi.examples.comms;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;

public class AllGather {

    public static void main(String[] args) throws MPIException {

        MPI.Init(args);

        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();

        int elementsPerProcs = 4;

        double randomNumbers[] = null;

        if (world_rank == 0) {
            randomNumbers = genRandomDouble(elementsPerProcs * world_size);
            System.out.println("Original Array : " + Arrays.toString(randomNumbers));
        }

        double subRandomNums[] = new double[elementsPerProcs];

        MPI.COMM_WORLD.scatter(randomNumbers, elementsPerProcs, MPI.DOUBLE, subRandomNums,
                elementsPerProcs, MPI.DOUBLE, 0);

        System.out.println("Rank : " + world_rank + ", Scattered Data : " + Arrays.toString(subRandomNums)
                + ", Average : " + Arrays.toString(subRandomNums) + "\n");

        double[] subAverages = new double[elementsPerProcs * world_size];

        MPI.COMM_WORLD.allGather(subRandomNums, elementsPerProcs, MPI.DOUBLE, subAverages, elementsPerProcs , MPI.DOUBLE);

        System.out.println(" All Data From Gather : " + Arrays.toString(subAverages) + "\n");
        System.out.println(" Original Data : " + Arrays.toString(randomNumbers) + "\n");
        if (Arrays.equals(subAverages, randomNumbers)) {
            System.out.println("Original Data = Gathered Data");
        }

        MPI.Finalize();

    }

    public static double[] genRandomDouble ( int noOfElements){
        double[] rands = new double[noOfElements];
        Random random = new Random();
        for (int i = 0; i < noOfElements; i++) {
            rands[i] = random.nextDouble();
        }

        return rands;
    }

}