package edu.iu.ompi.examples.test.average;

import edu.iu.ompi.examples.comms.Reduce;
import edu.iu.ompi.constants.Constants;
import mpi.MPI;
import mpi.MPIException;

import java.util.Random;
import java.util.logging.Logger;

public class ReduceAdvanced {

    static{
        System.setProperty(Constants.LOG_TYPE, Constants.LOG_FORMAT);
    }

    public final static Logger LOG = Logger.getLogger(Reduce.class.getName());

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        if(args.length==0){
            LOG.info("Usage : Read Makefile");
            MPI.Finalize();
        }

        int noOfElementsPerProcs = Integer.parseInt(args[0]);

        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize() ;

        double [] randomNumbers = createRandomNums(noOfElementsPerProcs);

        double localSum [] = new double[2] ;
        for (int i = 0; i < noOfElementsPerProcs; i++) {
            for (int j = 0; j < localSum.length; j++) {
                localSum[j] += randomNumbers[i] + j;
            }


        }

        System.out.printf("\n Local Sum For Process %d - %f, avg = %f \n ", world_rank, localSum[0], (double)localSum[0]/noOfElementsPerProcs);

        double globalSum [] = new double [2];
        MPI.COMM_WORLD.reduce(localSum, globalSum, 2, MPI.DOUBLE, MPI.SUM, 0);

        if(world_rank == 0) {
            for (int i = 0; i < globalSum.length; i++) {
                System.out.printf("\n Total Sum : %f, avg = %f \n", globalSum[i], (float)globalSum[i] /(float)(world_size * noOfElementsPerProcs));
            }
        }
        MPI.COMM_WORLD.barrier();
        MPI.Finalize();
    }

    public static double [] createRandomNums(int numElements){
        double randomNumbers [] = new double[numElements];

        for (int j = 0; j < numElements; j++) {
            randomNumbers[j] = new Random().nextDouble();
        }
        return randomNumbers;
    }
}
