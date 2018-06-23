package edu.iu.ompi.comms;

import edu.iu.ompi.constants.Constants;
import mpi.MPI;
import mpi.MPIException;
import java.util.logging.Logger;

public class Reduce {

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

        double localSum [] = new double[1] ;
        for (int i = 0; i < noOfElementsPerProcs; i++) {
            localSum[0] += randomNumbers[i];
        }

        System.out.printf("\nLocal Sum For Process %d - %f, avg = %f\n ", world_rank, localSum[0], (double)localSum[0]/noOfElementsPerProcs);

        double globalSum [] = new double [1];
        MPI.COMM_WORLD.reduce(localSum, globalSum, 1, MPI.DOUBLE, MPI.SUM, 0);

        if(world_rank == 0) {
            System.out.printf("\nTotal Sum : %f, avg = %f \n", globalSum[0], (float)globalSum[0] /(float)(world_size * noOfElementsPerProcs));
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


