package entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
public class Lists {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne
	private Board board;
	@OneToMany(mappedBy="lists")
	@Size(min=1,message="at least 1 card")
	private List<Card> cards;
	
	public long getId()
	{
		return id;
	}
	public String getListName()
	{
		return name;
	}
	public Board getBoard()
	{
		return board;
	}
	public void setListName(String name)
	{
		this.name=name;
	}
	public void setBoard(Board board)
	{
		this.board=board;
	}
	public void setCards(List<Card> cards)
	{
		this.cards=cards;
	}

}
