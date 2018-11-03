package edu.iu.ompi.examples.comms;

import mpi.*;
import java.util.logging.Logger;
//import edu.iu.ise.dsvwn.constants.Constants;

public class SendRecv {

    static{
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    public final static Logger LOG = Logger.getLogger(SendRecv.class.getName());

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize() ;
        int message[] = new int [1];

        if(world_size < 2){
            LOG.info("World Size must be greater than 1");
        }

        int number = 0 ;

        if(world_rank==0){
            LOG.info("World Size : " + world_size);
        }

        if (world_rank == 0) {
            message[0] = -10;
            MPI.COMM_WORLD.send(message, 1, MPI.INT, 1, 50 );
            LOG.info("Message Sent : "+ message[0] + " @ Rank : " + world_rank);
        }else if(world_rank == 1) {
            MPI.COMM_WORLD.recv(message, 1, MPI.INT, 0, 50);
            LOG.info("Message Received : "+ message[0] + " @ Rank : " + world_rank);
        }

        MPI.Finalize();
    }
}

