package:
	mvn clean
	mvn package

crun:
	mpirun -np 1 java -classpath "classes" edu.iu.ompi.test.Test

crunSenRecv:
	mpirun -np 2 java -classpath "classes" edu.iu.ompi.comms.SendRecv

runSendRecv:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.comms.SendRecv

runTest:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.test.Test

runRing:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.comms.Ring

runAllReduce:
	mpirun -np 2 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.comms.AllReduce

runMyBcast:
	mpirun -np 4  java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.test.MyBroadCast

runBcastComp:
	mpirun -np 4  java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.test.BroadCastComp 2 10

runReduce:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.comms.Reduce 4

runReduceTest:
	mpirun -np 4 java -cp target/OpenMPITutorials-1.0-SNAPSHOT.jar edu.iu.ompi.test.ReduceTest
