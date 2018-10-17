package SwitchCover.method;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import SwitchCover.graph.Cycle;
import SwitchCover.graph.Graph;
import SwitchCover.graph.State;
import SwitchCover.graph.Transition;

/**
 * 1� fase
 * For em todas as transi��es setando elas como false em visitadas
 * come�ar a percorrer a partir do estado inicial at� achar o estado incial 
 * novamente. 
 * [Fazer depois -> colocar um peso nas transi��es para poder j� no primeiro ciclo
 * passar no maior n�mero de estados poss�veis. ]
 * O primeiro ciclo montado deve ser adicionado em uma lista de MAP<estadoinicial, ciclo1>
 * se existir estados ainda n�o visitados fa�a:
 * pergar ciclo 1 percorrer os estados visitados, se estado for diferente do estado chave, fa�a:
 * pegar o estado e pergunta: tem transi��es de saida desse estado, se sim, come�a a gerar um 
 * novo ciclo a partir desse estado, at� retornar a ele. 
 * quando terminar, adiciona na lista de map de ciclos. 
 * como est� dentro de um for, vai ser perguntado se o proximo estado tem transi��es de saida...
 * e assim vai at� terminar e todos os estados serem visitados.
 * 
 *  2� fase
 *
 * Nesse algoritmo para percorrer ser�o considerado os estados, e n�o as transi��es. Pois as
 * transi��es iniciais j� foram transformadas em estados, assim j� estar� passando por todas 
 * as transi��es. Dessa forma otimiza o tamanho da sequencia de teste, que poderia ser gigantesca. 
 *  
 */

public class GenerateTestCase {
	
	private Graph graph = new Graph();
	
	private List<State> stateInitialList = new ArrayList<State>();
	private List<State> stateList = new ArrayList<State>();
	private List<String> listTestCase = new ArrayList<String>();
	private List<Cycle> cycleList = new ArrayList<Cycle>();
	private List<List<State>> cycleFinale = new ArrayList<List<State>>();
	
	private State stateInitial = new State();
	private boolean caseFound = false;
	private String testCase = new String();
	
	public GenerateTestCase(Graph graph){
		this.graph = graph;
	}
	
	private List<State> returnInicialStates(Graph graph){
		Iterator<State> statesIterator = graph.getIteratorStateValue();
		while(statesIterator.hasNext()){
			State state = (State)statesIterator.next();	
			if(state.getTypeState().equals("inicial")){
				stateInitialList.add(state);
			}
		}
		return stateInitialList;
	}
	
	private void setFalseTransitionsStates(){
		Iterator<State> statesIterator = graph.getIteratorStateValue();
		while(statesIterator.hasNext()){
			State state = (State)statesIterator.next();
			state.setVisited(false);
			Iterator<Transition> transitionsIterator = state.getTransitionIterator();
			
			while(transitionsIterator.hasNext()){
				Transition t = transitionsIterator.next();
				t.setVisited(false);
			}
		}
	}

	private Boolean checkStateNotVisited(){
		//Iterator<State> stateIterator = graph.getIteratorStateValue();
		
		/**CHECAR SE PRECISA TIRAR**/
		//while(stateIterator.hasNext()){
			//if(stateIterator.next().getVisited() == false){
				Iterator<State> stateIt = graph.getIteratorStateValue();
				
				while(stateIt.hasNext()){
					State stateTrans = (State)stateIt.next();
				
					if(stateTrans.getVisited() == true){
						Iterator<Transition> transitionIterator = stateTrans.getTransitionIterator();
						
						while(transitionIterator.hasNext()){							
							if(transitionIterator.next().getVisited() == false){
								stateInitial = stateTrans;
								return true;
							}
						}
					}
				}
			//}
		//}
		return false;
	}
	
	private List<Cycle> course(State state){
		Iterator<Transition> transitionsIterator = state.getTransitionIterator();
		while(transitionsIterator.hasNext() && caseFound == false){//ENTRA
			Transition transition = (Transition)transitionsIterator.next();
			if(transition.getVisited() == false){//ENTRA E NAO ENTRA
				State stateDestination = transition.getDestination();
				stateDestination.setPonderosity(stateDestination.getPonderosity()+1);
				if(!stateDestination.equals(stateInitial)){
					/**CONFIRMAR RETIRADA - PELO VISTO, ACRESCENTA MAIS UM!*/
					if (!stateDestination.equals(state)){
						stateList.add(stateDestination);
						transition.setVisited(true);
						stateDestination.setVisited(true);
						testCase = testCase + stateDestination.getName() + ", ";
						course(stateDestination);
					}
				}
				else{
					transition.setVisited(true);
					listTestCase.add(testCase);
					Cycle cycle = new Cycle();
					cycle.setStateOrigin(stateInitial);
					cycle.setCycle(testCase);
					cycle.setStateList(stateList);
					cycleList.add(cycle);

					if(checkStateNotVisited()){
						stateList = new ArrayList<State>();
						stateList.add(stateInitial);
						testCase = new String();
						stateInitial.setVisited(true);
						course(stateInitial);
					}
					caseFound = true;
					break;
				}
			}
		}
		return cycleList;
	}
	
	public List<List<State>> initial(){
		Iterator<State> statesIterator = returnInicialStates(graph).iterator();
		
		while(statesIterator.hasNext()){
			stateInitial = (State)statesIterator.next();
			setFalseTransitionsStates();
			stateInitial.setVisited(true);
			stateInitial.setInitialSequence(true);
			stateList.add(stateInitial);

			EulerCycle makeCycle = new EulerCycle(course(stateInitial));
			//int cont = 0;
			List<State> eulerCycle = makeCycle.createEulerCycle();
			Iterator<State> it = eulerCycle.iterator();
			
			while(it.hasNext()) {
				it.next().getName();
			}
			
			cycleFinale.add(eulerCycle);
		}
		return cycleFinale;
	}
}