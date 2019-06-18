package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams(Map<String, Team> idMapSquadre) {
		String sql = "SELECT Team " + 
					"FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team t = new Team(res.getString("Team"));
				result.add( t) ;
				idMapSquadre.put(t.getTeam(), t);
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	

	public List<Match> listMatch(Map<String, Team> idMapSquadre, Season stagioneScelta) {
		String sql = "SELECT  * " + 
				"FROM matches AS m " + 
				"WHERE m.Season = ? " ;
		
		List<Match> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, stagioneScelta.getSeason());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Match(res.getInt("match_id"), stagioneScelta, res.getString("Div"), res.getDate("Date").toLocalDate(),
						idMapSquadre.get(res.getString("HomeTeam")), idMapSquadre.get(res.getString("AwayTeam")),
						res.getInt("FTHG"), res.getInt("FTAG"), res.getString("FTR"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	

}
