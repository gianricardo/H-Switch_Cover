package SwitchCover.main;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import SwitchCover.chinesePostmanProblem.ChinesePostmanProblem;
import SwitchCover.conversion.Reader;
import SwitchCover.graph.Graph;
import SwitchCover.graph.State;
import SwitchCover.graph.Transition;
import SwitchCover.method.Balancing;
import SwitchCover.method.FirstSearch;
import SwitchCover.method.GenerateTestCase;
import SwitchCover.method.GeraCasosTeste;
import SwitchCover.method.NodeConverter;

public class Main {
	
	private Graph graph = new Graph();
	private Graph dualGraphConverted = new Graph();
	//private Graph dualGraphBalanced = new Graph();
	
	private NodeConverter node = new NodeConverter();
	private FirstSearch search = new FirstSearch();
	private Balancing balancing = new Balancing();

	public Main() {

	}
	
	public List<String> newTratamentFile(List<List<String>> listSequence, String typeFile, String name){
		List<String> sequenceTest = new LinkedList<String>();
		
		for(int seq = 0;  seq < listSequence.size(); seq++) {
			String input = "";
			
			for(int inp = 0; inp < listSequence.get(seq).size(); inp++) {
				input = input + listSequence.get(seq).get(inp);
			}
		}
		
		
		return sequenceTest;
	}
	
	public List<String> tratamentFile(List<List<String>> listSequence, String typeFile, String name){
		List<String> sequenceTest = new LinkedList<String>();
		
		//add sequence in a unique string to after check if this sequence is prefix
		for(int i = 0; i < listSequence.size(); i++){
			String test = "";
			for(int in = 0; in < listSequence.get(i).size(); in++) {
				test = test + listSequence.get(i).get(in);
			}
			/*if(typeFile.equals("xml")){
				for(int in = 0; in < listSequence.get(i).size(); in++) test = test + listSequence.get(i).get(in);
				//if(listSequence.get(i).get(in).length() <= 1) test = test + listSequence.get(i).get(in);
				//else test = test + listSequence.get(i).get(in).charAt(0);
			}
			else {
				if(name.equals("Alltrans")) {
					for(int in = 0; in < listSequence.get(i).size(); in++) {
						test = test + (listSequence.get(i).get(in));
						//if(listSequence.get(i).get(in).length() <= 1) test = test + (listSequence.get(i).get(in));
						//else test = test + (listSequence.get(i).get(in).charAt(0));
					}
				}
				else {
					for(int in = 0; in < listSequence.get(i).size(); in++) {
						String input = listSequence.get(i).get(in).substring(listSequence.get(i).get(in).indexOf(">")+1, listSequence.get(i).get(in).indexOf("/"));
						test = test + (input);
						//if(input.length() <= 1) test = test + (input);
						//else test = test + (input.charAt(0));
						//test = test + (listSequence.get(i).get(in).substring(listSequence.get(i).get(in).lastIndexOf('>')+1, listSequence.get(i).get(in).lastIndexOf('/'))+",");
					}
				}
			}*/
			sequenceTest.add(test);
		}
		
		//Check if sequence is prefix; If is, remove this sequence
		for(int i = 0; i < sequenceTest.size(); i++){
			for(int in = 0; in < sequenceTest.size(); in++){
				if(i != in) {
					if (i < sequenceTest.size() && sequenceTest.get(in).startsWith(sequenceTest.get(i))) {
						if(sequenceTest.get(in).length() > sequenceTest.get(i).length()) {
							sequenceTest.remove(i);
							listSequence.remove(i);
						}
						else {
							sequenceTest.remove(in);
							listSequence.remove(in);
						}
						in=in-1;
					}
				}
			}
		}
		
		//Generate the real sequence tests with no prefix sequences
		List<String> testSuite = new LinkedList<String>();
		for(int seq = 0; seq < listSequence.size(); seq++) {
			String sequence = "";
			for(int inp = 0; inp < listSequence.get(seq).size(); inp++) {
				if(listSequence.get(seq).get(inp).length() <= 1) sequence = sequence + listSequence.get(seq).get(inp);
				else{
					if(name.equals("Alltranspair")) {
						String input = listSequence.get(seq).get(inp).substring(listSequence.get(seq).get(inp).indexOf(">")+1, listSequence.get(seq).get(inp).indexOf("/"));
						sequence = sequence + input.charAt(0);
					}
					else sequence = sequence + listSequence.get(seq).get(inp).charAt(0);
				}
			}
			testSuite.add(sequence);
		}
		
		
		return testSuite;
	}
	
	public void firstSearchTestCase(String path, String typeFile, Reader reader, int i, String criterion, Graph g){
		Iterator<State> firstSearch = g.getIteratorStateValue();
		List<List<String>> firstSearchListSequence = new LinkedList<List<String>>();
		
		//System.out.println(g.showResult());
		
		while(firstSearch.hasNext()){
			State state = firstSearch.next();
			//System.out.println(state.getName());
			if(state.getTypeState().equals("inicial")){
				g.inicialState();
				
				if(i == 1 || i == 5) {
					if(criterion.equals("Alltrans")) {
						List<List<Transition>> ts = search.bfs(state, g);
						firstSearchListSequence.addAll(search.testSuiteString(ts, criterion));
						System.out.println(ts);
						System.out.println(tratamentFile(firstSearchListSequence, typeFile, criterion));
					}
					else {
						List<List<String>> testSequence = search.breadthFirst(state, g);
						firstSearchListSequence.addAll(testSequence);
						System.out.println(testSequence);
						System.out.println(tratamentFile(firstSearchListSequence, typeFile, criterion));
					}
				}
				else if(i == 2 || i == 6) {
					if(criterion.equals("Alltrans")) {
						List<List<Transition>> ts = search.dfs(state, g);
						firstSearchListSequence.addAll(search.testSuiteString(ts, criterion));
						//System.out.println(ts);
						//System.out.println(tratamentFile(firstSearchListSequence, typeFile, criterion));
					}
					else {
						List<List<String>> testSequence = search.depthFirst(state, g);
						firstSearchListSequence.addAll(testSequence);
						//System.out.println(testSequence);
						//System.out.println(tratamentFile(firstSearchListSequence, typeFile, criterion));
					}
				}
			}
		}
		
		if(i == 1 || i == 5){
			if(typeFile.equals("xml")) reader.insertFile(path.substring(0, path.length() - (path.length() - path.lastIndexOf("/")))+"/tsbreadth"+criterion+".txt", tratamentFile(firstSearchListSequence, typeFile, criterion));
			else{
				reader.insertFile("src/"+path.substring(0, path.length() - (path.length() - path.lastIndexOf("/")))+"/tsbreadth"+criterion+".txt", tratamentFile(firstSearchListSequence, typeFile, criterion));
				//reader.insertFile(path+"/tsbreadth.txt", tratamentFile(firstSearchListSequence, typeFile));
			}
		}
		else if(i == 2 || i == 6){
			if(typeFile.equals("xml")) reader.insertFile(path.substring(0, path.length() - (path.length() - path.lastIndexOf("/")))+"/tsdepth"+criterion+".txt", tratamentFile(firstSearchListSequence, typeFile, criterion));
			else {
				reader.insertFile("src/"+path.substring(0, path.length() - (path.length() - path.lastIndexOf("/")))+"/tsdepth"+criterion+".txt", tratamentFile(firstSearchListSequence, typeFile, criterion));				
			}
		}
	}
	
	public void eulerianCycleTestCase(String path, String typeFile, Reader reader, String criterion, Graph graphBalanced, boolean typeGraph){
		GenerateTestCase testCaseSearch = new GenerateTestCase(graphBalanced.clone());
		List<String> testSequence;
		
		if(typeGraph) testSequence = testCaseSearch.eulerianCycleTestSuite();
		else testSequence = testCaseSearch.eulerianCycle();
		//System.out.println(testSequence);
		
		//List<List<State>> testSequenceBreadth = testCaseBreadthFirstSearch.initial();
		//GeraCasosTeste testCaseBreadthFirstSearch = new GeraCasosTeste(graphBalanced);
		//List<List<State>> testSequenceBreadth = testCaseBreadthFirstSearch.inicio();
		/*List<String> testListSequence = new LinkedList<String>();
		
		for(List<State> listState: testSequence){
			if(!listState.isEmpty()){
				String testList = "";
				for(State state: listState) {
					if(typeGraph){				
						if(state.getName().length() <= 1) testList = testList + (state.getName());
						else testList = testList + (state.getName().charAt(0));
					}
					else{
						String input = state.getName().substring(state.getName().indexOf(">")+1, state.getName().indexOf("/"));
						if(input.length() <= 1) testList = testList + input;
						else testList = testList + input.charAt(0);
					}
				}
				
				//testList = testList + " = " + testList.length();
				//test.substring(0, test.length()-1)
				//testListSequence.add(testList.substring(0, testList.length()-1));
				testListSequence.add(testList);
			}
		}*/
		/*if(typeFile.equals("xml")) reader.insertFile(path.substring(0, path.length() - (path.length() - path.lastIndexOf("/")))+"/"+name+".txt", testListSequence);
		else reader.insertFile("src/"+path.substring(0, path.length() - (path.length() - path.lastIndexOf("/")))+"/"+name+".txt", testListSequence);*/
		
		if(typeFile.equals("xml")) reader.insertFile(path.substring(0, path.lastIndexOf("/"))+"/"+criterion+".txt", testSequence);
		else reader.insertFile("src/"+path.substring(0, path.lastIndexOf("/"))+"/"+criterion+".txt", testSequence);
	}

	private Graph preProcess(Graph graph) {
		Iterator<State> stateList = graph.getIteratorStateValue();
		List<State> _stateList = new LinkedList<State>();
		
		while(stateList.hasNext()) {
			State state = stateList.next();
			List<Transition> transitionList = state.getTransitions();
			
			for(int i = 0; i < transitionList.size(); i++) {
				for(int j = i+1; j < transitionList.size(); j++) {
					if(transitionList.get(i).getDestination().getName().equals(transitionList.get(j).getDestination().getName())) {
						
						Transition t = transitionList.get(j);
						//State _state = new State(state.getName()+"."+t.getInput(), "normal", true, state);
						//System.out.println(t.getInput());
						State _state = new State(state.getName()+"."+j, "normal", true, state);
						Transition _transition = new Transition("0", t.getOutput(), t.getName(), t.getDestination(), _state, false, 0);
						_state.setTransition(_transition);
						t.setDestination(_state);
						_stateList.add(_state);
					}
				}
			}
		}
		
		for(int i = 0; i < _stateList.size(); i++) {
			graph.setStateMap(_stateList.get(i).getName(), _stateList.get(i));
		}
		return graph;
	}
	
	private Graph posProcess(Graph graph) {
		Iterator<State> stateList = graph.getIteratorStateValue();
		
		while(stateList.hasNext()) {
			State state = stateList.next();
			if(state.getPre_process()) {
				State source = state.getPre_processStateSource();
				State destin = state.getTransitions().get(0).getDestination();
				
				for(Transition transition: source.getTransitions()) {
					if(transition.getDestination().getName().equals(state.getName())) {
						transition.setDestination(destin);
						break;
					}
				}
				stateList.remove();
			}
		}
		
		return graph;
	}
	
	private void count(Graph graph) {
		int count = 0;
		Iterator<State> list = graph.getIteratorStateValue();
		while(list.hasNext()) {
			State state = list.next();
			count = count + state.getTransitions().size();
		}
		System.out.println(count);
	}
	
	
	
	private void createGraphTestCase(String path, String fileName, String typeFile, int i, int criterion) throws ParserConfigurationException, SAXException, IOException{
		try{
			//Step 01: Create graph by 1) XML or 2) a TXT file
			Reader reader = new Reader();
			
			if(typeFile.equals("xml")) {
				graph = graph.openXML(path);
				Iterator<State> states = graph.getIteratorStateValue();
				String id = "abcdefghijklmnopqrstuvwxyz0123456789"; //gambiarra para substituir uma entrada do modelo real para um caracter
				
				while(states.hasNext()) {
					State state = states.next();
					List<Transition> transitions = state.getTransitions();
					
					int in = 0;
					for(Transition t: transitions) {
						t.setInput(t.getSource().getName()+">"+String.valueOf(id.charAt(in)));
						in=in+1;
					}
				}
				//System.out.println(path.substring(0, path.lastIndexOf("/"))+"/fsm1.txt");
				reader.createMEF(path.substring(0, path.lastIndexOf("/"))+"/fsm1.txt", graph);
			}
			else graph = reader.generateTXTGraph(path);
			
			//System.out.println(graph.showResult()+"\n\n");
			
			if(graph.isConexo()) {
				if(graph.getIteratorState().hasNext()){
					if(i == 1 || i == 2 || i == 5 || i == 6){
						//Step 02.A: Create test case with dual graph in Depth or Breadth-First Search
						if(criterion == 0) {
							graph.inicialState();
							firstSearchTestCase(path, typeFile, reader, i, "Alltrans", graph.clone());
						}
						else{
							//Step 02.B: Create new graph with new transitions (dual graph / transition-pair graph)
							dualGraphConverted = node.transitionsConvertedNode(graph, typeFile);
							dualGraphConverted.inicialState();
							firstSearchTestCase(path, typeFile, reader, i, "Alltranspair", dualGraphConverted.clone());
						}
					}
					else if(i == 3 || i == 7){ //Eulerian Cycle
						//Step 04: Balancing graph with a H-Switch Cover heuristic and generate test cases with Hierholzer
						//if is true, so is a normal graph; else is a dual graph
						if(criterion == 0) {
							graph.inicialState();
							eulerianCycleTestCase(path, typeFile, reader, "tseulerAlltrans", balancing.inicio(graph.clone()), true);
						}
						else{
							dualGraphConverted = node.transitionsConvertedNode(graph, typeFile);
							dualGraphConverted.inicialState();
							eulerianCycleTestCase(path, typeFile, reader, "tseulerAlltranspair", balancing.inicio(dualGraphConverted.clone()), false);
						}
					}
					else if(i == 4 || i == 8){ //Chinese Postman Problem
						//Step 06: Balancing graph with Chinese Postman Problem (CPP) and generate test cases with Hierholzer
						ChinesePostmanProblem cpp = new ChinesePostmanProblem();
						
						if(criterion == 0) {
							graph.inicialState();
							eulerianCycleTestCase(path, typeFile, reader, "tspccAlltrans", posProcess(cpp.testCasePCC(preProcess(graph.clone()))), true);
						}
						else {
							dualGraphConverted = node.transitionsConvertedNode(graph, typeFile);
							dualGraphConverted.inicialState();
							eulerianCycleTestCase(path, typeFile, reader, "tspccAlltranspair", posProcess(cpp.testCasePCC(preProcess(dualGraphConverted.clone()))), false);
						}
					}
				}
			}
			else {
				System.out.println("This graph isn't conexo! -> "+path);
			}
		}
		catch(NullPointerException np){
			System.out.println("ERROR! File is empty.\n"+np);
		}
		catch(IOException io){
			System.out.println("ERROR! Problem in reader XML or TXT file.\n"+io);
		}
		catch(SAXException se){
			System.out.println("ERROR! Problem in reader XML file in SAXHandler.\n"+se);
		}
		
	}

	private void source(int i, int criterion, String typeFileName) throws ParserConfigurationException, SAXException, IOException {
		File DIR = new File("./src/SwitchCover/");
		String typeFile = "";
		
		if(i >= 1 && i <= 4) typeFile = "xml";
		else typeFile = "file";
		
		for (File pathXMLFile : DIR.listFiles()) { //[XML, TXT]...
			if (pathXMLFile.getName().equals(typeFile)) { // XML or TXT
				for(File pathMEF : pathXMLFile.listFiles()){ //APEX or SWPDC | 4-4-4, 8-8-8...
					//System.out.println(pathMEF.getName());
					if(!pathMEF.getName().equals(".DS_Store")){
						//if(pathMEF.getName().equals("swpdc")) {
						for(File pathNumber : pathMEF.listFiles()){ //1, 2, 3...
							if(!pathNumber.getName().equals(".DS_Store")){
								//if(pathNumber.getName().equals("1")) {
								//System.out.println(pathNumber);
								for(File file : pathNumber.listFiles()){ //fsm1, fsm2...
									if(!file.getName().equals(".DS_Store")){
										if(!file.getName().contains("tsdepth") && 
										   !file.getName().contains("tsbreadth") && 
										   !file.getName().contains("tseuler") &&
										   !file.getName().contains("tspcc") && 
										   !file.getName().contains("timeDepth") && 
										   !file.getName().contains("timeBreadth") && 
										   !file.getName().contains("timeEuler") &&
										   !file.getName().contains("timeCPP") /*&& !file.getName().contains("fsm1")*/){
											String path = "";
											
											if(typeFile.equals("xml")) path = "./src/SwitchCover/"+pathXMLFile.getName()+"/"+pathMEF.getName()+"/"+pathNumber.getName()+"/"+file.getName();
											else path = "/SwitchCover/"+pathXMLFile.getName()+"/"+pathMEF.getName()+"/"+pathNumber.getName()+"/"+file.getName();
											System.out.println(path);
											Main main = new Main();
											main.createGraphTestCase(path, file.getName(), typeFile, i, criterion);
											
											/*long duracaoEmMilissegundos = 0;
											for(int ti = 0; ti < 100; ti++) {
												Instant start = Instant.now();
												main.createGraphTestCase(path, file.getName(), typeFile, i, criterion);
												Instant stop = Instant.now();
												
												Duration duration = Duration.between(start, stop);
												duracaoEmMilissegundos = duracaoEmMilissegundos + duration.toMillis();
											}
												
											/*Reader read = new Reader();
											//reader.insertFile(+"/"+name+".txt", testListSequence);
											
											if(typeFile.equals("xml")) {
												System.out.println(path.substring(0, path.lastIndexOf("/"))+"/time"+typeFileName+".txt"+" TIME (miliseconds): "+ (duracaoEmMilissegundos/100));
												read.insertFile(path.substring(0, path.lastIndexOf("/"))+"/time"+typeFileName+".txt", duracaoEmMilissegundos);
											}
											else {
												System.out.println("src/"+path.substring(0, path.lastIndexOf("/"))+"/time"+typeFileName+".txt"+" TIME (miliseconds): "+ (duracaoEmMilissegundos/100));
												read.insertFile("src/"+path.substring(0, path.lastIndexOf("/"))+"/time"+typeFileName+".txt", duracaoEmMilissegundos);
											}*/
										}
									}
								}
								//}
							}
						}
					}
				}				
			}
			//}
		}
	}
	
	private int inicialize() {
		while (true) {
			System.out.print(
					"\nHi and welcome the H-Switch Cover program. For more informations, please take a look the README.txt file in source of project."
							+ "\nWould do you like to generate test cases with: \n"
							+ "\n1- [XML] Breadth First Search    | 5- [TXT] Breadth First Search"
							+ "\n2- [XML] Depth First Search      | 6- [TXT] Depth First Search"
							+ "\n3- [XML] Eulerian Cycle          | 7- [TXT] Eulerian Cycle"
							+ "\n4- [XML] Chinese Postman Problem | 8- [TXT] Chinese Postman Problem"
							+ "\n0- [Exit] ");

			try {
				Scanner input = new Scanner(System.in);
				int r = 0;
				
				try{
					r = Integer.valueOf(input.next());
				}
				finally{
					input.close();
				}

				if (r < 0 || r > 8) System.out.println("\n\n[Only number between 0 and 8!]");
				else if (r == 0) System.exit(0);
				else return r;
			}
			catch (Exception e) {
				System.out.println("\n\n[Only number between 0 and 8!]");
			}
		}
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		Main main = new Main();
		
		//main.source(main.inicialize());
		//main.source(4, 0, "CPPAlltrans");
		//main.source(3, 0, "EulerianAlltrans"); // 0: all transitions, 1: all transitions-pairs
		//main.source(2, 0, "DepthAlltrans"); // 0: all transitions, 1: all transitions-pairs
		main.source(1, 0, "BreadthAlltrans"); // 0: all transitions, 1: all transitions-pairs
		
		//main.source(4, 1, "CPPAlltranspair");
		//main.source(3, 1, "EulerianAlltranspair"); // 0: all transitions, 1: all transitions-pairs
		//main.source(2, 1, "DepthAlltranspair"); // 0: all transitions, 1: all transitions-pairs
		//main.source(1, 1, "BreadthAlltranspair"); // 0: all transitions, 1: all transitions-pairs
		
		/*for(int x = 0 ; x < 1; x++) { // 0: all transitions, 1: all transitions-pairs
			String criterion = "";
			
			if(x == 0) criterion = "Alltrans";
			else criterion = "Alltranspair";
			
			for(int i = 1; i <= 8; i++) {
				String typeFileName = "";
				
				if(i == 1 || i == 5) typeFileName = "Breadth"+criterion;
				else if(i == 2 || i == 6) typeFileName = "Depth"+criterion;
				else if(i == 3 || i == 7) typeFileName = "Eulerian"+criterion;
				if(i == 4 || i == 8) typeFileName = "CPP"+criterion;
					
				main.source(i, x, typeFileName);
			}
		}*/
	}

}