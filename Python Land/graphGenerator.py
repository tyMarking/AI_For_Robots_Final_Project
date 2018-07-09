import Tkinter as tk
import random
import time
from graph_with_edge_cost as Dijk
def main():
	#Num to generate
	size = 4
	graph = {}
	for i in range(size**2):
		graph[i] = set()
	for node in range(size**2):
		if node % size != size-1 and random.random() < 0.5:
			graph[node].add(node+1)
			graph[node+1].add(node)
		if node < size**2-size and random.random() < 0.5:
			graph[node].add(node+size)
			graph[node+size].add(node)

	print graph
	dijkGraph = Dijk.Graph()
	for node in graph.keys():
		dijkGraph.add_node(node,None)
	for node in graph.keys():
		for toNode in graph[node]:
			dijkGraph.add_edge(node,toNode,1)


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

	root.mainloop()


	# time.sleep(10)


if __name__ == "__main__":
	main()