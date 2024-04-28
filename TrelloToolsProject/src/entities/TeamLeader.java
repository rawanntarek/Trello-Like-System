package entities;

import java.util.List;

import javax.persistence.OneToMany;

public class TeamLeader extends User {

	@OneToMany(mappedBy="TeamLeader")
	private List<Board> boards;
	public List<Board> getBoards()
	{
		return boards;
	}
}
