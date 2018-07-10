import Tkinter as tk
import random
import time
import starter_bfs as Bfs
def main():
	#Num to generate
	size = 4
	graph = {}
	for i in range(size**2):
		graph[i] = []
	for node in range(size**2):
		if node % size != size-1 and random.random() < 0.9:
			graph[node].append(node+1)
			graph[node+1].append(node)
		if node < size**2-size and random.random() < 0.9:
			graph[node].append(node+size)
			graph[node+size].append(node)

	print graph
	
	bfs = Bfs.BFS(graph)
	numPaths = len(bfs.bfs_paths(0,size**2-1))
	print(numPaths)

	# while(numPaths > 3):
	# 	key = random.choice(graph.keys())
	# 	if len(graph[key]) > 0:
	# 		graph[key].remove(random.choice(graph[key]))
	# 		print("Removed a connection")
	# 	numPaths = len(bfs.bfs_paths(0,size**2-1))
	while numPaths < 1:
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
			options.remove(key-size)
		if key > size**2-1-size:
			options.remove(key+size)
		if key%size == 0:
			options.remove(key-1)
		if key%size == size-1:
			options.remove(key+1)

		if len(options) == 0:
			continue
		choice = random.choice(options)
		print("Choice loop start")
		counter = 0
		while choice in graph[key] and counter < 100:
			counter += 1
			choice = random.choice(options)
		if counter > 100:
			print("COUNTER TERMINATED LOOP")
		print("choice loop end")
		print("Node: " + str(key)+"\tChoice: %s" % choice)
		print("Added a connection")
		graph[key].append(choice)
		graph[choice].append(key)
		numPaths = len(bfs.bfs_paths(0,size**2-1))
		print(numPaths)
		display(graph,size)
	print("###################\nFINAL LOOP EXITED\n#################")
	display(graph,size)



def display(graph, size):
	root = tk.Tk()
	root.geometry="400x400"
	canvas = tk.Canvas(root, bg="white", width=400, height=400)
	canvas.pack()

	pointXs = []
	pointYs = []
	dx = 400/(size+1)
	dy = 400/(size+1)
	for i in range(size):
		pointXs.append((i+1)*dx)
		pointYs.append((i+1)*dx)

	for node in graph.keys():
		x = pointXs[node%size]
		y = pointYs[int(node/size)]
		if node + 1 not in graph[node]:
			#draw line to right
			p1 = [x+dx,y-dy]
			p2 = [x+dx,y+dy]
			canvas.create_line(p1,p2,width=2)
		if node + size in graph[node]:
			print("HORIZONTAL LINE")
			#draw line below
			p1 = [x-dx,y+dy]
			p2 = [x+dx,y+dy]
			canvas.create_line(p1,p2,width=2)

	# time.sleep(2)
	root.mainloop()


	# time.sleep(10)


if __name__ == "__main__":
	main()