package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class TeamLeader extends User {

	@Transient
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="teamLeader")
	private List<Board> boards= new ArrayList<>();
	public List<Board> getBoards ()
	{
		return boards;
	}
	
}
