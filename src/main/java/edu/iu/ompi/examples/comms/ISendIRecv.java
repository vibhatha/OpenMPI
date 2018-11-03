package edu.iu.ompi.examples.comms;

import mpi.MPI;
import mpi.MPIException;
import mpi.Request;
import mpi.Status;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.logging.Logger;
import static mpi.MPI.slice;

public class ISendIRecv {
    static{
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    public final static Logger LOG = Logger.getLogger(ISendIRecv.class.getName());

    private static int me, tasks, bytes;
    private static IntBuffer indata  = MPI.newIntBuffer(1000);
    private static IntBuffer outdata = MPI.newIntBuffer(1000);
    private static Request req[];

    public static void main (String args[]) throws MPIException
    {
        MPI.Init(args);
        me = MPI.COMM_WORLD.getRank();
        tasks = MPI.COMM_WORLD.getSize();
        req = new Request[1000];

        for (int i = 0; i < tasks; i++) {
            outdata.put(i, me*10+1);
            indata.put(i, -1);
        }

        for (int i = 0; i < tasks; i++)  {
            req[2*i] = MPI.COMM_WORLD.iSend(slice(outdata, i), 1, MPI.INT, i, 1);
            req[2*i+1] = MPI.COMM_WORLD.iRecv(slice(indata, i), 1, MPI.INT, i, 1);
        }
        wstart();



        MPI.COMM_WORLD.barrier ();
        MPI.Finalize();
    }


    private static void wstart() throws MPIException
    {
        Status[] stats = Request.waitAllStatus(Arrays.copyOf(req, 2 * tasks));

        for (int i = 0; i < tasks; ++i)
            if (indata.get(i) != (i*10+1)){
                System.out.println("ERROR in Waitall: data is " +
                        indata.get(i) + ", should be " +
                        i +"\n");
            } else{
                System.out.println("Sending data " + indata.get(i));
            }

        /* ONLY THE RECEIVERS HAVE STATUS VALUES ! */
        for (int i = 1; i < 2 * tasks; i += 2) {
            bytes = stats[i].getCount(MPI.BYTE);
            if (bytes != 4){
                System.out.println("ERROR in Waitall/getcount: " +
                        "bytes = " + bytes + ", should " +
                        "be 4\n");
            }else {
                System.out.println("Receiving data " + outdata.get(0));
            }


        }
    }
}
