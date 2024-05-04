package entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Table(name = "lists_table")
@Entity
public class Lists {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne
	private Board board;
	@OneToMany(mappedBy="lists",cascade = CascadeType.ALL)
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
	public List<Card> getcards()
	{
		return cards;
	}

}
