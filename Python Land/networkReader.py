import json
import Queue as Q
import math

class Network():
	weightDict = None
	nodes = None

	def __init__(self):
		weightDict = {}
		nodes = set()

	def getNetworkFromFile(self, file):
		
		file = open(file,"r")
		netJson = file.read()
		netList = json.loads(netJson)
		
		#netList in form [fromNode, toNode, wieght]
		# netList = [[1,3,1],[2,3,1],[2,4,0.5],[3,3,10],[3,4,1]]
		weightDict = {}
		nodes = set()
		for netItem in netList:
			nodes.add(netItem[0])
			nodes.add(netItem[1])
			if netItem[0] not in weightDict.keys():
				weightDict[netItem[0]] = [(netItem[1],netItem[2])]
			else:
				weightDict[netItem[0]].append((netItem[1],netItem[2]))
		print weightDict
		print(nodes)

		self.weightDict = weightDict
		self.nodes = nodes

	def propagate(self, inputs):
		NUM_INPUTS = 3
		nodeSum = {}
		activations = {}
		nodeDones = {}
		for node in self.nodes:
			nodeDones[node] = False
			nodeSum[node] = 0
		#nodes start at 1, Thanks Brian
		for i in range(len(inputs)):
			nodeSum[i+1] = inputs[i]
			# print("Node " + str(i+1)+" is inputed: "+str(inputs[i]))
		for node in self.nodes:
			nodeDones[node] = True
			a = sig(nodeSum[node])
			nodeSum[node] = 0

			activations[node] = a
			# print("Node: " + str(node)+"\tSum: " +str(nodeSum[node])+"\tA: " + str(activations[node]))
			# nodeSum[node] = 

			if node not in self.weightDict.keys():
				continue
			for edge in self.weightDict[node]:

				nodeSum[edge[0]] += a * edge[1]
				if nodeDones[edge[0]]:
					activations[edge[0]] = sig(nodeSum[edge[0]])


		# print(activations)
		return activations[max(self.nodes)]*2-1


def sig(x):
	# print(x)
	y = (1/(1+math.exp(-x)))
	# print(y)
	# y = x
	return y
	# return x


if __name__ == "__main__":
	net = Network()
	net.getNetworkFromFile("Network.json")
	result = net.propagate([4,4,3])
	print(result)

