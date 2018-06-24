package edu.iu.ompi.examples.test.average;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;

public class ScatterAndGather {

    public static void main(String[] args) throws MPIException {

        MPI.Init(args);

        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();

        int elementsPerProcs = 2;

        double randomNumbers [] = new double [elementsPerProcs * world_size];

        if (world_rank == 0) {
            randomNumbers = genRandomDouble(elementsPerProcs * world_size);
            System.out.println("Original Array : " + Arrays.toString(randomNumbers));
        }

        double subAverage [] = new double[elementsPerProcs];

        MPI.COMM_WORLD.scatter(randomNumbers, elementsPerProcs, MPI.DOUBLE, subAverage,
                elementsPerProcs, MPI.DOUBLE, 0);

        double [] subAverages = null;

        if (world_rank == 0) {
            subAverages = new double[world_size];
        }

        MPI.COMM_WORLD.gather(subAverage, 1, MPI.DOUBLE, subAverages, 1, MPI.DOUBLE, 0);

        if (world_rank == 0) {
            double average = computeAverage(subAverages);
            System.out.println(" Average : " + average);
        }

        MPI.Finalize();

    }

    public static double [] genRandomDouble(int noOfElements) {
        double [] rands = new double [noOfElements];
        Random random = new Random();
        for (int i = 0; i < noOfElements; i++) {
            rands[i] = random.nextDouble();
        }

        return rands;
    }

    public static double computeAverage(double [] values) {
        double avg = 0.0;

        for (int i = 0; i < values.length; i++) {
            avg += values[i];
        }

        avg = avg / values.length;


        return avg;
    }
}
