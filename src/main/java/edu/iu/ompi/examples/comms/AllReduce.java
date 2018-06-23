package edu.iu.ompi.examples.comms;

import edu.iu.ompi.constants.Constants;
import mpi.MPI;
import mpi.MPIException;

import java.util.logging.Logger;

public class AllReduce {

    static{
        System.setProperty(Constants.LOG_TYPE, Constants.LOG_FORMAT);
    }

    public final static Logger LOG = Logger.getLogger(AllReduce.class.getName());

    public static void main(String[] args) throws MPIException {

        if(args.length==0){
            LOG.info("Read Makefile");
            MPI.Finalize();
        }
        MPI.Init(args);

        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();

        int noOfElementsPerProc = Integer.parseInt(args[0]);

        double [] randonNumbers = new double [noOfElementsPerProc];
        randonNumbers = createRandomNums(noOfElementsPerProc);

        double [] localSum = new double [1];
        for (int i = 0; i < noOfElementsPerProc; i++) {
            localSum[0] += randonNumbers[i];
        }

        double [] globalSum = new double[1];
        // reduce local sums to global sums for calculating the mean
        MPI.COMM_WORLD.allReduce(localSum, globalSum, 1, MPI.DOUBLE, MPI.SUM);

        double mean = globalSum[0] / (noOfElementsPerProc * world_size);

        //calculate the square difference of the local sum and the mean
        double [] localSqDiff = new double [1];
        for (int i = 0; i < noOfElementsPerProc; i++) {
            localSqDiff[0] += (randonNumbers[i] - mean) * (randonNumbers[i] - mean);
        }

        // Reduce global sum of the squared differences to the root process

        double [] globalSqDiff = new double[1];

        MPI.COMM_WORLD.reduce(localSqDiff, globalSqDiff, 1, MPI.DOUBLE, MPI.SUM, 0);

        if(world_rank == 0){
            double stdDev = Math.sqrt(globalSqDiff[0]/(noOfElementsPerProc * world_size));
            System.out.printf("Mean - %f , Standard Deviation - %f \n", mean, stdDev);
        }

        MPI.COMM_WORLD.barrier();
        MPI.Finalize();

    }

    public static double [] createRandomNums(int numElements){
        double randomNumbers [] = new double[numElements];

        for (int j = 0; j < numElements; j++) {
            randomNumbers[j] = (double)j+1;
        }
        return randomNumbers;
    }
}
