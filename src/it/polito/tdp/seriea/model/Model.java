package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private List<Season> stagioni;
	private SerieADAO dao;
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo;
	private List<Team> squadre;
	private Map<String, Team> idMapSquadre;
	private List<Match> matches;
	
	List<Team> best ;
	Set<DefaultWeightedEdge> archiUsati;
	
	public Model() {
		dao = new SerieADAO();
		this.stagioni = dao.listSeasons();
		idMapSquadre = new HashMap<>();
	}

	public List<Season> getAllSeason() {
		return this.stagioni;
	}

	public void creaGrafo(Season stagioneScelta) {

		this.grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		squadre = dao.listTeams(idMapSquadre);
		matches = dao.listMatch(idMapSquadre, stagioneScelta);
		
		for(Match m : matches) {
			grafo.addVertex(m.getHomeTeam());
			grafo.addVertex(m.getAwayTeam());
			
			int peso = 0;
			switch(m.getFtr()) {
			case "H":
				peso = 1;
				break;
				
			case "D":
				peso = 0;
				break;
				
			case "A":
				peso = -1;
				break;
			}
			
			Graphs.addEdgeWithVertices(grafo, m.getHomeTeam(), m.getAwayTeam(), peso);
					
		}
	}

	public List<Team> getClassifica() {
		
		for(Team t : squadre) {
			t.setPunti(0);
		}
		List<Team> classifica = new ArrayList<>();
		classifica.addAll(grafo.vertexSet());
		
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			Team home = grafo.getEdgeSource(e);
			Team away = grafo.getEdgeTarget(e);
			
			switch((int)grafo.getEdgeWeight(e)) {
			case 1:
				home.setPunti(home.getPunti()+3);
				break;
			case 0:
				home.setPunti(home.getPunti()+1);
				away.setPunti(away.getPunti()+1);
				break;
			case -1:
				away.setPunti(away.getPunti()+3);
				break;
			}
		}
		
		Collections.sort(classifica);
		return classifica;
	}

	public Set<Team> getSquadreAnno() {
		return this.grafo.vertexSet();
	}

	public List<Team> calcolaDomino() {

		best = new ArrayList<Team>();
		List<Team> parziale = new ArrayList<Team>();
		archiUsati = new HashSet<>();
		
		/***ATTENZIONE***/
		/**
		 * Elimina dei vertici dal grafo per renderlo
		 * gestibile dalla ricorsione.
		 * Nella soluzione "vera" questa istruzione va rimossa
		 * (però l'algoritmo non termina in tempi umani).
		 */
		this.riduciGrafo(8);
		
		for(Team t : grafo.vertexSet()) {
			parziale.add(t);
			this.recursive(1, parziale);
			parziale.remove(0);
		}
		
		return best;
	}
	
	
	private void recursive(int livello, List<Team> parziale) {
		
		Team ultimo = parziale.get(livello-1);
		
		if(parziale.size()> best.size()) {
				best = new ArrayList<>(parziale);
			}
		
		
		for(Team t : Graphs.successorListOf(grafo,ultimo)) {
			DefaultWeightedEdge arco = grafo.getEdge(ultimo, t);
			
			if(grafo.getEdgeWeight(arco)==1 && !this.archiUsati.contains(arco)){
				archiUsati.add(arco);
				parziale.add(t);
				this.recursive(livello+1, parziale);
				parziale.remove(parziale.size()-1);
				archiUsati.remove(arco);
			}
		}
		
	}

	/**
	 * cancella dei vertici dal grafo in modo che la sua dimensione
	 * sia solamente pari a {@code dim} vertici
	 * @param dimensione
	 */
	private void riduciGrafo(int dimensione) {
		Set<Team> togliere = new HashSet<>();
		
		Iterator<Team> it = grafo.vertexSet().iterator();
 		for(int i=0; i<grafo.vertexSet().size() - dimensione; i++) {
			togliere.add(it.next());
		}
 		grafo.removeAllVertices(togliere);
	}

	

}
