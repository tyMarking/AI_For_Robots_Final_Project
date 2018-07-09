import matplotlib as plt
import threading
import time
from HamsterAPI.comm_ble import *


def graphit(robot):
    while True:
        prox = (robot.get_proximity(0),robot.get_proximity(1))
        print("Prox L: " + str(prox[0]) + "\tProx R: " + str(prox[1]))
        time.sleep(1)



def main():
    max_robot_num = 1   # max number of robots to control
    comm = RobotComm(max_robot_num)
    comm.start()
    print 'Bluetooth starts'
    robotList = comm.robotList

    thread = threading.Thread(name="Graph Thread", target=graphit(robotList[0]))
    thread.daemon = True
    threat.start()
  
    root.mainloop()

    comm.stop()
    comm.join()

if __name__== "__main__":
  sys.exit(main())

def __main__():
    main()