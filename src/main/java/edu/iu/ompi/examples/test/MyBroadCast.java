package edu.iu.ompi.examples.test;

import edu.iu.ompi.constants.Constants;
import mpi.MPI;
import mpi.MPIException;

import java.util.logging.Logger;

public class MyBroadCast {
    static{
        System.setProperty(Constants.LOG_TYPE, Constants.LOG_FORMAT);
    }

    public final static Logger LOG = Logger.getLogger(MyBroadCast.class.getName());

    public static void main(String[] args) throws MPIException {

        MPI.Init(args);
        int world_rank = MPI.COMM_WORLD.getRank();
        int data [] = new int[1];
        data[0]=100;
        if(world_rank == 0){
            LOG.info("Rank : " + world_rank + " ," + "Sending Data : "+data[0]);
            myBcast(data,1,0);
        }else{
            myBcast(data,1,0);
            LOG.info("Rank : " + world_rank + " ," + "Sending Data : "+data[0]);
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
        }
    }
}
