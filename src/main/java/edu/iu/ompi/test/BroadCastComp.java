package edu.iu.ompi.test;

import edu.iu.ompi.constants.Constants;
import mpi.MPI;
import mpi.MPIException;

import java.util.logging.Logger;

public class BroadCastComp {

    static{
        System.setProperty(Constants.LOG_TYPE, Constants.LOG_FORMAT);
    }

    public final static Logger LOG = Logger.getLogger(BroadCastComp.class.getName());

    public static void main(String[] args)throws MPIException {

        if(args.length!=2){
            LOG.info("Usage BroadCastComp <num-elements> <num-trials>");
            System.exit(0);
        }

        int num_elements = Integer.parseInt(args[0]);
        int num_trials = Integer.parseInt(args[1]);

        MPI.Init(args);
        int world_rank = MPI.COMM_WORLD.getRank();
        int data [] = new int[num_elements];

        for (int i = 0; i < num_elements; i++) {
            data[i]=i+1;
            LOG.info("Data "+i+ " : " +data[i]);
        }

        double total_my_bcast_time =  0.0;
        double total_mpi_bcast_time =  0.0;
        int i=0;
        for (int j = 0; j < num_trials; j++) {
            MPI.COMM_WORLD.barrier();
            total_my_bcast_time -= MPI.wtime();
            myBcast(data, num_elements, 0);
            MPI.COMM_WORLD.barrier();
            total_my_bcast_time += MPI.wtime();

            MPI.COMM_WORLD.barrier();
            total_mpi_bcast_time -= MPI.wtime();
            MPI.COMM_WORLD.bcast(data, num_elements, MPI.INT,0);
            MPI.COMM_WORLD.barrier();
            total_mpi_bcast_time += System.currentTimeMillis();
        }

        if(world_rank == 0){
            LOG.info("Data Size : " + num_elements + ", Trials : " + num_trials);
            LOG.info("Average My Bcast Time : " + total_my_bcast_time / num_trials);
            LOG.info("Average MPI Bcast Time : " + total_mpi_bcast_time / num_trials);
        }

        MPI.Finalize();

    }

    public static void myBcast(int [] data, int count, int root) throws MPIException{
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
        if (world_rank == root) {
            // If we are the root process, send our data to everyone
            int i;
            for (i = 0; i < world_size; i++) {
                if (i != world_rank) {
                    MPI.COMM_WORLD.send(data, count, MPI.INT, i, 50);
                }
            }
        } else {
            // If we are a receiver process, receive the data from the root
            MPI.COMM_WORLD.recv(data, count, MPI.INT,  root, 50);
            LOG.info("Rank : " + world_rank + " ," + "Received Data : ");
            for (int i = 0; i < count; i++) {
                LOG.info(""+data[i]);
            }
        }
    }

}

