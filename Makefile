install:
	mvn clean install
package:
	mvn clean
	mvn package

crun:
	mpirun -np 1 java -classpath "classes" edu.iu.ompi.test.Test

crunSenRecv:
	mpirun -np 2 java -classpath "classes" edu.iu.ompi.comms.SendRecv

runSendRecv:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.SendRecv

runISendIRecv:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.ISendIRecv

runDSendRecv:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.ring.DualSendRecv

runDISendIRecv:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.ring.DualISendIRecv

runBcast:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.BroadCast

runScatter:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.Scatter	

runGather:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.Gather

runAllGather:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.AllGather

runScatterNGather:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.average.ScatterAndGather

runTest:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.Test

runRing:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.Ring

runAllReduce:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.AllReduce 4

runMyBcast:
	mpirun -np 4  java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.MyBroadCast

runBcastComp:
	mpirun -np 4  java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.BroadCastComp 2 10

runReduce:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.comms.Reduce 4

runReduceTest:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.ReduceTest

runReduceAdvanced:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.examples.test.average.ReduceAdvanced 4

runMtSendRecv:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.multithread.MtSendRecv
