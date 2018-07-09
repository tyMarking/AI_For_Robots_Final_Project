import json

def getNetworkFromFile(file):
	"""
	file = open(file,"r")
	netJson = file.read()
	netList = json.loads(netJson)
	"""
	#netList in form [fromNode, toNode, wieght]
	netList = [[0,1,0.345],[0,3,0.234],[1,2,-234]]
	weightDict = {}
	nodes = set()
	for netItem in netList:
		nodes.add(netItem[0])
		nodes.add(netItem[1])
		if netItem[0] not in weightDict.keys():
			weightDict[netItem[0]] = {netItem[1]:netItem[2]}
		else:
			weightDict[netItem[0]][netItem[1]] = netItem[2]
	print weightDict
	print(nodes)

	activations = {}
	for i in nodes:
		print(i)

if __name__ == "__main__":
	getNetworkFromFile("Test.json")
