import Tkinter as tk
import random
import time
import starter_bfs as Bfs
import Queue as Q
import json
def main():
	#Num to generate
	size = 6
	for j in range(1000):
		graph = {}
		conenctProb = random.gauss(0.35,0.1)
		if conenctProb < 0.1:
			conenctProb = 0.1
		if conenctProb > 0.8:
			conenctProb = 0.8

		# print("CONNECT PROB: " + str(conenctProb))
		for i in range(size**2):
			graph[i] = set()
		for node in range(size**2):
			if node % size != size-1 and random.random() < conenctProb:
				graph[node].add(node+1)
				graph[node+1].add(node)
			if node < size**2-size and random.random() < conenctProb:
				graph[node].add(node+size)
				graph[node+size].add(node)
		graph[size**2-1].add(size**2-2)
		graph[size**2-1].add(size**2-1-size)
		graph[size**2-2].add(size**2-1)
		graph[size**2-1-size].add(size**2-1)
		graph[0].add(1)
		graph[0].add(size)
		graph[1].add(0)
		graph[size].add(0)
		# print graph
		
		bfs = Bfs.BFS(graph)
		numPaths = len(bfs.bfs_maze(0,size**2-1))
		# print(numPaths)

		# while(numPaths > 3):
		# 	key = random.choice(graph.keys())
		# 	if len(graph[key]) > 0:
		# 		graph[key].remove(random.choice(graph[key]))
		# 		print("Removed a connection")
		# 	numPaths = len(bfs.bfs_paths(0,size**2-1))
		# print("STARTING FULL GRAPH STUFF")
		fullGraph = {}
		for i in range(size**2):
			fullGraph[i] = set()
		for node in range(size**2):
			if node % size != size-1:
				fullGraph[node].add(node+1)
				fullGraph[node+1].add(node)
			if node < size**2-size:
				fullGraph[node].add(node+size)
				fullGraph[node+size].add(node)
		# print("FULL graph")
		# print(fullGraph)
		fullBFS = Bfs.BFS(fullGraph)
		allPaths = fullBFS.bfs_maze(0,size**2-1)
		# print("ALL PATHS")
		# print(allPaths)
		if len(allPaths) != 0:
			forcedPath = random.choice(allPaths)
			# print("FORCED PATH: " + str(forcedPath))
			for i in range(len(forcedPath)-1):
				if forcedPath[i+1] not in graph[forcedPath[i]]:
					graph[forcedPath[i]].add(forcedPath[i+1])
				if forcedPath[i] not in graph[forcedPath[i+1]]:
					graph[forcedPath[i+1]].add(forcedPath[i])
		else:
			print "FORCED PATH WAS NONE???"

		bfs.changeGraph(graph)
		numPaths = len(bfs.bfs_maze(0,size**2-1))
		# print("AFTER FORCED PATH #PATHS: " + str(numPaths))
		"""
		while numPaths < 1:

			print(graph)
			print(numPaths)
			key = random.choice(graph.keys())
			print("key loop start")
			while len(graph[key]) >= 4:
				key = random.choice(graph.keys())
			print("key loop end")

			options = [key+1,key-1,key+size,key-size]
			# if key == size**2-1:
			# 	options.remove(key+1)
			if key < size:
				print("REMOVED option key-size")
				options.remove(key-size)
			if key > size**2-1-size:
				print("REMOVED option key+size")
				options.remove(key+size)
			if key%size == 0:
				print("REMOVED option key-size")
				options.remove(key-1)
			if key%size == size-1:
				print("REMOVED option key+size")
				options.remove(key+1)

			if len(options) == 0:
				continue
			choice = random.choice(options)
			print("Choice loop start")
			counter = 0
			while choice in graph[key] and counter < 100:
				# print("CHOICE in graph[key]")
				counter += 1
				choice = random.choice(options)
			if counter > 100:
				print("COUNTER TERMINATED LOOP")
			print("choice loop end")
			print("Node: " + str(key)+"\tChoice: %s" % choice)
			print("Added a connection")
			# display(graph,size)
			graph[key].add(choice)
			graph[choice].add(key)
			bfs.changeGraph(graph)
			numPaths = len(bfs.bfs_maze(0,size**2-1))
			print(numPaths)
		"""
			
		# print("##################\nFINAL LOOP EXITED\n##################")
		values = bushfire(graph, size**2-1)
		export(graph,values, "../Maps/" + str(size)+"x"+str(size)+"/" + str(size)+"x"+str(size)+"_#"+str(j)+ ".json")
		print("FINISHED Graph " + str(j))
	# display(graph,size,values)
	


def bushfire(graph, goal):
	values = {}
	values[goal] = 0
	q = Q.Queue()
	q.put(goal)
	done = set()
	done.add(goal)
	while not q.empty():
		node = q.get()
		val = values[node] + 1
		for cNode in graph[node]:
			if cNode not in values.keys() or val < values[cNode]:
				values[cNode] = val
			if cNode not in done:
				done.add(cNode)
				q.put(cNode)

	return values

def export(graph, values, file):
	newGraph = {}
	for key in graph:
		newGraph[key] = list(graph[key])

	file = open(file, "w")
	file.truncate(0)
	data = (newGraph,values)
	string = json.dumps(data)
	file.write(string)

def readFile(file):
	size = int(file[8])
	file = open(file, "r")
	graphJson = file.read()
	newGraph, values = json.loads(graphJson)
	graph = {}
	for key in newGraph:
		graph[int(key)] = set()
		for x in newGraph[key]:
			graph[int(key)].add(int(x))
	newValues = {}
	for key in values:
		newValues[int(key)] = int(values[key])
	
	display(graph, size, newValues)

def display(graph, size, values):
	root = tk.Tk()
	geoSize = 700
	root.geometry=str(geoSize)+"x"+str(geoSize)
	canvas = tk.Canvas(root, bg="white", width=geoSize, height=geoSize)
	canvas.pack()

	pointXs = []
	pointYs = []
	dx = geoSize/(size+1)
	dy = geoSize/(size+1)
	for i in range(size):
		pointXs.append((i+1)*dx)
		pointYs.append((i+1)*dy)
	tempCount = -1
	canvas.create_line(dx/2,dy/2,dx/2,geoSize-dy/2,width=2)
	canvas.create_line(dx/2,dy/2,geoSize-dx/2,dy/2,width=2)
	for y in pointYs:
		for x in pointXs:
			tempCount += 1
			if tempCount in values:
				posStr = str(tempCount)+": " + str(values[tempCount])
			else:
				posStr = str(tempCount)+": " + "NONE"
			canvas.create_text(x,y,text=posStr)
	for node in graph.keys():
		print("MY KINGDOM FOR A HORSE")
		print(type(node))
		node = int(node)
		print(size)
		print(pointXs)
		x = pointXs[node%size]
		y = pointYs[int(node/size)]
		if node + 1 not in graph[node]:
			#draw line to right
			p1 = [x+dx/2,y-dy/2]
			p2 = [x+dx/2,y+dy/2]
			canvas.create_line(p1,p2,width=2)
		if node + size not in graph[node]:
			# print("HORIZONTAL LINE")
			#draw line below
			p1 = [x-dx/2,y+dy/2]
			p2 = [x+dx/2,y+dy/2]
			canvas.create_line(p1,p2,width=2)

	# time.sleep(2)
	root.mainloop()


	# time.sleep(10)


if __name__ == "__main__":
	# main()
	readFile("../Maps/5x5/5x5_#356.json")
