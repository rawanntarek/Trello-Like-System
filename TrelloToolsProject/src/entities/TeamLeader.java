package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class TeamLeader extends User {

	@Transient
	@OneToMany(mappedBy="teamLeader")
	private List<Board> boards= new ArrayList<>();
	public List<Board> getBoards ()
	{
		return boards;
	}
	
}
