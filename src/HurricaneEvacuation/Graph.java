package HurricaneEvacuation;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Graph {

    static final String edgeEncoding = "#E";
    static final String vertexEncoding = "#V";
    static final String deadlineEncoding = "#D";
    static final String ignoreNonDigitsRegex = "[^0-9]*";

    private Scanner sc;
    ArrayList<Vertex> vertices = new ArrayList<>();
    int deadline;



    Graph(File file) {

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    void constructGraph(){

        try {
            readNumOfVertices();

            while(sc.hasNextLine()){


                if (sc.hasNext(edgeEncoding)){

                    readEdge();

                }
                else if (sc.hasNext(vertexEncoding)){

                    readVertex();

                }
                else if (sc.hasNext(deadlineEncoding)){
                    readDeadline();
                }
                else{

                    throw new InputMismatchException("Unknown input parameter");

                }
                sc.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        sc.close();

    }

    private void readDeadline() {
        sc.skip(deadlineEncoding);
        deadline = sc.nextInt();
    }

    private void readVertex() {
        sc.skip(vertexEncoding);
        int vertexNum = sc.nextInt();
        if (sc.hasNext("P")){
            sc.skip(ignoreNonDigitsRegex);
            int persons = sc.nextInt();
            vertices.get(vertexNum).setPersons(persons);

        } else if (sc.hasNext("S")){
            vertices.get(vertexNum).setShelter();
        }
    }

    private void readEdge() {
        sc.skip(edgeEncoding);
        int in = sc.nextInt();
        int out = sc.nextInt();
        sc.skip(ignoreNonDigitsRegex);
        int weight = sc.nextInt();

        new Edge(vertices.get(in), vertices.get(out),weight);
    }

    private void readNumOfVertices() throws IOException{

        if (sc.hasNext(vertexEncoding)){

            sc.skip(vertexEncoding);
            int numOfVertices = sc.nextInt();
            vertices.add(null);
            for (int i = 1; i <= numOfVertices; i++) {
                vertices.add(i,new Vertex(i));
            }

            sc.nextLine();

        }
        else{
            throw new InputMismatchException("Missing number of vertices");
        }
    }

    void displayWorldState(){

        for (int i = 1; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);

            System.out.printf("Vertex No. %d: %d people", i, v.getPersons());
            if (v.isShelter()){
                System.out.print(" ; has shelter.");
            }
            System.out.print("\nNeighbours: {");
            ArrayList<Edge> edges = v.getEdges();
            for (int j = 0; j < edges.size(); j++) {
                Edge e = edges.get(j);

                System.out.printf("%d", vertices.indexOf(e.getNeighbour(v)));
                if (e.isBlocked()) {
                    System.out.print(" - blocked");
                }
                if (j < edges.size() - 1)
                    System.out.print(", ");
            }
            System.out.println("}\n");

        }
    }

    int getNumberOfVertices(){

        return vertices.size() - 1;

    }

    Vertex getVertex(int index){
        return vertices.get(index);
    }
}
