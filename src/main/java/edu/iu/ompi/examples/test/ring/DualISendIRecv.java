package edu.iu.ompi.examples.test.ring;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import static mpi.MPI.slice;

public class DualISendIRecv {
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    public final static Logger LOG = Logger.getLogger(DualISendIRecv.class.getName());

    private static int rank, tasks, bytes, next, prev;
    private static IntBuffer indata = MPI.newIntBuffer(1000);
    private static IntBuffer outdata = MPI.newIntBuffer(1000);
    private static Request req[];
    private static Status status[];
    private static int i = 0;


    public static void main(String args[]) throws MPIException {
        int tasks, bytes;
        IntBuffer me = MPI.newIntBuffer(1),
                data = MPI.newIntBuffer(1000);
        Request req[];

        MPI.Init(args);
        me.put(0, MPI.COMM_WORLD.getRank());
        tasks = MPI.COMM_WORLD.getSize();
        req = new Request[1000];

        for (int i = 0; i < tasks; i++) {
            req[2 * i] = MPI.COMM_WORLD.iSend(me, 1, MPI.INT, i, 1);
            req[2 * i + 1] = MPI.COMM_WORLD.iRecv(slice(data, i), 1, MPI.INT, i, 1);
        }

        Request.waitAll(Arrays.copyOf(req, 2 * tasks));

        for (int i = 0; i < tasks; i++) {
            req[2 * i] = MPI.COMM_WORLD.iSend(me, 1, MPI.INT, i, 1);
            req[2 * i + 1] = MPI.COMM_WORLD.iRecv(slice(data, i), 1, MPI.INT, i, 1);
        }
        Request.waitAll(Arrays.copyOf(req, 2 * tasks));

        /* Also try giving a 0 count and ensure everything is ok */
        Request.waitAll(new Request[0]);

        MPI.COMM_WORLD.barrier();
        MPI.Finalize();
    }


}
