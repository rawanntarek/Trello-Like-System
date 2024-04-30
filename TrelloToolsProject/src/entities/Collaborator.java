package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
@Entity
public class Collaborator extends User {
	@ManyToMany(targetEntity = Board.class)
	private List<Board> boards;
	public List<Board> getBoards ()
	{
		return boards;
	}
	    
	}

