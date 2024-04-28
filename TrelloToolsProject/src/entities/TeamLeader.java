package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class TeamLeader extends User {

	@OneToMany(mappedBy="teamLeader")
	private List<Board> boards;
	
}
