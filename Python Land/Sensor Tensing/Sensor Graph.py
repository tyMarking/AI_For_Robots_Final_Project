import matplotlib.pyplot as plt
import threading
import time
from HamsterAPI.comm_ble import *
import sys
import Tkinter as tk


def graphit(robotList):
    print("Started")
    proxL = []
    proxR = []
    count = 0
    superCount = 0
    while True:
        for robot in robotList:
            if robot:
                count += 1

                prox = (robot.get_proximity(0),robot.get_proximity(1))
                print("Prox L: " + str(prox[0]) + "\tProx R: " + str(prox[1]))
                time.sleep(0.1)
                proxL.append(prox[0])
                proxR.append(prox[1])

                if count %200 == 0:
                    count = 0
                    superCount += 1
                    fig = plt.figure()
                    plt.title("Sensor Data")
                    plt.ylabel("Sensor Reading")
                    plt.plot(proxL, label='Left Sensor')
                    plt.plot(proxR, label='Right Sensor')
                    plt.legend()
                    fig.savefig("Fig: " + str(superCount))
                    # plt.show()

def main():
    max_robot_num = 1   # max number of robots to control
    comm = RobotComm(max_robot_num)
    comm.start()
    print 'Bluetooth starts'
    robotList = comm.robotList
    plt.show(block=True)
    print("Creating Thread")
    thread = threading.Thread(name="Graph Thread", target=graphit, args=[robotList])
    thread.daemon = True
    thread.start()
  
    root = tk.Tk()
    root.mainloop()


    comm.stop()
    comm.join()

if __name__== "__main__":
  sys.exit(main())

def __main__():
    main()